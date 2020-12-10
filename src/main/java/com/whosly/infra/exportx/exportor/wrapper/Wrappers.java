package com.whosly.infra.exportx.exportor.wrapper;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.whosly.infra.exportx.exportor.IExportor;

/**
 * 每次  new, new init， muti instance
 */
public final class Wrappers extends BaseWrapper<ExcelWriter, WriteSheet> {
    public Wrappers(IExportor exportor) {
        super(exportor);
    }

}
