import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
	
public class Test1 {

public static List<Map<Integer,Object>> readFile(File file){
    // excel中第几列 ： 对应的表头
    Map<Integer,String> colAndNameMap = new HashMap<>();
    List<Map<Integer,Object>> resultList = new ArrayList<>();
    FileInputStream fs = null;
    XSSFWorkbook wb = null;
    try {
         fs = new FileInputStream(file);
         wb = new XSSFWorkbook(fs);
        for(int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++){
            //获取sheet数据
            XSSFSheet st = wb.getSheetAt(sheetIndex);
            //遍历一个sheet中每一行
            for (int rowIndex = 0; rowIndex <= st.getLastRowNum(); rowIndex++) {
            	
            	System.out.print("第  " +rowIndex+ " 行");
                // 表头：值
                Map<Integer,Object> nameAndValMap = new HashMap<>();
                // 获取到一行数据
                XSSFRow row = st.getRow(rowIndex);
                for(int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++){
                    
                    if(rowIndex==0){
                        colAndNameMap.put(cellIndex, row.getCell(cellIndex).getStringCellValue());
                        System.out.print(row.getCell(cellIndex).getStringCellValue() + "");
                    }else if(!colAndNameMap.isEmpty()){
                        nameAndValMap.put(cellIndex, buildDate(row.getCell(cellIndex)));
                        System.out.print(buildDate(row.getCell(cellIndex)) + "");
                    }
                }
                if(!nameAndValMap.isEmpty()){
                    resultList.add(nameAndValMap);
                }
                
                System.out.println();
            }
        }
        return resultList;
    } catch (FileNotFoundException e) {
        System.out.println(">>>>>>>>>>  读取excel文件时出错了！！！");
        e.printStackTrace();
    } catch (IOException e) {
        System.out.println(">>>>>>>>>>  读取excel文件时出错了！！！");
        e.printStackTrace();
    } catch (Exception e) {
        System.out.println(">>>>>>>>>>  读取excel文件时出错了！！！");
        e.printStackTrace();
    } finally{
        try {
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return null;
}

// 将excel中时间格式字段进行处理
@SuppressWarnings("deprecation")
public static String buildDate(XSSFCell cell) {
    String result = new String();
    switch (cell.getCellType()) {
    case XSSFCell.CELL_TYPE_NUMERIC:
        if (cell.getCellStyle().getDataFormat() == 58) {
            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            double value = cell.getNumericCellValue();
            Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
            result = sdf.format(date);
        } else {
            double value = cell.getNumericCellValue();
            CellStyle style = cell.getCellStyle();
            DecimalFormat format = new DecimalFormat();
            String temp = style.getDataFormatString();
            // 单元格设置成常规
            if (temp.equals("General")) {
                format.applyPattern("#");
            }
            result = format.format(value);
        }
        break;
    case XSSFCell.CELL_TYPE_STRING:// String类型
        result = cell.getStringCellValue();
        break;
    default:
        result = "";
        break;
    }
    return result;
}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test1 tt = new Test1();
		tt.readFile(new File("D://filepath//20190815152300046784.xlsx"));			
	}

}
