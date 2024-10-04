package cn.yuyao.rpc.cache;

import java.util.List;

/**
 * @author yuyao
 * @create 2024/10/4
 */
public class CacheTest {

    public static void main(String[] args) {
        RegisterCache cache = new RegisterCache();
        System.out.println(cache.getByKey("sss"));
    }
}
