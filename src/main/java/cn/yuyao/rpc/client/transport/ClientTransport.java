package cn.yuyao.rpc.client.transport;

import cn.yuyao.rpc.client.ClientRefAutoConfig;
import cn.yuyao.rpc.protocol.MethodInvokData;
import cn.yuyao.rpc.protocol.Result;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author yuyao
 * @create 2024/10/4
 */
public class ClientTransport implements Transport{


    private Bootstrap bootstrap;
    private EventLoopGroup worker;

    private int workerThreads;

    public ClientTransport() {
        this(1);
    }

    public ClientTransport(Integer workerThreads) {
        this.workerThreads = workerThreads;
        bootstrap = new Bootstrap();
        worker = new NioEventLoopGroup(workerThreads);
        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class);
    }


    @Override
    public Result invoke(JDKProxy.IpAndPort hostAndPort, MethodInvokData methodInvokData) throws Exception {
        RPCClientChannelInitializer initializer = new RPCClientChannelInitializer(methodInvokData);
        bootstrap.handler(initializer);
        ChannelFuture channelFuture = bootstrap.connect(hostAndPort.getIp(), hostAndPort.getPort()).sync();
        channelFuture.channel().closeFuture().sync();
        Result result = initializer.getResult();
        close();
        return result;
    }

    @Override
    public void close() {
        worker.shutdownGracefully();
    }
}
