package cn.yuyao.rpc.client.transport;

import cn.yuyao.rpc.client.ClientRefAutoConfig;
import cn.yuyao.rpc.protocol.MethodInvokData;
import cn.yuyao.rpc.protocol.Result;

public interface Transport {

    public Result invoke(JDKProxy.IpAndPort hostAndPort, MethodInvokData methodInvokData) throws Exception;

    public void close();
}
