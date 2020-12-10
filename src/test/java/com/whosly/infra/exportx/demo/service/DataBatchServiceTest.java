package com.whosly.infra.exportx.demo.service;

import com.whosly.infra.exportx.metadata.api.IDataModel;
import com.whosly.infra.exportx.query.ExportxQueryContext;
import com.whosly.infra.exportx.query.api.IConditions;
import com.whosly.infra.exportx.query.api.IDataBatchService;
import com.whosly.infra.exportx.demo.vo.TagAssetLogQueryTestVO;
import com.whosly.infra.exportx.demo.vo.UserDemoVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DataBatchServiceTest implements IDataBatchService<TagAssetLogQueryTestVO> {

    @Override
    public Page<? extends IDataModel> fetchData(ExportxQueryContext context, IConditions<TagAssetLogQueryTestVO> conditions) {
        // 该逻辑仅仅为模拟数据库查询，仅仅为了正确的返回 Page， 请勿带入任何逻辑思考。
        // 该模拟查询请无视


        // 模拟数据查询，假定数据库中只有 16666条数据
        Long total = 16666L;

        Integer queryBatch = context.getPageSize();
        if(queryBatch > total.longValue()){
            queryBatch = total.intValue();
        }

        if(conditions != null){
            // 获取自定义查询条件
            TagAssetLogQueryTestVO condition = conditions.getCondition();

            //
            condition.setPage(context.getPageNo().longValue());
            condition.setRows(context.getPageSize().longValue());

            System.out.println(condition);

            // 带自定义条件的dao查询，此处不实现，纯属模拟
            //.
        }


        // 模拟查询到的数据库数据
        List<UserDemoVO> list = new ArrayList<>();
        for(int i = 0; i< queryBatch; i++){
            UserDemoVO vo = UserDemoVO.builder()
                    .name("姓名张" + i)
                    .age(new Long(i))
                    .bir(new Date())
                    .height(i * 1000L)
                    .build();

            list.add(vo);
        }

        // 模拟和构造 Page
        Page<UserDemoVO> page = new PageImpl<UserDemoVO>(list, PageRequest.of(context.getPageNo(), context.getPageSize()), total);

        // 返回查询到的分页结果
        return page;
    }
}
