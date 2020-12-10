package com.whosly.infra.exportx.metadata.exportor;

import com.whosly.rapid.lang.mask.instance.AbstractMaskBo;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class DataHeads extends AbstractMaskBo {
    private List<DataHead> dataHeads;

    /**
     * 将数据列表转为 哈希
     */
    public Map.Entry<List<String>, List<String>> headMap(){
        if(CollectionUtils.isEmpty(dataHeads)){
            return new AbstractMap.SimpleEntry(null, null);
        }

        List<String> colunms = dataHeads.stream()
                .map(dataHead -> dataHead.getColumn())
                .collect(Collectors.toList());
        List<String> alias = dataHeads.stream()
                .map(dataHead -> dataHead.getAlias())
                .collect(Collectors.toList());

        return new AbstractMap.SimpleEntry(colunms, alias);
    }
}
