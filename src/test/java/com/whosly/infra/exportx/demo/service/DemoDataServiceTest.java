package com.whosly.infra.exportx.demo.service;

import com.whosly.infra.exportx.query.api.IConditions;
import com.whosly.infra.exportx.query.api.IDataOnceService;
import com.whosly.infra.exportx.metadata.api.IDataModel;
import com.whosly.infra.exportx.demo.vo.UserDemoVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 没有查询条件的单词查询导出
 */
@Service
public class DemoDataServiceTest implements IDataOnceService<Void> {
    @Override
    public List<? extends IDataModel> getData(IConditions<Void> conditions) {
        List<UserDemoVO> list = new ArrayList<>();

        // 没有输入查询条件，此时 conditions is null

        for(int i = 0; i< 100; i++){
            UserDemoVO vo = UserDemoVO.builder()
                    .name("姓名张" + i)
                    .age(new Long(i))
                    .bir(new Date())
                    .height(i * 1000L)
                    .build();

            list.add(vo);
        }

        return list;
    }
}
