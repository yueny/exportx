package com.whosly.infra.exportx.metadata.config;

import com.whosly.infra.exportx.enums.ExportType;
import com.whosly.infra.exportx.metadata.api.IDataModel;
import com.whosly.rapid.lang.mask.instance.AbstractMaskBo;
import lombok.*;

@Getter
@Builder
public class ExportConfig extends AbstractMaskBo implements IExportConfig {
    /**
     * 导出的文件名称所处的路径，默认 /tmp
     */
    @Builder.Default
    private final String outputPath = "/tmp";

    @NonNull
    private final String fileName;

    /**
     * 导出数据的数据类型 bean, 如  UserVO.class
     */
    @NonNull
    private final Class<? extends IDataModel> clazz;

    /**
     * 导出类型。默认 csv
     */
    @Setter
    @Builder.Default
    private ExportType exportType = ExportType.EXCEL2007;

    /**
     * 当前允许的导出最大数量， 默认值2W。当大于全局限制的时候，以全局限制为准； 当小于全局限制时， 以本配置为准。
     *
     * 当配置不存在时，以全局限制为准。
     */
    @Getter
    @Setter
    @Builder.Default
    private Long limit = 20000L;

    // 未实现
//    /**
//     * 导出头。 可自定义设置。默认取 clazz 的 field name list。 正序排序.
//     *
//     * 如果设置了 columns， 则必须设置 heads， 且heads与 columns 长度保持一致。此时注解@Exportx的 alias无效， sort 有效。
//     * 暂不支持自动计划规划
//     */
//    private final List<String> columns;
//
//    /**
//     * 导出头。可自定义设置。默认 为 columns。
//     *
//     * 如果设置了 heads， 则必须设置 columns， 且heads与 columns 长度保持一致。此时注解@Exportx的 alias无效， sort 有效。
//     * 暂不支持自动计划规划
//     */
//    private final List<String> heads;
}
