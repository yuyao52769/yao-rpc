package cn.yuyao.rpc.server.anno;

import org.springframework.context.annotation.Bean;

/**
 * @author yuyao
 * @create 2024/10/4
 */
public class ProviderConfig {


    @Bean
    private MyApplicationRefreshListener myApplicationRefreshListener() {
        return new MyApplicationRefreshListener();
    }


}


