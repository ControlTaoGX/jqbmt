package lyt.jingqi.badminton.service;

import lyt.jingqi.badminton.controller.vo.JqBadmintonIndexCategoryVO;
import lyt.jingqi.badminton.controller.vo.SearchPageCategoryVO;
import lyt.jingqi.badminton.entity.GoodsCategory;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.PageResult;

import java.util.List;

public interface JqBadmintonCategoryService {

    PageResult getCategorisPage(PageQueryUtil pageQueryUtil);

    String saveCategory(GoodsCategory goodsCategory);

    String updateGoodsCategory(GoodsCategory  goodsCategory);

    GoodsCategory getGoodsCategoryById(Long id);

    Boolean deleteBatch(Integer[] ids);

    List<JqBadmintonIndexCategoryVO> getCategoriesForIndex();

    SearchPageCategoryVO getCategoriesForSearch(Long categoryId);

    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);
}
