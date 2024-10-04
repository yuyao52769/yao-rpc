package cn.yuyao.rpc.client.transport;

import cn.yuyao.rpc.common.RegisterUtils;
import cn.yuyao.rpc.protocol.MethodInvokData;
import cn.yuyao.rpc.protocol.Result;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuyao
 * @create 2024/10/4
 */
public class JDKProxy implements InvocationHandler {

    private Class<?> aClass;

    private Boolean init = false;

    private InstanceDefine instanceDefine;

    public JDKProxy(Class<?> cls) {
        this.aClass = cls;
        init();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        if (name.equals("toString") || name.equals("hashCode") || name.equals("equals")) {
            return method.invoke(proxy, args);
        }
        Transport transport = new ClientTransport();
        MethodInvokData invokData = new MethodInvokData();
        invokData.setParameterTypes(method.getParameterTypes());
        invokData.setTargetInterface(this.aClass);
        invokData.setArgs(args);
        invokData.setMethodName(method.getName());
        Result invoke = transport.invoke(instanceDefine.getIpAndPorts().get(0), invokData);
        if (invoke.getException() != null) {
            throw invoke.getException();
        }
        return invoke.getResultValue();
    }

    private void init() {
        if (!init) {
            instanceDefine = new InstanceDefine();
            instanceDefine.setClsName(this.aClass.getName());
            try {
                List<Instance> instances = RegisterUtils.getInstances(this.aClass);
                List<IpAndPort> ipAndPorts = instances.stream().map(o -> {
                    IpAndPort ipAndPort = new IpAndPort();
                    ipAndPort.setIp(o.getIp());
                    ipAndPort.setPort(o.getPort());
                    return ipAndPort;
                }).collect(Collectors.toList());
                instanceDefine.setIpAndPorts(ipAndPorts);
            } catch (NacosException e) {
                System.out.println(e);
            } finally {
                init = true;
            }
        }
    }

    @Data
    public static class InstanceDefine {
        private String clsName;

        private List<IpAndPort> ipAndPorts;
    }

    @Data
    public static class IpAndPort {
        private String ip;

        private Integer port;
    }
}
