package lyt.jingqi.badminton.controller.badminton;

import lyt.jingqi.badminton.common.Constants;
import lyt.jingqi.badminton.controller.vo.JqBadmintonGoodsDetailVO;
import lyt.jingqi.badminton.controller.vo.SearchPageCategoryVO;
import lyt.jingqi.badminton.entity.JqBadmintonGoods;
import lyt.jingqi.badminton.service.JqBadmintonCategoryService;
import lyt.jingqi.badminton.service.JqBadmintonGoodsService;
import lyt.jingqi.badminton.util.BeanUtil;
import lyt.jingqi.badminton.util.PageQueryUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class GoodsController {

    @Resource
    private JqBadmintonGoodsService jqBadmintonGoodsService;

    @Resource
    private JqBadmintonCategoryService jqBadmintonCategoryService;

    /**
     * 搜索商品及其回显
     * @param params
     * @param request
     * @return
     */
    @GetMapping({"/search", "/search.html"})
    public String searchPage(@RequestParam Map<String, Object>params, HttpServletRequest request) {
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        //封装分类数据
        if (params.containsKey("goodsCategoryId") && !StringUtils.isEmpty(params.get("goodsCategoryId") + "")) {
            Long categoryId = Long.valueOf(params.get("goodsCategoryId") + "");
            SearchPageCategoryVO searchPageCategoryVO = jqBadmintonCategoryService.getCategoriesForSearch(categoryId);
            if (searchPageCategoryVO != null) {
                request.setAttribute("goodsCategoryId", categoryId);
                request.setAttribute("searchPageCategoryVo", searchPageCategoryVO);
            }
        }
        //封装参数供前端回显
        if (params.containsKey("orderBy") && !StringUtils.isEmpty(params.get("orderBy") + "")) {
            request.setAttribute("orderBy", params.get("orderBy") + "");
        }
        String keyword = "";
        //对keyword做过滤 去掉空格
        if (params.containsKey("keyword") && !StringUtils.isEmpty((params.get("keyword") + "").trim())) {
            keyword = params.get("keyword") + "";
        }
        request.setAttribute("keyword", keyword);
        params.put("keyword", keyword);
        //封装商品数据
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        request.setAttribute("pageResult", jqBadmintonGoodsService.searchJqBadmintonGoods(pageQueryUtil));
        return "badminton/search";
    }

    @GetMapping("/goods/detail/{goodsId}")
    public String detailPage(@PathVariable("goodsId") Long goodsId, HttpServletRequest request) {
        if (goodsId < 1) {
            return "error/error_5xx";
        }
        JqBadmintonGoods goods = jqBadmintonGoodsService.getJqBadmintonGoodsById(goodsId);
        if (goods == null ) {
            return "error/error_404";
        }
        JqBadmintonGoodsDetailVO goodsDetailVO = new JqBadmintonGoodsDetailVO();
        BeanUtil.copyProperties(goods ,goodsDetailVO);
        goodsDetailVO.setGoodsCarouselList(goods.getGoodsCarousel().split(","));
        request.setAttribute("goodsDetail", goodsDetailVO);
        return "badminton/detail";
    }
}
