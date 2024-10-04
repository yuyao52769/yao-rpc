package cn.yuyao.rpc.server.anno;

import cn.hutool.core.collection.CollectionUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yuyao
 * @create 2024/10/4
 */
public class ProviderRespority {

    private static final ConcurrentHashMap<Class, Object> MAP = new ConcurrentHashMap<>();

    public static Object getInstance(Class cls) {
        return MAP.get(cls);
    }

    public static Object getInstance(String clsName) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(clsName);
        return MAP.get(aClass);
    }

    public static void addInstance(Class cls, Object bean) {
        MAP.put(cls, bean);
    }

    public static boolean hasInstance() {
        return CollectionUtil.isNotEmpty(MAP);
    }

    public static ConcurrentHashMap<Class, Object> getTotalMap() {
        return MAP;
    }
}
