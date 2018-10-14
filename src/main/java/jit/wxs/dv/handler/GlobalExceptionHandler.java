package jit.wxs.dv.handler;

import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.util.JsonUtils;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * @author jitwxs
 * @date 2018/4/25 16:43
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO globalException(Exception e) {
        return ResultVOUtils.error(ResultEnum.OTHER_ERROR.getCode(), e.getMessage());
    }

    /**
     * 自定义异常捕捉
     * @author jitwxs
     * @since 2018/10/14 15:05
     */
    @ExceptionHandler(value = CustomException.class)
    public ModelAndView customException(CustomException ce) {
        Map<String, Object> model = new HashMap<>(16);
        model.put("errorMsg", ce.getMessage());

        return new ModelAndView("error", model);
    }

    /**
     * Validated异常捕捉
     * @author jitwxs
     * @since 2018/10/14 15:05
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handleConstraintViolationException(ConstraintViolationException cve){
        Set<ConstraintViolation<?>> cves = cve.getConstraintViolations();
        List<String> errorList = new ArrayList<>(cves.size() + 1);
        for (ConstraintViolation<?> constraintViolation : cves) {
            errorList.add(constraintViolation.getMessage());
        }

        Map<String, Object> model = new HashMap<>(16);
        model.put("errorMsg", JsonUtils.objectToJson(errorList));

        return new ModelAndView("error", model);
    }
}
