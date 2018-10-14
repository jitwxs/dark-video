package jit.wxs.dv.controller;

import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.DvContentLookLaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * 稍后再看Controller
 * @author jitwxs
 * @since 2018/10/7 22:27
 */
@Validated
@RestController
@RequestMapping("/content/later")
public class LookLaterController {
    @Autowired
    private DvContentLookLaterService lookLaterService;

    /**
     * 添加到稍后再看
     * @author jitwxs
     * @since 2018/10/7 22:30
     */
    @PostMapping("")
    public ResultVO addLookLater(@NotBlank(message = "内容不能为空") String contentId) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        return lookLaterService.addLookLater(contentId, username);
    }

    /**
     * 删除单一记录
     * @author jitwxs
     * @since 2018/10/7 22:30
     */
    @DeleteMapping("/{id}")
    public ResultVO deleteLookLater(@PathVariable String id) {
        return lookLaterService.deleteLookLater(id);
    }
}
