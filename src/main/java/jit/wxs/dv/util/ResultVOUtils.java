package jit.wxs.dv.util;

import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;

/**
 * @author jitwxs
 * @since 2018/6/6 0:42
 */
public class ResultVOUtils {
    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(ResultEnum.OK.getCode(),ResultEnum.OK.getMessage(), data);
    }

    public static ResultVO successWithMsg(String message) {
        return success(message, null);
    }

    public static <T> ResultVO<T> success(String message, T data) {
        return new ResultVO<>(ResultEnum.OK.getCode(), message, data);
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String message) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMessage(message);
        return resultVO;
    }

    public static ResultVO error(ResultEnum resultEnum) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(resultEnum.getCode());
        resultVO.setMessage(resultEnum.getMessage());
        return resultVO;
    }
}
