package jit.wxs.dv.handler;

import jit.wxs.dv.domain.enums.ResultEnum;
import lombok.Data;

@Data
public class CustomException extends Exception {
    private Integer code;

    public CustomException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public CustomException(Integer code , String info) {
        super(info);
        this.code = code;
    }
}