package com.whosly.infra.exportx.exportor;

import com.whosly.infra.exportx.BaseTest;
import com.whosly.infra.exportx.metadata.config.ExportConfig;
import com.whosly.infra.exportx.Exportors;
import com.whosly.infra.exportx.configuration.SpringContextHolder;
import com.whosly.infra.exportx.exception.ExportxException;
import com.whosly.infra.exportx.query.api.IConditions;
import com.whosly.infra.exportx.query.api.IDataBatchService;
import com.whosly.infra.exportx.demo.service.DemoDataServiceTest;
import com.whosly.infra.exportx.demo.vo.TagAssetLogQueryTestVO;
import com.whosly.infra.exportx.demo.vo.UserDemoVO2;
import com.whosly.infra.exportx.util.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.*;

public class ExcelExportorTest  extends BaseTest {
    @Autowired
    private SpringContextHolder springContextHolder;
    @Autowired
    private DemoDataServiceTest demoDataService;
    @Autowired
    private IDataBatchService dataBatchService;

    /**
     * 无自定义查询条件
     * @throws ExportxException
     */
    @Test
    public void testFile() throws ExportxException {
        Assert.assertTrue(springContextHolder != null);

        ApplicationContext context = SpringContextHolder.get();
        Assert.assertTrue(context != null);

        ExportConfig exportConfig = ExportConfig.builder()
//                .clazz(UserDemoVO.class)
                .clazz(UserDemoVO2.class)
//                .clazz(UserDemoVO3.class)
                .outputPath("/Users/xiaobai")
                .fileName("aaa.xlsx")
//                .limit(99L)
                .build();

        File file = Exportors.export(demoDataService, exportConfig);
        Assert.assertTrue(file != null);
        // true if the file path is a file, else false
        Assert.assertTrue(file.exists());
        Assert.assertTrue(file.isFile());

        // returns the length in bytes
        long length = file.length();
        Assert.assertTrue(length >  0);

        System.out.println("文件大小： " + FileUtils.getNetFileSizeDescription(length));
    }

    /**
     * 自定义查询条件
     * @throws ExportxException
     */
    @Test
    public void testFileWithCondition() throws ExportxException {
        Assert.assertTrue(springContextHolder != null);

        ApplicationContext context = SpringContextHolder.get();
        Assert.assertTrue(context != null);

        ExportConfig exportConfig = ExportConfig.builder()
//                .clazz(UserDemoVO.class)
                .clazz(UserDemoVO2.class)
//                .clazz(UserDemoVO3.class)
                .outputPath("/Users/xiaobai")
                .fileName("aaa.xlsx")
//                .limit(99L)
                .build();

        File file = Exportors.export(dataBatchService, exportConfig, new IConditions<TagAssetLogQueryTestVO>() {
            @Override
            public TagAssetLogQueryTestVO getCondition() {
                TagAssetLogQueryTestVO tempvo = new TagAssetLogQueryTestVO();
                tempvo.setRentCode("test");
                tempvo.setTagKey("test");

                return tempvo;
            }
        });

        Assert.assertTrue(file != null);
        // true if the file path is a file, else false
        Assert.assertTrue(file.exists());
        Assert.assertTrue(file.isFile());

        // returns the length in bytes
        long length = file.length();
        Assert.assertTrue(length >  0);

        System.out.println("文件大小： " + FileUtils.getNetFileSizeDescription(length));
    }
//
//    @Test
//    public void testOutput() throws Exception {
//        ExportConfig exportConfig = ExportConfig.builder()
//                .clazz(UserDemoVO2.class)
//                .outputPath("/Users/xiaobai")
//                .fileName("bbbbbb.xlsx")
//                .build();
//
//        OutputStream outputStream = Exportors.exportOutputStream(dataBatchService, exportConfig);
//        Assert.assertTrue(outputStream != null);
//
////        ByteArrayInputStream is = new ByteArrayInputStream();
////        IOUtils.copy(in, out);
//    }

    @Test
    public void testByte() throws Exception {
        ExportConfig exportConfig = ExportConfig.builder()
                .clazz(UserDemoVO2.class)
                .outputPath("/Users/xiaobai")
                .fileName("bbbbbb.xlsx")
                .build();

        byte[] bytes = Exportors.exportByte(dataBatchService, exportConfig);
        Assert.assertTrue(bytes != null);
        Assert.assertTrue(bytes.length > 0);

        System.out.println("bytes 大小： " + FileUtils.getNetFileSizeDescription(bytes.length));

        // 将byte数组写入文件，验证准确度
        FileOutputStream fos = new FileOutputStream("/Users/xiaobai/temp.xlsx");
        fos.write(bytes);
        fos.close();
    }

}
