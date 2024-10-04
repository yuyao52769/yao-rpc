package cn.yuyao.rpc.protocol;

import java.io.Serializable;

public interface Protocol extends Serializable {
    String MAGIC_NUM = "myrpc";
    byte PROTOCOL_VERSION = 1;

    Integer type();
}
