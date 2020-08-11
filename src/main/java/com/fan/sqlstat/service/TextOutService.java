package com.fan.sqlstat.service;

import com.fan.sqlstat.model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class TextOutService extends AbstractOutService implements OutService {
    private static final Logger logger = LoggerFactory.getLogger(TextOutService.class);

    @Resource(name="combinedRuleMap")
    Map<String, Map<Integer, Rule>> combinedRuleMap;

    @Value("${app.projectStat.showDetails:false}")
    private boolean showDetails;

    @Value("${app.projectStat.output.excel:true}")
    private boolean isOutputExcel;

    @Override
    public void outputAction(ResultSet resultSet) {
        Map<String, ProjectStat> resultMap = resultSet.getResultMap();
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++");
        resultMap.forEach((key, projectStat) -> {
            logger.info("{}", projectStat.toString());
            if(showDetails){
                projectStat.fileTargetList.forEach(fileTarget -> {
                    logger.info("{}", "file:"+fileTarget.getFile().getAbsolutePath());
                    fileTarget.getSqlHitList().forEach(sqlHit -> {
                        StringBuilder stringBuilder = new StringBuilder();
                        String ruleMapName = sqlHit.getRuleMapName();
                        int ruleId = sqlHit.getRuleId();
                        Map<Integer,Rule> ruleMap = combinedRuleMap.get(ruleMapName);
                        Rule rule = ruleMap.get(ruleId);
                        stringBuilder.append(" [ruleId:").append(ruleId)
                                .append(" Regex:").append("\"").append(rule.getRegex()).append("\"")
                                .append(" Suggestion:").append(rule.getSuggrestion())
                                .append(" originalSql:").append(sqlHit.getOriginalSql() == null ? null : sqlHit.getOriginalSql().trim())
                                .append("]");
                        logger.info("{}", stringBuilder.toString());
                    });
                });
            }
        });
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++");
        if(isOutputExcel){
            outputExcel(resultSet);
        }
    }

    private void outputExcel(ResultSet resultSet){
        List<String> header = Arrays.asList("file", "ruleId", "regex", "Suggestion", "originalSql");
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        sheet.setDefaultRowHeight((short) 400);
        // 构建头单元格样式
        CellStyle cellStyle = buildHeadCellStyle(sheet.getWorkbook());
        // 写入第一行各列的数据

        int rowNum = 0;
        Row head = sheet.createRow(rowNum++);
        for (int i = 0; i < header.size(); i++) {
            Cell cell = head.createCell(i);
            cell.setCellValue(header.get(i));
            sheet.setColumnWidth(i, 4000);
            cell.setCellStyle(cellStyle);
        }

        Map<String, ProjectStat> resultMap = resultSet.getResultMap();

        Set<String> projectNameSet = resultMap.keySet();
        for(String projectName : projectNameSet){
            ProjectStat projectStat = resultMap.get(projectName);
            for(FileTarget fileTarget : projectStat.fileTargetList){
                try {
                    String fileName = fileTarget.getFile().getCanonicalPath();
                    for(SqlHit sqlHit : fileTarget.getSqlHitList()){
                        String ruleMapName = sqlHit.getRuleMapName();
                        int ruleId = sqlHit.getRuleId();
                        Map<Integer,Rule> ruleMap = combinedRuleMap.get(ruleMapName);
                        Rule rule = ruleMap.get(ruleId);
                        Row row = sheet.createRow(rowNum++);
                        int cellNum = 0;
                        row.createCell(cellNum++).setCellValue(fileName);
                        row.createCell(cellNum++).setCellValue(ruleId);
                        row.createCell(cellNum++).setCellValue(rule.getRegex());
                        row.createCell(cellNum++).setCellValue(rule.getSuggrestion());
                        row.createCell(cellNum++).setCellValue(sqlHit.getOriginalSql() == null ? null : sqlHit.getOriginalSql().trim());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        FileOutputStream fileOut = null;
        try {
            String exportFilePath = getDateDir()+"output.xlsx";
            File exportFile = new File(exportFilePath);
            if (!exportFile.exists()) {
                exportFile.createNewFile();
            }
            fileOut = new FileOutputStream(exportFilePath);
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOut) {
                    fileOut.close();
                }
                if (null != workbook) {
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static CellStyle buildHeadCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        //对齐方式设置
        style.setAlignment(HorizontalAlignment.CENTER);
        //边框颜色和宽度设置
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 下边框
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边框
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex()); // 右边框
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上边框
        //设置背景颜色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //粗体字设置
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
