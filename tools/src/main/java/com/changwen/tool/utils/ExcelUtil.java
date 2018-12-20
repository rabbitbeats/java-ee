package com.changwen.tool.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Excel文件操作工具类，包括读、写、合并等功能
 */
public class ExcelUtil {

    //%%%%%%%%-------常量部分 开始----------%%%%%%%%%  
    /**
     * 默认的开始读取的行位置为第一行（索引值为0）
     */
    private final static int READ_START_POS = 0;

    /**
     * 默认结束读取的行位置为最后一行（索引值=0，用负数来表示倒数第n行）
     */
    private final static int READ_END_POS = 0;

    /**
     * 默认Excel内容的开始比较列位置为第一列（索引值为0）
     */
    private final static int COMPARE_POS = 0;

    /**
     * 默认多文件合并的时需要做内容比较（相同的内容不重复出现）
     */
    private final static boolean NEED_COMPARE = true;

    /**
     * 默认多文件合并的新文件遇到名称重复时，进行覆盖
     */
    private final static boolean NEED_OVERWRITE = true;

    /**
     * 默认只操作一个sheet
     */
    private final static boolean ONLY_ONE_SHEET = true;

    /**
     * 默认读取第一个sheet中（只有当ONLY_ONE_SHEET = true时有效）
     */
    private final static int SELECTED_SHEET = 0;

    /**
     * 默认从第一个sheet开始读取（索引值为0）
     */
    private final static int READ_START_SHEET = 0;

    /**
     * 默认在最后一个sheet结束读取（索引值=0，用负数来表示倒数第n行）
     */
    private final static int READ_END_SHEET = 0;

    /**
     * 默认打印各种信息
     */
    private final static boolean PRINT_MSG = true;

    //%%%%%%%%-------常量部分 结束----------%%%%%%%%%  


    //%%%%%%%%-------字段部分 开始----------%%%%%%%%%  
    /**
     * Excel文件路径
     */
    private String excelPath = "data.xlsx";

    /**
     * 设定开始读取的位置，默认为0
     */
    private int startReadPos = READ_START_POS;

    /**
     * 设定结束读取的位置，默认为0，用负数来表示倒数第n行
     */
    private int endReadPos = READ_END_POS;

    /**
     * 设定开始比较的列位置，默认为0
     */
    private int comparePos = COMPARE_POS;

    /**
     * 设定汇总的文件是否需要替换，默认为true
     */
    private boolean isOverWrite = NEED_OVERWRITE;

    /**
     * 设定是否需要比较，默认为true(仅当不覆写目标内容是有效，即isOverWrite=false时有效)
     */
    private boolean isNeedCompare = NEED_COMPARE;

    /**
     * 设定是否只操作第一个sheet
     */
    private boolean onlyReadOneSheet = ONLY_ONE_SHEET;

    /**
     * 设定操作的sheet在索引值
     */
    private int selectedSheetIdx = SELECTED_SHEET;

    /**
     * 设定操作的sheet的名称
     */
    private String selectedSheetName = "";

    /**
     * 设定开始读取的sheet，默认为0
     */
    private int startSheetIdx = READ_START_SHEET;

    /**
     * 设定结束读取的sheet，默认为0，用负数来表示倒数第n行
     */
    private int endSheetIdx = READ_END_SHEET;

    /**
     * 设定是否打印消息
     */
    private boolean printMsg = PRINT_MSG;


    //%%%%%%%%-------字段部分 结束----------%%%%%%%%%

    public ExcelUtil() {

    }

    public ExcelUtil(String excelPath) {
        this.excelPath = excelPath;
    }

    /**
     * 获取Excel文件的Workbook
     *
     * @param inExcelFile Excel文件路径对应的数据流
     * @return 返回Excel文件的Workbook
     * @throws IOException 如果没有文件还要读文件时，抛出该异常
     */
    private Workbook getWorkbook(InputStream inExcelFile) throws IOException {
        Workbook workbook = null;

        //测试此输入流是否支持mark(在此输入流中标记当前的位置)方法。
        if (!inExcelFile.markSupported()) {
            //创建一个PushbackReader对象，指定回推缓冲区的长度为8,作用允许试探性的读取数据流，如果不是我们想要的则返还回去
            inExcelFile = new PushbackInputStream(inExcelFile, 8);
        }

        /*HSSF是指2007年以前的,XSSF是指2007年版本以上的，
         * 是根据文件的头部信息去比对进行判断的，此时，就算改了后缀名，还是原来的类型。*/
        if (POIFSFileSystem.hasPOIFSHeader(inExcelFile)) {
            workbook = new HSSFWorkbook(inExcelFile);

        } else if (POIXMLDocument.hasOOXMLHeader(inExcelFile)) {
            try {
                workbook = new (OPCPackage.open(inExcelFile));

            } catch (InvalidFormatException e) {
                // log.error("当运行fetchSheet方法里的OPCPackage.open(inp)时，出现该异常" + e);
                e.printStackTrace();
            }

        } else {
            // log.error("您的excel版本目前poi解析不了,或者不是Excel文件");
            throw new IllegalArgumentException("你的excel版本目前poi解析不了,或者不是Excel文件");
        }

        return workbook;
    }


