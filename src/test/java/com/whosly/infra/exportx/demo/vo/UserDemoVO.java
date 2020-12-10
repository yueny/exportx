package com.whosly.infra.exportx.demo.vo;

import com.whosly.infra.exportx.metadata.api.IDataModel;
import lombok.*;

import java.util.Date;

/**
 * 数个字段顺序按照 vo 定义的顺序输出
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDemoVO implements IDataModel {
    /**
     * 姓名
     */
    private String name;

    private Long age;

    private Long height;

    private Long hai;

    private Date bir;

}
