package cn.yuyao.rpc.client.transport;

import cn.yuyao.rpc.codec.RPCMessageToMessageCodec;
import cn.yuyao.rpc.protocol.MethodInvokData;
import cn.yuyao.rpc.protocol.Result;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yuyao
 * @create 2024/10/4
 */
@Slf4j
@Data
public class RPCClientChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    private MethodInvokData methodInvokData;

    private Result result;

    public RPCClientChannelInitializer(MethodInvokData methodInvokData) {
        this.methodInvokData = methodInvokData;
    }

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 10, 4, 0, 0));
        pipeline.addLast(new LoggingHandler());
        pipeline.addLast(new RPCMessageToMessageCodec());
        pipeline.addLast(new ChannelInboundHandlerAdapter() {
            //client --->server 发请求 MethodInvokData
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                log.debug("发送数据...{} ", methodInvokData);
                ChannelFuture channelFuture = ctx.writeAndFlush(methodInvokData);
                channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }

            //server ---> client Result
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("接受到了服务端响应的数据 ....{} ", msg);
                result = (Result) msg;
            }
        });
    }
}
