package jit.wxs.dv.controller;

import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jitwxs
 * @since 2018/9/29 9:47
 */
@RestController
public class AuthController {

    @RequestMapping("/auth/error")
    public ResultVO loginError(HttpServletRequest request) {
        // 如果Spring Security中有异常，输出
        AuthenticationException exception =
                (AuthenticationException)request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if(exception != null) {
            return ResultVOUtils.error(ResultEnum.AUTHORITY_ERROR.getCode(), exception.toString());
        }

        // 其次从ERR_MSG中取
        Object obj = request.getAttribute("ERR_MSG");
        if(obj instanceof ResultVO) {
            return (ResultVO)obj;
        }
        if(obj instanceof ResultEnum) {
            return ResultVOUtils.error((ResultEnum)obj);
        } else {
            return ResultVOUtils.error(ResultEnum.OTHER_ERROR);
        }
    }


}
