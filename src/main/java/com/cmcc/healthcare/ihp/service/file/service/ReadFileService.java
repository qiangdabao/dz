package com.cmcc.healthcare.ihp.service.file.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cmcc.healthcare.ihp.service.file.entity.ResultBean;

public class ReadFileService {
 
	/**
	 * 解析excel文件
	 * @param rate   年利率
	 * @param volume 回档次数
	 * @param filePath  上传后的excel文件路径
	 * @return
	 */
	public ResultBean readExcel(double rate, int volume, String filePath) {
		ResultBean resultBean = new ResultBean();
		//回款/付款日期结果集合    当前回档期 原数据
	    HashMap<Integer,String> titleMap = new HashMap<Integer,String>();
		//回款/付款日期结果集合    当前回档期 原数据
	    HashMap<Integer,String> dateMap = new HashMap<Integer,String>();		
		//付款集合    当前回档期  原数据
		HashMap<Integer,Double> paymentMap = new HashMap<Integer,Double>();
		//回款集合     当前回档期  原数据
		HashMap<Integer,Double> gatherMap = new HashMap<Integer,Double>();		
		//付款统计集合     当前回档期    
	    HashMap<Integer,Double> paymentTotalMap = new HashMap<Integer,Double>();
		//回款统计集合     当前回档期
		HashMap<Integer,Double> gatherTotalMap = new HashMap<Integer,Double>();		
		//垫资金额集合     当前回档期
		HashMap<Integer,Double> dzMap = new HashMap<Integer,Double>();
		//垫资时长集合     当前回档期
		HashMap<Integer,Long> dzscMap = new HashMap<Integer,Long>();		
		//垫资成本集合     当前回档期
		HashMap<Integer,Double> dzcbMap = new HashMap<Integer,Double>();	
		
		try {
			Workbook wb = readExcel(filePath); // 获得excel文件对象workbook			
			Sheet s = wb.getSheetAt(0); // 获取指定工作表<这里获取的是第一个>
			
			//读取第一行标题内容
			Row row0 = s.getRow(0); 
			System.out.println("第一行 共   "+row0.getPhysicalNumberOfCells()+"  列");
			for (int j = 1;j < row0.getPhysicalNumberOfCells(); j++) {  
				String titleVal = getStringCellValue(row0.getCell(j));
				titleMap.put(j, titleVal);
				System.out.print(titleVal+"  ");
			}
			System.out.println();
			
			//读取第二行日期内容
			Row row1 = s.getRow(1); 
			System.out.println("第二行 共   "+row1.getPhysicalNumberOfCells()+"  列");
			for (int j = 1;j < row1.getPhysicalNumberOfCells()-1; j++) { 
				String dateVal = getDateCellValue(row1.getCell(j));
				dateMap.put(j, dateVal);
				System.out.print(dateVal+"  ");
			}
			System.out.println();

			//读取第三行回款内容
			Row row2 = s.getRow(2); 
			double gatherTotal = 0;
			System.out.println("第三行 共   "+row2.getPhysicalNumberOfCells()+"  列");
			for (int j = 1;j < row2.getPhysicalNumberOfCells()-1; j++) { 
				String gatherVal = getCellValue(row2.getCell(j));
				double f_gatherVal = 0;
				if (!"".equals(gatherVal)) {
					f_gatherVal =  Double.parseDouble(gatherVal);
				}
				gatherMap.put(j ,f_gatherVal);
				//当前时点的回款统计
				gatherTotal = gatherTotal + f_gatherVal;
				gatherTotalMap.put(j, gatherTotal);
				System.out.print(gatherVal+"  ");
			}
			System.out.println();
			
			//读取第四行付款内容
			Row row3 = s.getRow(3);
			double paymentTotal = 0;
			System.out.println("第四行 共   "+row3.getPhysicalNumberOfCells()+"  列");
			for (int j = 1;j < row3.getPhysicalNumberOfCells()-1; j++) { 
				String paymentVal = getCellValue(row3.getCell(j));
				double f_paymentVal = 0;
				if (!"".equals(paymentVal)) {
					f_paymentVal = Double.parseDouble(paymentVal);
				}
				paymentMap.put(j ,f_paymentVal);
				//当前时点的付款统计
				paymentTotal = paymentTotal + f_paymentVal;
				paymentTotalMap.put(j, paymentTotal);
				
				//当前时点的垫资统计,垫资时长统计，垫资成本统计     当前时点的垫资 = 当前时点的付款 - 当前时点的回款
				double c_gatherTotal = gatherTotalMap.get(j);  				
				if (c_gatherTotal >= paymentTotal) {
					dzMap.put(j, Double.parseDouble("0"));
					dzscMap.put(j, Long.parseLong("0"));
					dzcbMap.put(j, Double.parseDouble("0"));
				} else {
					//垫资统计
					dzMap.put(j, (paymentTotal-c_gatherTotal) );
					// 遍历到最后一列
					if (j == row3.getPhysicalNumberOfCells()-2 ) {
						dzscMap.put(j, Long.parseLong("0"));
						dzcbMap.put(j, Double.parseDouble("0"));
					} else {						
						//垫资时长统计
						dzscMap.put(j, this.computeDaySub(dateMap.get(j), dateMap.get(j+1)));
						dzcbMap.put(j, this.computeDzcb(rate, dzMap.get(j), dzscMap.get(j)));
					}
				}
                
				System.out.print(paymentVal+"  ");
			}
			System.out.println();
			
 
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		resultBean.setDateMap(dateMap);
		resultBean.setPaymentMap(paymentMap);
		resultBean.setGatherMap(gatherMap);
		resultBean.setPaymentTotalMap(paymentTotalMap);
		resultBean.setGatherTotalMap(gatherTotalMap);
		resultBean.setDzMap(dzMap);
		resultBean.setDzscMap(dzscMap);
		resultBean.setDzcbMap(dzcbMap);		
		return resultBean;
   }

 /**
  * xls/xlsx都使用的Workbook
  * @param fileName
  */
  public  Workbook readExcel(String fileName){
      Workbook wb = null;
      if(fileName==null){
          return null;
      }
      String extString = fileName.substring(fileName.lastIndexOf("."));
      InputStream is = null;
      try {
	      is = new FileInputStream(fileName);
	      if(".xls".equals(extString)){
	          return wb = new HSSFWorkbook(is);
	      }else if(".xlsx".equals(extString)){
	              return wb = new XSSFWorkbook(is);
          }
          
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }
      return wb;
  }
	  
  //读取cell单元格的值，如果为日期格式，进行转换
  @SuppressWarnings("deprecation")
  public String getStringCellValue(Cell cell) {
      if (cell == null){
          return "";
      }
	  if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
	      return cell.getStringCellValue();
	  } else {
	  	return "";
	  }
  }
	  
