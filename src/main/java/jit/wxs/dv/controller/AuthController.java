package jit.wxs.dv.controller;

import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.domain.vo.TreeVO;
import jit.wxs.dv.service.DvCategoryService;
import jit.wxs.dv.service.SysSettingService;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author jitwxs
 * @since 2018/9/29 9:47
 */
@Controller
public class AuthController {
    @Autowired
    private DvCategoryService categoryService;
    @Autowired
    private SysSettingService settingService;

    /**
     * 首页面
     * @author jitwxs
     * @since 2018/10/4 1:26
     */
    @RequestMapping("/")
    public String showIndex(ModelMap map) {
        // 导航数据
        List<TreeVO> navCategory = categoryService.listNavCategory();
        map.addAttribute("navCategory", navCategory);

        return "index";
    }

    /**
     * 登录页面
     * @author jitwxs
     * @since 2018/10/4 23:10
     */
    @RequestMapping("/login")
    public String showLogin() {
        return "login";
    }

    /**
     * 错误信息
     * @author jitwxs
     * @since 2018/10/4 23:11
     */
    @RequestMapping("/auth/error")
    @ResponseBody
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
