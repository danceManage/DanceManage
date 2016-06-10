package com.dance.manage.utils;

import com.dance.manage.bean.order.OrderDetail;
import com.dance.manage.bean.order.OrderInfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>名称</p>
 * <p/>
 * <p>wikiURL</p>
 *
 * @author zb.jiang
 * @version 1.0
 * @Date 16/6/4
 */
public class ExportExcelUtils {

    public static HSSFWorkbook export(List<OrderInfo> list) {

        String[] excelHeader = { "Sno", "Name", "Age"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Campaign");
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            OrderInfo orderInfo = list.get(i);
            row.createCell(0).setCellValue(orderInfo.getOrderNo());
            row.createCell(1).setCellValue(orderInfo.getSenderId());
            row.createCell(2).setCellValue(orderInfo.getCustomerId());
        }
        return wb;
    }

    public static HSSFWorkbook exportOrder(Map<String,Object> orderMap,List<OrderDetail> detailList)
    {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("订单");
        HSSFRow hssfRow = sheet.createRow(0);
        sheet.createRow(1);

        HSSFCellStyle style = wb.createCellStyle();
        // 设置居中样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中

        // 设置合计样式
        HSSFCellStyle style1 = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗体
        font.setFontHeightInPoints((short) 24);
        style1.setFont(font);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
        style1.setBorderTop((short) 1);
        style1.setBorderBottom((short) 1);
        style1.setBottomBorderColor(HSSFColor.BLACK.index);
        style1.setTopBorderColor(HSSFColor.BLACK.index);

        HSSFCellStyle style2 = wb.createCellStyle();
        Font font1 = wb.createFont();
        font1.setColor(HSSFColor.BLACK.index);
        font1.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗体
        font1.setUnderline((byte)1);
        style2.setFont(font1);

        sheet.addMergedRegion(new CellRangeAddress(0,1,0,13));
        HSSFCell hssfCell = hssfRow.createCell(0);
        hssfCell.setCellValue("业务订单");
        hssfCell.setCellStyle(style1);

        hssfRow = sheet.createRow(2);
        sheet.addMergedRegion(new CellRangeAddress(3,3,0,1));
        hssfCell = hssfRow.createCell(0);
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("订单编号:");

        hssfCell = hssfRow.createCell(2);
        sheet.addMergedRegion(new CellRangeAddress(3,3,2,3));
        hssfCell.setCellStyle(style2);
        hssfCell.setCellValue((String)orderMap.get("orderNo"));

        hssfCell = hssfRow.createCell(5);
        sheet.addMergedRegion(new CellRangeAddress(3,3,5,6));
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("操作人:");

        hssfCell = hssfRow.createCell(7);
        sheet.addMergedRegion(new CellRangeAddress(3,3,7,8));
        hssfCell.setCellStyle(style2);
        hssfCell.setCellValue((String)orderMap.get("userName"));

        hssfCell = hssfRow.createCell(10);
        sheet.addMergedRegion(new CellRangeAddress(3,3,10,11));
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("订单日期:");

        hssfCell = hssfRow.createCell(12);
        sheet.addMergedRegion(new CellRangeAddress(3,3,12,13));
        hssfCell.setCellStyle(style2);
        hssfCell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format((Date) orderMap.get("orderDate")));

        hssfRow = sheet.createRow(3);
        sheet.addMergedRegion(new CellRangeAddress(2,2,0,1));
        hssfCell = hssfRow.createCell(0);
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("客户:");

        hssfCell = hssfRow.createCell(2);
        sheet.addMergedRegion(new CellRangeAddress(2,2,2,3));
        hssfCell.setCellStyle(style2);
        hssfCell.setCellValue((String)orderMap.get("customerName"));

        hssfCell = hssfRow.createCell(5);
        sheet.addMergedRegion(new CellRangeAddress(2,2,5,6));
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("客户电话:");

        hssfCell = hssfRow.createCell(7);
        sheet.addMergedRegion(new CellRangeAddress(2,2,7,8));
        hssfCell.setCellStyle(style2);
        hssfCell.setCellValue((String)orderMap.get("customerPhone"));

        hssfCell = hssfRow.createCell(10);
        sheet.addMergedRegion(new CellRangeAddress(2,2,10,11));
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("送货日期:");

