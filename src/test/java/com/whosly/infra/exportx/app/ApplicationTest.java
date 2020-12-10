package com.whosly.infra.exportx.app;

import com.whosly.infra.exportx.annotation.EnableExports;
import com.whosly.infra.exportx.bean.ExportSpringAtomticConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages= {"com.whosly.infra.exportx.demo"},
        exclude = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class}) // 不扫描
@EnableExports(applicationName="demoProject")
@Slf4j
public class ApplicationTest {

    /**
     * @param args
     */
    public static void main( String[] args ) {
        ApplicationContext context = SpringApplication.run(ApplicationTest.class, args);

//        String serverPort = context.getEnvironment().getProperty("server.port");
//
//        log.info("Application started at http://localhost:{}.", serverPort);
    }

//    // 自定义  GlobalConfigure
//    @Bean
//    public GlobalConfigure globalConfigure(){
//        return GlobalConfigure.builder()
//                .exportQuerySize(15)
////                .limit(100L)
//                .debug(true)
//                .build();
//    }

    // 自定义  ExportSpringAtomticConfigure
    @Bean
    public ExportSpringAtomticConfigure exportSpringAtomticConfigure(){
        ExportSpringAtomticConfigure configure = new ExportSpringAtomticConfigure();
        configure.setNamespace("application");

        return configure;
    }

}
