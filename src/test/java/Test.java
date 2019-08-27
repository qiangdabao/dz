import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Test {
    
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
	// 读取cell单元格的值，如果为日期格式，进行转换
    @SuppressWarnings("deprecation")
    public String getStringCellValue(Cell cell) {
        if (cell == null)
            return "";
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        } else {
        	return "";
        }
    }
    
	// 读取cell单元格的值，如果为日期格式，进行转换
    @SuppressWarnings("deprecation")
    public String getDateCellValue(Cell cell) {
        if (cell == null)
            return "";
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
        if (cell == null)
            return "";
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
	 * @param fileName
	 */
	public void analyzeDemo(String fileName){
 
		try {
			Workbook wb = readExcel(fileName); // 获得excel文件对象workbook			
			Sheet s = wb.getSheetAt(0); // 获取指定工作表<这里获取的是第一个>
			
			//读取第一行标题内容
			Row row0 = s.getRow(0); 
			System.out.println("第一行 共   "+row0.getPhysicalNumberOfCells()+"  列");
			for (int j = 0;j < row0.getPhysicalNumberOfCells(); j++) {  
				String value1 = getStringCellValue(row0.getCell(j));
				System.out.print(value1+"  ");
			}
			System.out.println();
			
			//读取第二行内容
			Row row1 = s.getRow(1); 
			System.out.println("第二行 共   "+row1.getPhysicalNumberOfCells()+"  列");
			for (int j = 0;j < row1.getPhysicalNumberOfCells(); j++) { 
				String value1 = getDateCellValue(row1.getCell(j));
				System.out.print(value1+"  ");
			}
			System.out.println();

			//读取第三行内容
			Row row2 = s.getRow(2); 
			System.out.println("第三行 共   "+row2.getPhysicalNumberOfCells()+"  列");
			for (int j = 0;j < row2.getPhysicalNumberOfCells(); j++) { 
				String value1 = getCellValue(row2.getCell(j));
				System.out.print(value1+"  ");
			}
			System.out.println();
			
			//读取第四行内容
			Row row3 = s.getRow(3); 
			System.out.println("第四行 共   "+row3.getPhysicalNumberOfCells()+"  列");
			for (int j = 0;j < row3.getPhysicalNumberOfCells(); j++) { 
				String value1 = getCellValue(row3.getCell(j));
				System.out.print(value1+"  ");
			}
			System.out.println();
 
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long 
     * @author Administrator
     */
    public long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
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
    
    public double getDzcb (double rate, float dz, long sc) {
    	double dzcb =  sc * dz * rate /365;
    	BigDecimal b =  new BigDecimal(dzcb);  
    	return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
    }
    
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/****/
		String fileName = "D://filepath//20190815152300046784.xlsx";
		Test test = new Test();
		//test.analyzeDemo(fileName);
		//long subDate = test.getDaySub("2019-01-31", "2019-04-30");
		//System.out.println("subDate:   " + subDate);
		double dzcb = test.getDzcb(0.055, 3200, 92);
		System.out.println("dzcb:   " + dzcb);
		
	}

}
