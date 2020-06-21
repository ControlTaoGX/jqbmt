package lyt.jingqi.badminton.controller.vo;

import java.io.Serializable;

/**
 * 首页三级分类
 */
public class ThirdLevelCategoryVO implements Serializable {
    private Long categoryId;
    private Byte categoryLevel;
    private String categoryName;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Byte getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(Byte categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
