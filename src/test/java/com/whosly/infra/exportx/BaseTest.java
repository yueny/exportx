package com.whosly.infra.exportx;

import com.whosly.infra.exportx.app.ApplicationTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@ActiveProfiles("dev")
//数据库中测试数据回滚
@Rollback(true)
public abstract class BaseTest {
}
