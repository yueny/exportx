package com.whosly.infra.exportx.demo.config;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableApolloConfig(value = {"application","FX.redis.third", "server"})
public class ApolloConfigTest {
    //.
}
