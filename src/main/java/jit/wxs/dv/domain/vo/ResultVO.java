package jit.wxs.dv.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回给前台的数据
 * @author jitwxs
 * @since 2018/6/6 0:03
 */
@ApiModel("返回类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO<T> {
    @ApiModelProperty("状态码")
    private Integer code;

    @ApiModelProperty("返回消息")
    private String message;

    @ApiModelProperty("返回数据")
    private T data;
}