    /**
     * 还原设定（其实是重新new一个新的对象并返回）
     */
    public ExcelUtil RestoreSettings() {
        return new ExcelUtil(this.excelPath);
    }


    public List<Row> readExcel() throws IOException {
        return readExcel(this.excelPath);
    }

    public List<Row> readExcel(String xlsPath) throws IOException {

        //扩展名为空时，  
        if ("".equals(xlsPath)) {
            throw new IOException("文件路径不能为空！");
        }

        File file = new File(xlsPath);
        if (!file.exists()) {
            throw new IOException("文件不存在！");
        }

        Workbook wb = null;// 用于Workbook级的操作，创建、删除Excel
        List<Row> rowList = new ArrayList<Row>();

        try {
            // 读取Excel
            wb = getWorkbook(new FileInputStream(file));
            rowList = readExcel(wb);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rowList;
    }


    /**
     * 写入方法
     */
    public void writeExcel(List<Row> rowList) throws IOException {
        writeExcel(rowList, excelPath);
    }

    public void writeExcel(List<Row> rowList, String xlsPath) throws IOException {
        // 判断文件路径是否为空
        if (StringUtils.isBlank(xlsPath)) {
            out("文件路径不能为空");
            throw new IOException("文件路径不能为空");
        }

        // 判断列表是否有数据，如果没有数据，则返回
        if (rowList == null || rowList.size() == 0) {
            out("文档为空");
            return;
        }

        try {
            Workbook wb = null;

            // 判断文件是否存在
            File file = new File(xlsPath);
            if (file.exists()) {
                // 如果复写，则删除后
                if (isOverWrite) {
                    file.delete();
                    wb = getWorkbook(new FileInputStream(file));

                } else {
                    // 如果文件存在，则读取Excel
                    wb = new HSSFWorkbook(new FileInputStream(file));
                }
            } else {
                // 如果文件不存在，则创建一个新的Excel
                wb = new HSSFWorkbook(new FileInputStream(xlsPath));
            }

            // 将rowlist的内容写到Excel中
            writeExcel(wb, rowList, xlsPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*** 
     * 读取单元格的值
     */
    public static String getCellValue(Cell cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    result = double2String(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    //result = cell.getCellFormula();
                    int type = cell.getCachedFormulaResultType();
                    if (type == 0) {
                        result = double2String(cell.getNumericCellValue());
                    } else if (type == 1) {
                        result = cell.getStringCellValue();
                    } else if (type == 5) {
                        result = cell.getErrorCellValue();
                    }
                    //result = String.valueOf(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }

    /**
     * 科学计数法问题解决
     */
    public static String double2String(double test) {
        BigDecimal bdTest = null;
        if (test % 1 == 0) {
            long i = new Double(test).longValue();
            bdTest = new BigDecimal(String.valueOf(i));
        } else {
            bdTest = new BigDecimal(String.valueOf(test));
        }
        ///System.out.println(String.valueOf(test) + "=" + bdTest.toString());
        return bdTest.toString();
    }

    /**
     * 通用读取Excel
     */
    private List<Row> readExcel(Workbook wb) {
        List<Row> rowList = new ArrayList<Row>();

        int sheetCount = 1;//需要操作的sheet数量  

        Sheet sheet = null;
        if (onlyReadOneSheet) {   //只操作一个sheet
            // 获取设定操作的sheet(如果设定了名称，按名称查，否则按索引值查)  
            sheet = selectedSheetName.equals("") ? wb.getSheetAt(selectedSheetIdx) : wb.getSheet(selectedSheetName);
        } else {                          //操作多个sheet
            sheetCount = wb.getNumberOfSheets();//获取可以操作的总数量  
        }

        // 获取sheet数目  
        for (int t = startSheetIdx; t < sheetCount + endSheetIdx; t++) {
            // 获取设定操作的sheet  
            if (!onlyReadOneSheet) {
                sheet = wb.getSheetAt(t);
            }

            //获取最后行号  
            int lastRowNum = sheet.getLastRowNum();

            if (lastRowNum > 0) {    //如果>0，表示有数据
                out("\n开始读取名为【" + sheet.getSheetName() + "】的内容：");
            }

            Row row = null;
            // 循环读取  
            for (int i = startReadPos; i <= lastRowNum + endReadPos; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    rowList.add(row);
                    /*out("第"+(i+1)+"行：",false);  
                     // 获取每一单元格的值  
                     for (int j = 0; j < row.getLastCellNum(); j++) {  
                         String value = getCellValue(row.getCell(j));  
                         //row.getCell(j).setCellValue(value);
                         if (!value.equals("")) {  
                             out(value + " | ",false);  
                         }  
                     }  
                     out(""); */
                }
            }
        }
        return rowList;
    }

    /**
     * 修改Excel，并另存为
     */
    private void writeExcel(Workbook wb, List<Row> rowList, String xlsPath) {

        if (wb == null) {
            out("操作文档不能为空！");
            return;
        }

        Sheet sheet = wb.getSheetAt(0);// 修改第一个sheet中的值  

        // 如果每次重写，那么则从开始读取的位置写，否则果获取源文件最新的行。  
        int lastRowNum = isOverWrite ? startReadPos : sheet.getLastRowNum() + 1;
        int t = 0;//记录最新添加的行数  
        out("要添加的数据总条数为：" + rowList.size());
        for (Row row : rowList) {
            if (row == null) continue;
            // 判断是否已经存在该数据  
            int pos = findInExcel(sheet, row);

            Row r = null;// 如果数据行已经存在，则获取后重写，否则自动创建新行。  
            if (pos >= 0) {
                sheet.removeRow(sheet.getRow(pos));
                r = sheet.createRow(pos);
            } else {
                r = sheet.createRow(lastRowNum + t++);
            }

            //用于设定单元格样式  
            CellStyle newstyle = wb.createCellStyle();

            //循环为新行创建单元格  
            for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                Cell cell = r.createCell(i);// 获取数据类型  
                cell.setCellValue(getCellValue(row.getCell(i)));// 复制单元格的值到新的单元格  
                // cell.setCellStyle(row.getCell(i).getCellStyle());//出错  
                if (row.getCell(i) == null) continue;
                copyCellStyle(row.getCell(i).getCellStyle(), newstyle); // 获取原来的单元格样式  
                cell.setCellStyle(newstyle);// 设置样式  
                // sheet.autoSizeColumn(i);//自动跳转列宽度  
            }
        }
        out("其中检测到重复条数为:" + (rowList.size() - t) + " ，追加条数为：" + t);

        // 统一设定合并单元格  
        setMergedRegion(sheet);

        try {
            // 重新将数据写入Excel中  
            FileOutputStream outputStream = new FileOutputStream(xlsPath);
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            out("写入Excel时发生错误！ ");
            e.printStackTrace();
        }
    }

    /**
     * 查找某行数据是否在Excel表中存在，返回行数。
     */
    private int findInExcel(Sheet sheet, Row row) {
        int pos = -1;

        try {
            // 如果覆写目标文件，或者不需要比较，则直接返回  
            if (isOverWrite || !isNeedCompare) {
                return pos;
            }
            for (int i = startReadPos; i <= sheet.getLastRowNum() + endReadPos; i++) {
                Row r = sheet.getRow(i);
                if (r != null && row != null) {
                    String v1 = getCellValue(r.getCell(comparePos));
                    String v2 = getCellValue(row.getCell(comparePos));
                    if (v1.equals(v2)) {
                        pos = i;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pos;
    }


    /**
     * 获取合并单元格的值
     */
    public void setMergedRegion(Sheet sheet) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            // 获取合并单元格位置  
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstRow = ca.getFirstRow();
            if (startReadPos - 1 > firstRow) {// 如果第一个合并单元格格式在正式数据的上面，则跳过。  
                continue;
            }
            int lastRow = ca.getLastRow();
            int mergeRows = lastRow - firstRow;// 合并的行数  
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            // 根据合并的单元格位置和大小，调整所有的数据行格式，  
            for (int j = lastRow + 1; j <= sheet.getLastRowNum(); j++) {
                // 设定合并单元格  
                sheet.addMergedRegion(new CellRangeAddress(j, j + mergeRows, firstColumn, lastColumn));
                j = j + mergeRows;// 跳过已合并的行  
            }

        }
    }


    /**
     * 复制一个单元格样式到目的单元格样式
     */
    public void copyCellStyle(CellStyle fromStyle, CellStyle toStyle) {
        toStyle.setAlignment(fromStyle.getAlignment());
        //边框和边框颜色
        toStyle.setBorderBottom(fromStyle.getBorderBottom());
        toStyle.setBorderLeft(fromStyle.getBorderLeft());
        toStyle.setBorderRight(fromStyle.getBorderRight());
        toStyle.setBorderTop(fromStyle.getBorderTop());
        toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
        toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
        toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
        toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

        //背景和前景
        toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
        toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

        toStyle.setDataFormat(fromStyle.getDataFormat());
        toStyle.setFillPattern(fromStyle.getFillPattern());
//		toStyle.setFont(fromStyle.getFont(null));
        toStyle.setHidden(fromStyle.getHidden());
        toStyle.setIndention(fromStyle.getIndention());//首行缩进
        toStyle.setLocked(fromStyle.getLocked());
        toStyle.setRotation(fromStyle.getRotation());//旋转
        toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
        toStyle.setWrapText(fromStyle.getWrapText());

    }

    /**
     * 行复制功能
     */
    public void copyRow(Workbook wb, Row fromRow, Row toRow, boolean copyValueFlag) {
        for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext(); ) {
            Cell tmpCell = (Cell) cellIt.next();
            Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
            copyCell(wb, tmpCell, newCell, copyValueFlag);
        }
    }

    /**
     * 复制单元格
     * true则连同cell的内容一起复制
     */
    public void copyCell(Workbook wb, Cell srcCell, Cell distCell,
                         boolean copyValueFlag) {
        CellStyle newstyle = wb.createCellStyle();
        copyCellStyle(srcCell.getCellStyle(), newstyle);
        //distCell.setEncoding(srcCell.getEncoding());
        //样式
        distCell.setCellStyle(newstyle);
        //评论
        if (srcCell.getCellComment() != null) {
            distCell.setCellComment(srcCell.getCellComment());
        }
        // 不同数据类型处理
        int srcCellType = srcCell.getCellType();
        distCell.setCellType(srcCellType);
        if (copyValueFlag) {
            if (srcCellType == Cell.CELL_TYPE_NUMERIC) {
                distCell.setCellValue(srcCell.getNumericCellValue());
            } else if (srcCellType == Cell.CELL_TYPE_STRING) {
                distCell.setCellValue(srcCell.getRichStringCellValue());
            } else if (srcCellType == Cell.CELL_TYPE_BLANK) {
                // nothing21
            } else if (srcCellType == Cell.CELL_TYPE_BOOLEAN) {
                distCell.setCellValue(srcCell.getBooleanCellValue());
            } else if (srcCellType == Cell.CELL_TYPE_ERROR) {
                distCell.setCellErrorValue(srcCell.getErrorCellValue());
            } else if (srcCellType == Cell.CELL_TYPE_FORMULA) {
                distCell.setCellFormula(srcCell.getCellFormula());
            } else { // nothing29
            }
        }
    }

    /**
     * @param starRow 插入起始行，如 starRow = 1，则插入行index以2开始
     * @param rows    插入的行数
     */
    public void insertRow(Workbook wb, Sheet sheet, int starRow, int rows) {

        sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows, true, false);

        for (int i = 0; i < rows; i++) {
            Row targetRow = null;
            Row upRow = null;
            Cell upCell = null;
            Cell targetCell = null;
            short m;

            upRow = sheet.getRow(starRow - 1);
            targetRow = sheet.createRow(starRow);

            for (m = upRow.getFirstCellNum(); m < upRow.getLastCellNum(); m++) {
                targetCell = targetRow.createCell(m);
                upCell = upRow.getCell(m);
                targetCell.setCellStyle(upCell.getCellStyle());
                targetCell.setCellType(upCell.getCellType());
            }
        }
    }

    /**
     * 打印消息，
     */
    private void out(String msg) {
        if (printMsg) {
            out(msg, true);
        }
    }

    /**
     * 打印消息，
     */
    private void out(String msg, boolean tr) {
        if (printMsg) {
            System.out.print(msg + (tr ? "\n" : ""));
        }
    }


    /**
     * 导出excel工具类
     */
    public void export(HttpServletRequest request, HttpServletResponse response, String name, String cols, String datas) {
        if (name == null || cols == null || datas == null) {
            return;
        }
        try {
            List<Map<String, Object>> colList = new ObjectMapper().readValue(cols.replaceAll("&quot;", "\""), new TypeReference<List<Map<String, Object>>>() {
            });
            List<Map<String, Object>> dataList = new ObjectMapper().readValue(datas.replaceAll("&quot;", "\""), new TypeReference<List<Map<String, Object>>>() {
            });
            @SuppressWarnings("resource")
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet(name);
            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setFillForegroundColor(IndexedColors.DARK_TEAL.index);//颜色
            titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            titleStyle.setBorderBottom(CellStyle.BORDER_THIN);
            titleStyle.setBorderLeft(CellStyle.BORDER_THIN);
            titleStyle.setBorderRight(CellStyle.BORDER_THIN);
            titleStyle.setBorderTop(CellStyle.BORDER_THIN);
            titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            titleStyle.setAlignment(CellStyle.ALIGN_CENTER);//左右居中
            Font font = wb.createFont();
            font.setColor(IndexedColors.WHITE.index);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            titleStyle.setFont(font);

            CellStyle headStyle = wb.createCellStyle();
            headStyle.setFillForegroundColor(IndexedColors.WHITE.index);
            headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            headStyle.setBorderBottom(CellStyle.BORDER_THIN);
            headStyle.setBorderLeft(CellStyle.BORDER_THIN);
            headStyle.setBorderRight(CellStyle.BORDER_THIN);
            headStyle.setBorderTop(CellStyle.BORDER_THIN);
            headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            headStyle.setAlignment(CellStyle.ALIGN_CENTER);//左右居中
            Font fontHead = wb.createFont();
            fontHead.setBoldweight(Font.BOLDWEIGHT_BOLD);//字体样式
            fontHead.setFontHeightInPoints((short) 12);//设置字体大小
            headStyle.setFont(fontHead);

            CellStyle oddStyle = wb.createCellStyle();
            oddStyle.setBorderBottom(CellStyle.BORDER_THIN);
            oddStyle.setBorderLeft(CellStyle.BORDER_THIN);
            oddStyle.setBorderRight(CellStyle.BORDER_THIN);
            oddStyle.setBorderTop(CellStyle.BORDER_THIN);
            oddStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            CellStyle evenStyle = wb.createCellStyle();
            evenStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            evenStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            evenStyle.setBorderBottom(CellStyle.BORDER_THIN);
            evenStyle.setBorderLeft(CellStyle.BORDER_THIN);
            evenStyle.setBorderRight(CellStyle.BORDER_THIN);
            evenStyle.setBorderTop(CellStyle.BORDER_THIN);
            evenStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            //合并第一行单元格
            Row rowOne = sheet.createRow(0);
            rowOne.setHeight((short) 470);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colList.size() - 1));
            Cell cell;
            cell = rowOne.createCell(0);
            cell.setCellStyle(headStyle);
            cell.setCellValue(name);

            //增加最后第一行合并前最后一个cell的边框
            cell = rowOne.createCell(colList.size() - 1);
            cell.setCellStyle(headStyle);

            Row row = sheet.createRow(1);
            row.setHeight((short) 320);
            Map<String, Object> col, data;

            for (int i = 0; i < colList.size(); i++) {
                col = colList.get(i);
                cell = row.createCell(i);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(col.get("title").toString());
                //   	int a=(int) (Integer.parseInt(col.get("width").toString()) * 40);
                byte[] b = col.get("title").toString().getBytes("gbk");
                //设置execl列宽为标题的2倍
                int d = b.length * 256 * 2;
                if (b.length >= 255) {
                    d = 255 * 256;
                }
                sheet.setColumnWidth(i, d);
            }
            Object value;
            for (int i = 0; i < dataList.size(); i++) {
                data = dataList.get(i);
                row = sheet.createRow(i + 2);
                row.setHeight((short) 320);
                for (int j = 0; j < colList.size(); j++) {
                    col = colList.get(j);
                    cell = row.createCell(j);
                    cell.setCellStyle(i % 2 == 0 ? oddStyle : evenStyle);
                    value = data.get(col.get("field"));
                    if (value != null) {
                        if (value instanceof Double) {
                            cell.setCellValue(((Double) value).doubleValue());
                        } else if (value instanceof Integer) {
                            cell.setCellValue(((Integer) value).intValue());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }
            }
            String fileName = name + ".xls";
            fileName = new String(fileName.getBytes("GBK"), "iso8859-1");
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);//指定下载的文件名
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            wb.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}