  //读取cell单元格的值，如果为日期格式，进行转换
  @SuppressWarnings("deprecation")
  public String getDateCellValue(Cell cell) {
	  if (cell == null){
          return "";
      }
	  if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
	      return cell.getStringCellValue();
	  } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
	      return String.valueOf(cell.getBooleanCellValue());
	  } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
	      return cell.getCellFormula();
	  } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
	      short format = cell.getCellStyle().getDataFormat();
	      SimpleDateFormat sdf = null;
	      if (format == 14 || format == 31 || format == 57 || format == 58  
	              || (176<=format && format<=178) || (182<=format && format<=196) 
	              || (210<=format && format<=213) || (208==format ) ) { // 日期
	          sdf = new SimpleDateFormat("yyyy/M/dd");
	      } else if (format == 20 || format == 32 || format==183 || (200<=format && format<=209) ) { // 时间
	          sdf = new SimpleDateFormat("HH:mm");
	      } else { // 不是日期格式
	          return String.valueOf(cell.getNumericCellValue());
	      }
	      double value = cell.getNumericCellValue();
	      Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
	      if(date==null || "".equals(date)){
	          return "";
	      }
	      String result="";
	      try {
	          result = sdf.format(date);
	      } catch (Exception e) {
	          e.printStackTrace();
	          return "";
	      }
	      return result;
	  }
	  return "";
	  }
	  	
  // 读取cell单元格的值，如果为日期格式，进行转换
  @SuppressWarnings("deprecation")
  public String getCellValue(Cell cell) {
	  if (cell == null){
          return "";
      }
	  if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
	      return cell.getStringCellValue();
	  } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
	      return String.valueOf(cell.getBooleanCellValue());
	  } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
	      return cell.getCellFormula();
	  } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
	      short format = cell.getCellStyle().getDataFormat();
	      SimpleDateFormat sdf = null;
	      if (format == 14 || format == 31 || format == 57 || format == 58  
	              || (176<=format && format<=178) || (182<=format && format<=196) 
	              || (210<=format && format<=213) || (208==format ) ) { // 日期
	          sdf = new SimpleDateFormat("yyyy-MM-dd");
	      } else if (format == 20 || format == 32 || format==183 || (200<=format && format<=209) ) { // 时间
	          sdf = new SimpleDateFormat("HH:mm");
	      } else { // 不是日期格式
	          return String.valueOf(cell.getNumericCellValue());
	      }
	      double value = cell.getNumericCellValue();
	      Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
	      if(date==null || "".equals(date)){
	          return "";
	      }
	      String result="";
	      try {
	          result = sdf.format(date);
	      } catch (Exception e) {
	          e.printStackTrace();
	          return "";
	      }
	      return result;
	  }
	  return "";
   }
  
	/**
   * <li>功能描述：时间相减得到天数
   * @param beginDateStr
   * @param endDateStr
   * @return
   * long 
   * @author Administrator
   */
  public long computeDaySub(String beginDateStr,String endDateStr){
      long day=0;
      if ("".equals(beginDateStr) || "".equals(endDateStr)) {
    	  return 0;
      }
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");    
      Date beginDate;
      Date endDate;
      try{
          beginDate = format.parse(beginDateStr);
          endDate= format.parse(endDateStr);    
          day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);    
          //System.out.println("相隔的天数="+day);   
      } catch (ParseException e) {
          // TODO 自动生成 catch 块
          e.printStackTrace();
      }   
      return day;
  }
  
  /**
   * 计算垫资成本
   * @param rate
   * @param dz
   * @param sc
   * @return
   */
  public double computeDzcb (double rate, double dz, long sc) {
  	double dzcb =  sc * dz * rate /365;
  	BigDecimal b =  new BigDecimal(dzcb);  
  	return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
  }


  public void test() {
      System.out.println("1234567890---1");
      System.out.println("1234567890---2");
      System.out.println("1234567890---3");
      System.out.println("1234567890---4");
      System.out.println("1234567890---5");
      System.out.println("1234567890---6");
      System.out.println("1234567890---7");
  }
}
