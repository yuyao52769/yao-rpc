package cn.yuyao.rpc.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yuyao
 * @create 2024/10/4
 */
public abstract class AbstractCache<KEY, VALUE> implements Cache<KEY, VALUE>{

    private Class<VALUE> outClass;
    private Class<KEY> inClass;
    private LoadingCache<KEY, VALUE> cache;

    protected AbstractCache() {
        init(30, 1000);
    }

    private void init(long refreshSeconds,  int maxSize) {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        inClass = (Class<KEY>) genericSuperclass.getActualTypeArguments()[0];
        Type actualTypeArgument = genericSuperclass.getActualTypeArguments()[1];

        if (actualTypeArgument instanceof ParameterizedType) {
            ParameterizedType childType = (ParameterizedType) actualTypeArgument;
            Type[] actualTypeArguments = childType.getActualTypeArguments();
            outClass = (Class<VALUE>) childType.getRawType();
        } else {
           outClass = (Class<VALUE>) genericSuperclass.getActualTypeArguments()[1];
        }
        cache = Caffeine.newBuilder()
                .refreshAfterWrite(refreshSeconds, TimeUnit.SECONDS)
                .maximumSize(maxSize)
                .build(new CacheLoader<KEY, VALUE>() {

                    @Override
                    public @Nullable VALUE load(@NonNull KEY key) throws Exception {
                        return AbstractCache.this.load(key);
                    }
                });

    }

    abstract VALUE load(KEY key);

    @Override
    public VALUE getByKey(KEY key) {
        return cache.get(key);
    }

    @Override
    public List<VALUE> getByBatch(List<KEY> keys) {
        return null;
    }

    @Override
    public void delete(KEY req) {

    }
}
