package lyt.jingqi.badminton.controller.badminton;

import lyt.jingqi.badminton.common.Constants;
import lyt.jingqi.badminton.common.IndexConfigTypeEnum;
import lyt.jingqi.badminton.controller.vo.JqBadmintonIndexCarouselVO;
import lyt.jingqi.badminton.controller.vo.JqBadmintonIndexCategoryVO;
import lyt.jingqi.badminton.controller.vo.JqBadmintonIndexConfigGoodsVO;
import lyt.jingqi.badminton.service.JqBadmintonCarouselService;
import lyt.jingqi.badminton.service.JqBadmintonCategoryService;
import lyt.jingqi.badminton.service.JqBadmintonIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private JqBadmintonCarouselService jqBadmintonCarouselService;

    @Resource
    private JqBadmintonIndexConfigService jqBadmintonIndexConfigService;

    @Resource
    private JqBadmintonCategoryService jqBadmintonCategoryService;

    @GetMapping({"/index", "/", "index.html"})
    public String indexPage(HttpServletRequest request) {
        List<JqBadmintonIndexCategoryVO> categories = jqBadmintonCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            return "error/error_5xx";
        }
        List<JqBadmintonIndexCarouselVO> carouselVOS =
                jqBadmintonCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<JqBadmintonIndexConfigGoodsVO> hotGoodses =
                jqBadmintonIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(),
                        Constants.INDEX_GOODS_HOT_NUMBER);
        List<JqBadmintonIndexConfigGoodsVO> newGoodses =
                jqBadmintonIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(),
                        Constants.INDEX_GOODS_NEW_NUMBER);
        List<JqBadmintonIndexConfigGoodsVO> recommendGooses =
                jqBadmintonIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(),
                        Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        request.setAttribute("categories", categories);
        request.setAttribute("carousels", carouselVOS);
        request.setAttribute("hotGoodses",hotGoodses);
        request.setAttribute("newGoodses", newGoodses);
        request.setAttribute("recommendGoodses", recommendGooses); //推荐商品
        return "badminton/index";
    }


}
