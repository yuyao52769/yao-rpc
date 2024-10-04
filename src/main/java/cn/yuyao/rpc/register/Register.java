package cn.yuyao.rpc.register;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author yuyao
 * @create 2024/10/4
 */
public interface Register {

    void init(String registerAddress, String env) throws NacosException;

    void registerInstance(Class<?> interClass, ServiceInstance serviceInstance) throws NacosException;

    List<Instance> getInstances(Class<?> interClass) throws NacosException;

}
