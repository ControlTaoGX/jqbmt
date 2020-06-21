package lyt.jingqi.badminton.service;

import lyt.jingqi.badminton.controller.vo.JqBadmintonIndexCarouselVO;
import lyt.jingqi.badminton.entity.Carousel;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.PageResult;

import java.util.List;

public interface JqBadmintonCarouselService {

    /**
     * 后台分页
     *
     * @param pageQueryUtil
     * @return
     */
    PageResult getCarouselPage(PageQueryUtil pageQueryUtil);

    String saveCarousel(Carousel carousel);

    String updateCarousel(Carousel carousel);

    Carousel getCarouselById(Integer id);

    Boolean deleteBatch(Integer[] ids );

    /**
     * 返回固定数量的轮播图对象(首页调用)
     * @param number
     * @return
     */
    List<JqBadmintonIndexCarouselVO> getCarouselsForIndex(int number);
}
