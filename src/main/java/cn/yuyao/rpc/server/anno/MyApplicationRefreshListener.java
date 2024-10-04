package cn.yuyao.rpc.server.anno;

import cn.hutool.core.collection.CollectionUtil;
import cn.yuyao.rpc.common.NetUtils;
import cn.yuyao.rpc.common.RegisterUtils;
import cn.yuyao.rpc.register.ServiceInstance;
import cn.yuyao.rpc.server.transport.ServerTransport;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Map;

/**
 * @author yuyao
 * @create 2024/10/4
 */

public class MyApplicationRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        AbstractApplicationContext ap = (AbstractApplicationContext) applicationContext;
        ConfigurableListableBeanFactory beanFactory = ap.getBeanFactory();
        String[] serviceNames = applicationContext.getBeanNamesForAnnotation(MyServicePro.class);
        for (int i = 0; i < serviceNames.length; i++) {
            Object bean = applicationContext.getBean(serviceNames[i]);
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(serviceNames[i]);
            String beanClassName = beanDefinition.getBeanClassName();

            try {
                Class<?> aClass = Class.forName(beanClassName);
                Class<?> interCls = aClass.getInterfaces()[0];
                ProviderRespority.addInstance(interCls, bean);
            } catch (Exception e) {

            }
        }
        try {
            register();
            openNetty();
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }


    private void register() throws NacosException {
        if (ProviderRespority.hasInstance()) {
            for (Map.Entry<Class, Object> entry : ProviderRespority.getTotalMap().entrySet()) {
                Class interCls = entry.getKey();
                ServiceInstance serviceInstance = new ServiceInstance();
                serviceInstance.setIp(NetUtils.getLocalIp());
                serviceInstance.setPort(NetUtils.NETTY_PORT);
                serviceInstance.setServiceInstanceId(interCls.getName());
                RegisterUtils.registerInstance(interCls, serviceInstance);
            }
        }
    }

    private void openNetty() {
        ServerTransport serverTransport = new ServerTransport();
    }
}
