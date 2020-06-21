package lyt.jingqi.badminton.dao;

import lyt.jingqi.badminton.entity.JqBadmintonShoppingCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JqBadmintonShoppingCartItemMapper {

    int deleteByPrimaryKey(Long cartItemId);

    int insert(JqBadmintonShoppingCartItem record);

    int insertSelective(JqBadmintonShoppingCartItem record);

    JqBadmintonShoppingCartItem selectByPrimaryKey(Long cartItemId);

    JqBadmintonShoppingCartItem selectByUserIdAndGoodsId(@Param("jqBadmintonUserId") Long JqBadmintonUserId, @Param("goodsId") Long goodsId);

    List<JqBadmintonShoppingCartItem> selectByUserId(@Param("jqBadmintonUserId") Long JqBadmintonUserId, @Param("number") int number);

    int selectCountByUserId(Long JqBadmintonUserId);

    int updateByPrimaryKeySelective(JqBadmintonShoppingCartItem record);

    int updateByPrimaryKey(JqBadmintonShoppingCartItem record);

    int deleteBatch(List<Long> ids);
}
