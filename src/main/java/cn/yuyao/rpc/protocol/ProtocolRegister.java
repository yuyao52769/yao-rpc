package cn.yuyao.rpc.protocol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author yuyao
 * @create 2024/10/4
 */
public class ProtocolRegister {

    private static final Map<Integer, Class> MAP = new HashMap<>();

    static {
        ServiceLoader<Protocol> serviceLoader = ServiceLoader.load(Protocol.class);
        Iterator<Protocol> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            Protocol next = iterator.next();
            register(next.type(), next.getClass());
        }
    }

    public static void register(Integer type, Class cls) {
        MAP.put(type, cls);
    }

    public static Class getCla(Integer type) {
        return MAP.get(type);
    }
}
