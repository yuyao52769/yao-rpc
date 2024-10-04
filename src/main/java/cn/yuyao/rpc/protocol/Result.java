package cn.yuyao.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Result extends AbstractProtocol {
    private Object resultValue;
    private Exception exception;
    private Integer type = 2;

    @Override
    public Integer type() {
        return this.type;
    }
}
