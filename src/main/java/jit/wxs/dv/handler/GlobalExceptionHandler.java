package jit.wxs.dv.handler;

import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jitwxs
 * @date 2018/4/25 16:43
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO globalException(HttpServletRequest request, Exception e) {
        return ResultVOUtils.error(ResultEnum.OTHER_ERROR.getCode(), e.getMessage());
    }
}
