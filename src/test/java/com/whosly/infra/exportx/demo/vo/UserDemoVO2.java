package com.whosly.infra.exportx.demo.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.whosly.infra.exportx.metadata.api.IDataModel;
import lombok.*;

import java.util.Date;

/**
 * 自定义字段输出顺序
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDemoVO2 implements IDataModel {
    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名", index = 1)
    private String name;

    @ExcelProperty(value = "age", index = 2)
    private Long age;

    @ExcelProperty(value = "身高", index = 3)
    private Long height;

    @ExcelProperty(value = "嗨", index = 0)
    private Long hai;

    @ExcelProperty(value = "生日", index = 4)
    private Date bir;

}
