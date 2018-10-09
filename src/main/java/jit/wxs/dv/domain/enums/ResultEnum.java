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
    COMMENT_DEL_ERROR("评论删除失败", 16),
    COMMENT_PUBLISH_ERROR("评论发表失败", 17),
    COMMENT_TOO_LONG("评论内容长度不得超过255个字符", 18),
    INDEX_CAROUSEL_NULL("首页轮播图数据为空", 19),
    ADD_LOOK_LATER_ERROR("添加稍后再看失败", 20),
    LOOK_LATER_ALERADY("已经被添加，请不要重复添加", 21),
    DELETE_RECORD_ERROR("删除记录失败", 22),
    CONTENT_INDEX_NOT_EXIST("内容索引不存在", 23),
    CONTENT_INDEX_HAS_EXIST("内容索引已经存在", 24),
    CONTENT_INDEX_CREATE_ERROR("内容索引初始化失败", 25),
    CONTENT_SEARCH_ERROR("内容查询失败", 26),
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