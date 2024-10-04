package cn.yuyao.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MethodInvokData extends AbstractProtocol {

    private Integer type = 1;

    private Class targetInterface;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] args;

    @Override
    public Integer type() {
        return this.type;
    }
}
