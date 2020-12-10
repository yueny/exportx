package com.whosly.infra.exportx.demo.vo;

import com.whosly.infra.exportx.metadata.api.IDataModel;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDemoVO3 implements IDataModel {
    /**
     * 姓名
     */
    private String name;

    private Long age;

    private Long height;

    private Long hai;

    private Date bir;

}