        hssfCell = hssfRow.createCell(12);
        sheet.addMergedRegion(new CellRangeAddress(2,2,12,13));
        hssfCell.setCellStyle(style2);
        hssfCell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format((Date) orderMap.get("senderDate")));

        hssfRow = sheet.createRow(4);
        sheet.addMergedRegion(new CellRangeAddress(4,4,0,1));
        hssfCell = hssfRow.createCell(0);
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("送货人:");

        hssfCell = hssfRow.createCell(2);
        sheet.addMergedRegion(new CellRangeAddress(4,4,2,3));
        hssfCell.setCellStyle(style2);
        hssfCell.setCellValue((String)orderMap.get("sendName"));

        hssfCell = hssfRow.createCell(5);
        sheet.addMergedRegion(new CellRangeAddress(4,4,5,6));
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("客户地址:");

        hssfCell = hssfRow.createCell(7);
        sheet.addMergedRegion(new CellRangeAddress(4,4,7,13));
        hssfCell.setCellStyle(style2);
        hssfCell.setCellValue((String)orderMap.get("customerAddress"));

        sheet.addMergedRegion(new CellRangeAddress(5,5,0,13));

        CellStyle cellStyle=wb.createCellStyle();
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN); // 底部边框
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 底部边框颜色
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);  // 左边边框
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边边框颜色
        cellStyle.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
        cellStyle.setBorderTop(CellStyle.BORDER_THIN); // 上边边框
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());  // 上边边框颜色
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中

        hssfRow = sheet.createRow(6);
        hssfCell = hssfRow.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(6,6,0,2));
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("品种");

        hssfCell = hssfRow.createCell(3);
        sheet.addMergedRegion(new CellRangeAddress(6,6,3,5));
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("数量");

        hssfCell = hssfRow.createCell(6);
        sheet.addMergedRegion(new CellRangeAddress(6,6,6,7));
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("单位");

        hssfCell = hssfRow.createCell(8);
        sheet.addMergedRegion(new CellRangeAddress(6,6,8,10));
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("价格(元)");

        hssfCell = hssfRow.createCell(11);
        sheet.addMergedRegion(new CellRangeAddress(6,6,11,13));
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("总价(元)");

        setCellBorder(hssfRow,cellStyle);

        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

        for (int i=0 ;i<detailList.size(); i++)
        {
            int _rowNum = 6 + (i+1);
            OrderDetail detail = detailList.get(i);
            hssfRow = sheet.createRow( _rowNum );
            hssfCell = hssfRow.createCell(0);
            sheet.addMergedRegion(new CellRangeAddress(_rowNum,_rowNum,0,2));
            hssfCell.setCellStyle(style);
            hssfCell.setCellValue(detail.getProductName());

            hssfCell = hssfRow.createCell(3);
            sheet.addMergedRegion(new CellRangeAddress(_rowNum,_rowNum,3,5));
            hssfCell.setCellStyle(style);
            hssfCell.setCellValue(detail.getAmount());

            hssfCell = hssfRow.createCell(6);
            sheet.addMergedRegion(new CellRangeAddress(_rowNum,_rowNum,6,7));
            hssfCell.setCellStyle(style);
            hssfCell.setCellValue(detail.getUnit());

            hssfCell = hssfRow.createCell(8);
            sheet.addMergedRegion(new CellRangeAddress(_rowNum,_rowNum,8,10));
            hssfCell.setCellStyle(style);
            hssfCell.setCellValue(decimalFormat.format(detail.getPrice()));

            hssfCell = hssfRow.createCell(11);
            sheet.addMergedRegion(new CellRangeAddress(_rowNum,_rowNum,11,13));
            hssfCell.setCellStyle(style);
            hssfCell.setCellValue(decimalFormat.format(detail.getTotalMoney()));

            setCellBorder(hssfRow,cellStyle);
        }

        hssfRow = sheet.createRow((6 + detailList.size() + 1));
        hssfCell = hssfRow.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress((6 + detailList.size() + 1),(6 + detailList.size() + 1),0,2));
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("合计：");

        Double orderMoney = (Double) orderMap.get("totalMoney");
        hssfCell = hssfRow.createCell(3);
        hssfCell.setCellValue("￥ " + decimalFormat.format(orderMoney));
        sheet.addMergedRegion(new CellRangeAddress((6 + detailList.size() + 1), (6 + detailList.size() + 1), 3, 13));
        setCellBorder(hssfRow,cellStyle);

        CellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 水平居中
        cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 居右
        cellStyle1.setBorderBottom(CellStyle.BORDER_THIN); // 底部边框
        cellStyle1.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 底部边框颜色
        hssfCell.setCellStyle(cellStyle1);

        sheet.addMergedRegion(new CellRangeAddress((6 + detailList.size() + 2),(6 + detailList.size() + 2),0,13));

        int lastRows = 6 + detailList.size() + 3;
        hssfRow = sheet.createRow(lastRows);

        hssfCell = hssfRow.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(lastRows,lastRows,0,1));
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("送货人签名:");
        sheet.addMergedRegion(new CellRangeAddress(lastRows,lastRows,2,3));

        hssfCell = hssfRow.createCell(10);
        sheet.addMergedRegion(new CellRangeAddress(lastRows,lastRows,10,11));
        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("收货人签名:");
        sheet.addMergedRegion(new CellRangeAddress(lastRows,lastRows,12,13));

        return wb;
    }

    /**
     * 设置边框
     * @param hssfRow
     * @param cellStyle
     */
    protected static void setCellBorder(HSSFRow hssfRow,CellStyle cellStyle)
    {
        for(int i=0; i < 14; i ++ )
        {
            HSSFCell detailCell = hssfRow.getCell(i);
            if(detailCell == null )
            {
                detailCell = hssfRow.createCell(i);
            }
            detailCell.setCellStyle(cellStyle);
        }
    }
}
