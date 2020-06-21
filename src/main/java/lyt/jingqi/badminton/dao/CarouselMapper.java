package lyt.jingqi.badminton.dao;

import lyt.jingqi.badminton.entity.Carousel;
import lyt.jingqi.badminton.util.PageQueryUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarouselMapper {

    int deleteByPrimaryKey(Integer carouselId);

    int insert(Carousel record);

    int insertSelective(Carousel record);

    Carousel selectByPrimaryKey(Integer carouselId);

    int updateByPrimaryKey(Carousel record);

    int updateByPrimaryKeySelective(Carousel record);

    List<Carousel> findCarouselList(PageQueryUtil pageQueryUtil);

    int getTotalCarousels(PageQueryUtil pageQueryUtil);

    int deleteBatch(Integer[] ids);

    List<Carousel> findCarouselsByNum(@Param("number") int number);
}
