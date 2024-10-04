package cn.yuyao.rpc.cache;

import java.util.List;

/**
 * @author yuyao
 * @create 2024/10/4
 */
public interface Cache<KEY, VALUE> {

    VALUE getByKey(KEY key);

    List<VALUE> getByBatch(List<KEY> keys);

    void delete(KEY req);
}
