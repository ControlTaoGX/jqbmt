package lyt.jingqi.badminton.dao;

import lyt.jingqi.badminton.entity.JqBadmintonOrder;
import lyt.jingqi.badminton.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JqBadmintonOrderMapper {

    int deleteByPrimaryKey(Long orderId);

    int insert(JqBadmintonOrder record);

    int insertSelective(JqBadmintonOrder record);

    JqBadmintonOrder selectByPrimaryKey(Long orderId);

    JqBadmintonOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(JqBadmintonOrder record);

    int updateByPrimaryKey(JqBadmintonOrder record);

    List<JqBadmintonOrder> findJqBadmintonOrderList(PageQueryUtil pageUtil);

    int getTotalJqBadmintonOrders(PageQueryUtil pageUtil);

    List<JqBadmintonOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);


}
