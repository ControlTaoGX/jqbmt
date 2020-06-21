package lyt.jingqi.badminton.dao;

import lyt.jingqi.badminton.entity.JqBadmintonOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JqBadmintonOrderItemMapper {

    int deleteByPrimaryKey(Long orderItemId);

    int insert(JqBadmintonOrderItem record);

    int insertSelective(JqBadmintonOrderItem record);

    JqBadmintonOrderItem selectByPrimaryKey(Long orderItemId);

    /**
     * 根据订单id获取订单项列表
     *
     * @param orderId
     * @return
     */
    List<JqBadmintonOrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单ids获取订单项列表
     *
     * @param orderIds
     * @return
     */
    List<JqBadmintonOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 批量insert订单项数据
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<JqBadmintonOrderItem> orderItems);

    int updateByPrimaryKeySelective(JqBadmintonOrderItem record);

    int updateByPrimaryKey(JqBadmintonOrderItem record);

}
