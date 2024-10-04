package cn.yuyao.rpc.cache;

import java.util.Arrays;
import java.util.List;

/**
 * @author yuyao
 * @create 2024/10/4
 */
public class RegisterCache extends AbstractCache<String, List<Integer>>{
    @Override
    List<Integer> load(String s) {
       return Arrays.asList(10, 20, 30);
    }
}
