package cn.yuyao.rpc.server.anno;

import cn.yuyao.rpc.client.ClientRefAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yuyao
 * @create 2024/10/4
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(value = {ProviderConfig.class})
public @interface EnableServer {
}
