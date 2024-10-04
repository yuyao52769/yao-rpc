package cn.yuyao.rpc.server.transport;

import cn.yuyao.rpc.common.NetUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author yuyao
 * @create 2024/10/4
 */
public class ServerTransport {

    private ServerBootstrap bootstrap;
    private EventLoopGroup worker;

    private EventLoopGroup boss;

    private int workerThreads;


    public ServerTransport() {
        this(10);
    }

    public ServerTransport(int workerSize) {
        this.workerThreads = workerSize;
        bootstrap = new ServerBootstrap();
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup(workerThreads);
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new RpcServerProviderInitailizar());
        ChannelFuture channelFuture = bootstrap.bind(NetUtils.NETTY_PORT);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> stopServer()));
    }

    public void stopServer() {
        worker.shutdownGracefully();
        boss.shutdownGracefully();
    }
}
