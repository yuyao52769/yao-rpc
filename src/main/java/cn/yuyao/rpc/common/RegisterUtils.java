package cn.yuyao.rpc.common;

import cn.yuyao.rpc.register.NacosRegister;
import cn.yuyao.rpc.register.Register;
import cn.yuyao.rpc.register.ServiceInstance;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yuyao
 * @create 2024/10/4
 */
public class RegisterUtils {

    public static final Register REGISTER = new NacosRegister();

    static {
        try {
            REGISTER.init("localhost:8848", "dev");
        } catch (NacosException e) {
            System.out.println(e);
        }
    }

   public static void registerInstance(Class<?> interClass, ServiceInstance serviceInstance) throws NacosException {
       REGISTER.registerInstance(interClass, serviceInstance);
   }

   public static List<Instance> getInstances(Class<?> interClass) throws NacosException {
       return REGISTER.getInstances(interClass);
   }

}
