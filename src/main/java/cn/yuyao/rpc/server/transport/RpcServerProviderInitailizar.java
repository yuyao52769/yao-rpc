package cn.yuyao.rpc.server.transport;

import cn.yuyao.rpc.codec.RPCMessageToMessageCodec;
import cn.yuyao.rpc.protocol.MethodInvokData;
import cn.yuyao.rpc.protocol.Result;
import cn.yuyao.rpc.server.anno.ProviderRespority;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;


/**
 * @author yuyao
 * @create 2024/10/4
 */
@Slf4j
public class RpcServerProviderInitailizar extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //1. 封祯 LengthFieldBaseFrameDecoder eventLoopGroupHandler
        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 10, 4, 0, 0));
        //2. LoggingHandler                  eventLoopGroupHandler
        pipeline.addLast(new LoggingHandler());
        //3. 编解码 RPCMessageToMessageCodec  eventLoopGroupService
        pipeline.addLast(new RPCMessageToMessageCodec());
        pipeline.addLast(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                MethodInvokData methodInvokData = (MethodInvokData) msg;
                Result result = executeTargetObject(methodInvokData);
                ChannelFuture channelFuture = ctx.writeAndFlush(result);
                //关闭连接
                channelFuture.addListener(ChannelFutureListener.CLOSE);
                channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        });
    }

    public Result executeTargetObject(MethodInvokData methodInvokData) throws NoSuchMethodException {
        Class targetInterface = methodInvokData.getTargetInterface();
        Object instance = ProviderRespority.getInstance(targetInterface);

        Method method = targetInterface.getDeclaredMethod(methodInvokData.getMethodName(), methodInvokData.getParameterTypes());

        //进行方法的调用
        Result result = new Result();
        try {
            Object ret = method.invoke(instance, methodInvokData.getArgs());
            log.debug("method invoke returnValue is {} ", ret);
            result.setResultValue(ret);
        } catch (Exception e) {
            log.error("methond invoke error", e);
            result.setException(e);
        }
        return result;
    }
}
