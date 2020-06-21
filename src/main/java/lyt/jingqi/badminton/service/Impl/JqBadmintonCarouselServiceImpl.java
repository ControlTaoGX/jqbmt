package lyt.jingqi.badminton.service.Impl;

import lyt.jingqi.badminton.common.ServiceResultEnum;
import lyt.jingqi.badminton.controller.vo.JqBadmintonIndexCarouselVO;
import lyt.jingqi.badminton.dao.CarouselMapper;
import lyt.jingqi.badminton.entity.Carousel;
import lyt.jingqi.badminton.service.JqBadmintonCarouselService;
import lyt.jingqi.badminton.util.BeanUtil;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JqBadmintonCarouselServiceImpl implements JqBadmintonCarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public PageResult getCarouselPage(PageQueryUtil pageQueryUtil) {
        List<Carousel> carousels = carouselMapper.findCarouselList(pageQueryUtil);
        int total = carouselMapper.getTotalCarousels(pageQueryUtil);
        PageResult pageResult = new PageResult(carousels, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveCarousel(Carousel carousel) {
        if (carouselMapper.insertSelective(carousel) > 0) { //回头再研究研究一下这个是什么判断
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateCarousel(Carousel carousel) {
        Carousel temp = carouselMapper.selectByPrimaryKey(carousel.getCarouselId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        temp.setCarouselRank(carousel.getCarouselRank());
        temp.setCarouselUrl(carousel.getCarouselUrl());
        temp.setRedirectUrl(carousel.getRedirectUrl());
        temp.setUpdateTime(new Date());
        if (carouselMapper.updateByPrimaryKeySelective(temp) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Carousel getCarouselById(Integer id) {
        return carouselMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if(ids.length < 1) {
            return false;
        }
        return carouselMapper.deleteBatch(ids) > 0;
    }

    @Override
    public List<JqBadmintonIndexCarouselVO> getCarouselsForIndex(int number) {
        List<JqBadmintonIndexCarouselVO> jqBadmintonIndexCarouselVOS = new ArrayList<>(number);
        List<Carousel> carousels = carouselMapper.findCarouselsByNum(number);
        if (!CollectionUtils.isEmpty(carousels)) {
            //这一句，要研究
            jqBadmintonIndexCarouselVOS = BeanUtil.copyList(carousels, JqBadmintonIndexCarouselVO.class);
        }
        return jqBadmintonIndexCarouselVOS;
    }
}
