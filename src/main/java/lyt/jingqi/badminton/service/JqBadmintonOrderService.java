package lyt.jingqi.badminton.service;

import lyt.jingqi.badminton.controller.vo.JqBadmintonOrderDetailVO;
import lyt.jingqi.badminton.controller.vo.JqBadmintonOrderItemVO;
import lyt.jingqi.badminton.controller.vo.JqBadmintonShoppingCartItemVO;
import lyt.jingqi.badminton.controller.vo.JqBadmintonUserVO;
import lyt.jingqi.badminton.entity.JqBadmintonOrder;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.PageResult;

import java.util.List;

public interface JqBadmintonOrderService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getJqBadmintonOrdersPage(PageQueryUtil pageUtil);

    /**
     * 订单信息修改
     *
     * @param jqBadmintonOrder
     * @return
     */
    String updateOrderInfo(JqBadmintonOrder jqBadmintonOrder);

    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    /**
     * 保存订单
     *
     * @param user
     * @param myShoppingCartItems
     * @return
     */
    String saveOrder(JqBadmintonUserVO user, List<JqBadmintonShoppingCartItemVO> myShoppingCartItems);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param userId
     * @return
     */
    JqBadmintonOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    JqBadmintonOrder getJqBadmintonOrderByOrderNo(String orderNo);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    List<JqBadmintonOrderItemVO> getOrderItems(Long id);
}
