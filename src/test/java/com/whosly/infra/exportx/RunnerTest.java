package com.whosly.infra.exportx;

import com.whosly.infra.exportx.configuration.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@Slf4j
public class RunnerTest extends BaseTest {
    @Autowired
    private SpringContextHolder springContextHolder;

    @Test
    public void test() {
        Assert.assertTrue(true);
        Assert.assertTrue(springContextHolder != null);

        ApplicationContext context = SpringContextHolder.get();
        Assert.assertTrue(context != null);

//        while (true){
//
//        }
    }
}
