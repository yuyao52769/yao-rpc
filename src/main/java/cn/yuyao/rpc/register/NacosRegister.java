package cn.yuyao.rpc.register;

import cn.hutool.core.collection.CollectionUtil;
import cn.yuyao.rpc.constants.GlobalConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingMaintainFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yuyao
 * @create 2024/10/4
 */

@Slf4j
public class NacosRegister implements Register{

    //注册地址
    private String registerAddress;
    // 环境
    private String env;
    // 主要用于维护服务实例信息
    private NamingService namingService;

    private ConcurrentHashMap<String, List<Instance>> INSTANCE_DEPOSITORY = new ConcurrentHashMap<>();


    @Override
    public void init(String registerAddress, String env) throws NacosException {
        this.registerAddress = registerAddress;
        this.env = env;
        Properties properties = new Properties();
        properties.put("serverAddr", registerAddress);
        properties.put("username", "nacos");
        properties.put("password", "nacos");
        this.namingService = NamingFactory.createNamingService(properties);
    }

    @Override
    public void registerInstance(Class<?> interClass, ServiceInstance serviceInstance) throws NacosException {
        Instance nacosInstance = new Instance();
        nacosInstance.setInstanceId(serviceInstance.getServiceInstanceId());
        nacosInstance.setPort(serviceInstance.getPort());
        nacosInstance.setIp(serviceInstance.getIp());
        nacosInstance.setMetadata(ImmutableMap.of(GlobalConstants.META_DATA_KEY, JSON.toJSONString(serviceInstance)));

        namingService.registerInstance(interClass.getName(), env,nacosInstance);
        log.info("register {} ||| {}",interClass.getName(),serviceInstance);
        List<Instance> orDefault = INSTANCE_DEPOSITORY.getOrDefault(interClass.getName(), new ArrayList<Instance>());
        orDefault.add(nacosInstance);
        INSTANCE_DEPOSITORY.put(interClass.getName(), orDefault);
    }

    @Override
    public List<Instance> getInstances(Class<?> interClass) throws NacosException {
        List<Instance> allInstances = namingService.getAllInstances(interClass.getName(), env);
        return allInstances;
//        List<Instance> instances = INSTANCE_DEPOSITORY.get(interClass.getName());
//        if (CollectionUtil.isEmpty(instances)) {
//
//            INSTANCE_DEPOSITORY.put(interClass.getName(), allInstances);
//            return allInstances;
//        } else {
//           return instances;
//        }
    }

}
