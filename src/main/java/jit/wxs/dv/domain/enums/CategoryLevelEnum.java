package jit.wxs.dv.domain.enums;

import lombok.Getter;

/**
 * 目录级别枚举
 * @author jitwxs
 * @since 2018/6/5 23:53
 */
@Getter
public enum CategoryLevelEnum {
    /**
     * 一级目录
     */
    FIRST("first_category", 1),
    /**
     * 二级目录
     */
    SECOND("second_category", 2);

    private String message;
    private int code;

    CategoryLevelEnum(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public static String getMessage(int code) {
        for (CategoryLevelEnum enums : CategoryLevelEnum.values()) {
            if (enums.getCode() == code) {
                return enums.message;
            }
        }
        return null;
    }

    public static CategoryLevelEnum getEnum(Integer code){
        for(CategoryLevelEnum enums : CategoryLevelEnum.values()){
            if(code == enums.getCode()){
                return enums;
            }
        }
        return null;
    }
}
