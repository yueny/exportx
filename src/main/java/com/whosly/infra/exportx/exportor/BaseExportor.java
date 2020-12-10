package com.whosly.infra.exportx.exportor;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.whosly.infra.exportx.enums.ExportType;
import com.whosly.infra.exportx.exportor.wrapper.BaseWrapper;
import com.whosly.infra.exportx.metadata.config.IExportConfig;
import com.whosly.infra.exportx.metadata.config.ServerConfigurable;
import com.whosly.infra.exportx.metadata.exportor.DataHeads;
import lombok.Getter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.util.StopWatch;

import java.util.Map;


/**
 * Exportor 抽象模板
 *
 * @param <O>
 * @param <T>
 */
abstract class BaseExportor<O, T> implements IExportor<O, T>, ServerConfigurable {
    @Getter
    private O writer;

    @Getter
    private T target;

    @Getter
    private IExportConfig exportConfig;

    public BaseExportor(IExportConfig exportConfig, StopWatch watch) {
        this.exportConfig = exportConfig;

        init(watch);
    }

    protected void init(StopWatch watch) {
        watch.start(getClass().getName() + " getDataHeads");
        Map.Entry<DataHeads, DataHeads> dataHeads = BaseWrapper.getDataHeads(exportConfig);

        DataHeads allDataHeads = dataHeads.getKey();
        DataHeads includeDataHeads = dataHeads.getValue();
        watch.stop();

        watch.start(getClass().getName() + " buildWriter");
        O writer = buildWriter(exportConfig, allDataHeads, includeDataHeads, null);
        watch.stop();

        this.writer = writer;

        watch.start(getClass().getName() + " buildTarget");
        T target = buildTarget(exportConfig);
        watch.stop();

        this.target = target;
    }

    @Override
    public ExportType getExportType() {
        return getExportConfig().getExportType();
    }

    /**
     * 样式
     *
     * @return
     */
    protected HorizontalCellStyleStrategy horizontalCellStyleStrategy() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());

        // 单元格策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);

        // 字体大小 及 字体样式 add by  zhuli2-ac@aicaigroup.com
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 10);
        contentWriteFont.setFontName("宋体");
        contentWriteCellStyle.setWriteFont(contentWriteFont);

        // 初始化表格样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle,
                contentWriteCellStyle);

        return horizontalCellStyleStrategy;
    }

}
