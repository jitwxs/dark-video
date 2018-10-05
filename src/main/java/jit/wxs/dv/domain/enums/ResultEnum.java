package jit.wxs.dv.domain.enums;

import lombok.Getter;

/**
 * 返回枚举
 * @author jitwxs
 * @since 2018/6/5 23:53
 */
@Getter
public enum ResultEnum {
    /**
     * 返回枚举
     */
    OK("成功",0),
    OTHER_ERROR("其他错误", 10),
    LOGIN_ERROR("用户名或密码错误", 11),
    AUTHORITY_ERROR("鉴权错误", 12),
    PERMISSION_ERROR("权限错误", 13),
    RES_ROOT_ERROR("根路径错误", 13),
    CATEGORY_NOT_EXIST("分类不存在", 14),
    CONTENT_NOT_EXIST("内容不存在", 15),
    PARAM_ERROR("参数错误", 100);

    private String message;
    private int code;

    ResultEnum(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public static String getMessage(int code) {
        for (ResultEnum enums : ResultEnum.values()) {
            if (enums.getCode() == code) {
                return enums.message;
            }
        }
        return null;
    }
}