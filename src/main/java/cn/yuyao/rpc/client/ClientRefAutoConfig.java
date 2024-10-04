package cn.yuyao.rpc.client;


import cn.yuyao.rpc.client.anno.MyServiceRef;
import cn.yuyao.rpc.client.transport.ClientTransport;
import cn.yuyao.rpc.client.transport.JDKProxy;
import cn.yuyao.rpc.client.transport.Transport;
import cn.yuyao.rpc.common.RegisterUtils;
import cn.yuyao.rpc.protocol.MethodInvokData;
import cn.yuyao.rpc.protocol.Result;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author yuyao
 * @create 2024/10/4
 */

@Slf4j
public class ClientRefAutoConfig implements BeanPostProcessor {


    private static final Map<String, Object> PROXY_MAP = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> aClass = bean.getClass();
        ReflectionUtils.doWithFields(aClass, f -> {
            if (f.getAnnotation(MyServiceRef.class) != null) {
                Class<?> interCls = f.getType();
                Object proxy = createProxy(interCls);
                if (proxy != null) {
                    f.setAccessible(true);
                    ReflectionUtils.setField(f, bean, proxy);
                }
            }
        });
        return bean;
    }


    public <T> T createProxy(Class<T> interCls) {
        if (PROXY_MAP.get(interCls.getName()) == null) {
            Object o = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{interCls}, new JDKProxy(interCls));
            return (T) o;
        } else {
            return (T) PROXY_MAP.get(interCls.getName());
        }

    }

}
