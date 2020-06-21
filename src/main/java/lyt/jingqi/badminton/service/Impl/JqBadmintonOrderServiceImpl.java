package lyt.jingqi.badminton.service.Impl;

import lyt.jingqi.badminton.common.*;
import lyt.jingqi.badminton.controller.vo.*;
import lyt.jingqi.badminton.dao.JqBadmintonGoodsMapper;
import lyt.jingqi.badminton.dao.JqBadmintonOrderItemMapper;
import lyt.jingqi.badminton.dao.JqBadmintonOrderMapper;
import lyt.jingqi.badminton.dao.JqBadmintonShoppingCartItemMapper;
import lyt.jingqi.badminton.entity.JqBadmintonGoods;
import lyt.jingqi.badminton.entity.JqBadmintonOrder;
import lyt.jingqi.badminton.entity.JqBadmintonOrderItem;
import lyt.jingqi.badminton.entity.StockNumDTO;
import lyt.jingqi.badminton.service.JqBadmintonOrderService;
import lyt.jingqi.badminton.util.BeanUtil;
import lyt.jingqi.badminton.util.NumberUtil;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class JqBadmintonOrderServiceImpl implements JqBadmintonOrderService {

    @Autowired
    private JqBadmintonOrderMapper jqBadmintonOrderMapper;

    @Autowired
    private JqBadmintonOrderItemMapper jqBadmintonOrderItemMapper;

    @Autowired
    private JqBadmintonShoppingCartItemMapper jqBadmintonShoppingCartItemMapper;

    @Autowired
    private JqBadmintonGoodsMapper jqBadmintonGoodsMapper;


    @Override
    public PageResult getJqBadmintonOrdersPage(PageQueryUtil pageUtil) {
        List<JqBadmintonOrder> jqBadmintonOrders = jqBadmintonOrderMapper.findJqBadmintonOrderList(pageUtil);
        int total = jqBadmintonOrderMapper.getTotalJqBadmintonOrders(pageUtil);
        PageResult pageResult = new PageResult(jqBadmintonOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    /**
     * @param jqBadmintonOrder
     * @return
     */
    @Override
    @Transactional //回滚
    public String updateOrderInfo(JqBadmintonOrder jqBadmintonOrder) {
        JqBadmintonOrder temp = jqBadmintonOrderMapper.selectByPrimaryKey(jqBadmintonOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(jqBadmintonOrder.getTotalPrice());
            temp.setUserAddress(jqBadmintonOrder.getUserAddress());
            temp.setUpdateTime(new Date());
            if (jqBadmintonOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<JqBadmintonOrder> orders = jqBadmintonOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (JqBadmintonOrder jqBadmintonOrder : orders) {
                if (jqBadmintonOrder.getIsDeleted() == 1) {
                    errorOrderNos += jqBadmintonOrder.getOrderNo() + " ";
                    continue;
                }
                if (jqBadmintonOrder.getOrderStatus() != 1) {
                    errorOrderNos += jqBadmintonOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (jqBadmintonOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<JqBadmintonOrder> orders = jqBadmintonOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (JqBadmintonOrder jqBadmintonOrder : orders) {
                if (jqBadmintonOrder.getIsDeleted() == 1) {
                    errorOrderNos += jqBadmintonOrder.getOrderNo() + " ";
                    continue;
                }
                if (jqBadmintonOrder.getOrderStatus() != 1 && jqBadmintonOrder.getOrderStatus() != 2) {
                    errorOrderNos += jqBadmintonOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (jqBadmintonOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }


    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<JqBadmintonOrder> orders = jqBadmintonOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (JqBadmintonOrder jqBadmintonOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (jqBadmintonOrder.getIsDeleted() == 1) {
                    errorOrderNos += jqBadmintonOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (jqBadmintonOrder.getOrderStatus() == 4 || jqBadmintonOrder.getOrderStatus() < 0) {
                    errorOrderNos += jqBadmintonOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (jqBadmintonOrderMapper.closeOrder(Arrays.asList(ids), JqBadmintonOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(JqBadmintonUserVO user, List<JqBadmintonShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(JqBadmintonShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(JqBadmintonShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<JqBadmintonGoods> jqBadmintonGoods = jqBadmintonGoodsMapper.selectByPrimaryKeys(goodsIds);
        Map<Long, JqBadmintonGoods> jqBadmintonGoodsMap = jqBadmintonGoods.stream().collect(Collectors.toMap(JqBadmintonGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (JqBadmintonShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!jqBadmintonGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                JqBadmintonException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > jqBadmintonGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                JqBadmintonException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(jqBadmintonGoods)) {
            if (jqBadmintonShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = jqBadmintonGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    JqBadmintonException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                JqBadmintonOrder jqBadmintonOrder = new JqBadmintonOrder();
                jqBadmintonOrder.setOrderNo(orderNo);
                jqBadmintonOrder.setUserId(user.getUserId());
                jqBadmintonOrder.setUserAddress(user.getAddress());
                //总价
                for (JqBadmintonShoppingCartItemVO jqBadmintonShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += jqBadmintonShoppingCartItemVO.getGoodsCount() * jqBadmintonShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    JqBadmintonException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                jqBadmintonOrder.setTotalPrice(priceTotal);
                //todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
                String extraInfo = "";
                jqBadmintonOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (jqBadmintonOrderMapper.insertSelective(jqBadmintonOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<JqBadmintonOrderItem> jqBadmintonOrderItems = new ArrayList<>();
                    for (JqBadmintonShoppingCartItemVO jqBadmintonShoppingCartItemVO : myShoppingCartItems) {
                        JqBadmintonOrderItem jqBadmintonOrderItem = new JqBadmintonOrderItem();
                        //使用BeanUtil工具类将JqBadmintonShoppingCartItemVO中的属性复制到JqBadmintonOrderItem对象中
                        BeanUtil.copyProperties(jqBadmintonShoppingCartItemVO, jqBadmintonOrderItem);
                        //JqBadmintonOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        jqBadmintonOrderItem.setOrderId(jqBadmintonOrder.getOrderId());
                        jqBadmintonOrderItems.add(jqBadmintonOrderItem);
                    }
                    //保存至数据库
                    if (jqBadmintonOrderItemMapper.insertBatch(jqBadmintonOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    JqBadmintonException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                JqBadmintonException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            JqBadmintonException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        JqBadmintonException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }

    @Override
    public JqBadmintonOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        JqBadmintonOrder jqBadmintonOrder = jqBadmintonOrderMapper.selectByOrderNo(orderNo);
        if (jqBadmintonOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            List<JqBadmintonOrderItem> orderItems = jqBadmintonOrderItemMapper.selectByOrderId(jqBadmintonOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<JqBadmintonOrderItemVO> jqBadmintonOrderItemVOS = BeanUtil.copyList(orderItems, JqBadmintonOrderItemVO.class);
                JqBadmintonOrderDetailVO jqBadmintonOrderDetailVO = new JqBadmintonOrderDetailVO();
                BeanUtil.copyProperties(jqBadmintonOrder, jqBadmintonOrderDetailVO);
                jqBadmintonOrderDetailVO.setOrderStatusString(JqBadmintonOrderStatusEnum.getJqBadmintonOrderStatusEnumByStatus(jqBadmintonOrderDetailVO.getOrderStatus()).getName());
                jqBadmintonOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(jqBadmintonOrderDetailVO.getPayType()).getName());
                jqBadmintonOrderDetailVO.setJqBadmintonOrderItemVOS(jqBadmintonOrderItemVOS);
                return jqBadmintonOrderDetailVO;
            }
        }
        return null;
    }

    @Override
    public JqBadmintonOrder getJqBadmintonOrderByOrderNo(String orderNo) {
        return jqBadmintonOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = jqBadmintonOrderMapper.getTotalJqBadmintonOrders(pageUtil);
        List<JqBadmintonOrder> jqBadmintonOrders = jqBadmintonOrderMapper.findJqBadmintonOrderList(pageUtil);
        List<JqBadmintonOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(jqBadmintonOrders, JqBadmintonOrderListVO.class);
            //设置订单状态中文显示值
            for (JqBadmintonOrderListVO jqBadmintonOrderListVO : orderListVOS) {
                jqBadmintonOrderListVO.setOrderStatusString(JqBadmintonOrderStatusEnum.getJqBadmintonOrderStatusEnumByStatus(jqBadmintonOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = jqBadmintonOrders.stream().map(JqBadmintonOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<JqBadmintonOrderItem> orderItems = jqBadmintonOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<JqBadmintonOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(JqBadmintonOrderItem::getOrderId));
                for (JqBadmintonOrderListVO jqBadmintonOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(jqBadmintonOrderListVO.getOrderId())) {
                        List<JqBadmintonOrderItem> orderItemListTemp = itemByOrderIdMap.get(jqBadmintonOrderListVO.getOrderId());
                        //将JqBadmintonOrderItem对象列表转换成JqBadmintonOrderItemVO对象列表
                        List<JqBadmintonOrderItemVO> jqBadmintonOrderItemVOS = BeanUtil.copyList(orderItemListTemp, JqBadmintonOrderItemVO.class);
                        jqBadmintonOrderListVO.setJqBadmintonOrderItemVOS(jqBadmintonOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        JqBadmintonOrder jqBadmintonOrder = jqBadmintonOrderMapper.selectByOrderNo(orderNo);
        if (jqBadmintonOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            if (jqBadmintonOrderMapper.closeOrder(Collections.singletonList(jqBadmintonOrder.getOrderId()), JqBadmintonOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        JqBadmintonOrder jqBadmintonOrder = jqBadmintonOrderMapper.selectByOrderNo(orderNo);
        if (jqBadmintonOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            jqBadmintonOrder.setOrderStatus((byte) JqBadmintonOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            jqBadmintonOrder.setUpdateTime(new Date());
            if (jqBadmintonOrderMapper.updateByPrimaryKeySelective(jqBadmintonOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        JqBadmintonOrder jqBadmintonOrder = jqBadmintonOrderMapper.selectByOrderNo(orderNo);
        if (jqBadmintonOrder != null) {
            //todo 订单状态判断 非待支付状态下不进行修改操作
            jqBadmintonOrder.setOrderStatus((byte) JqBadmintonOrderStatusEnum.OREDER_PAID.getOrderStatus());
            jqBadmintonOrder.setPayType((byte) payType);
            jqBadmintonOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            jqBadmintonOrder.setPayTime(new Date());
            jqBadmintonOrder.setUpdateTime(new Date());
            if (jqBadmintonOrderMapper.updateByPrimaryKeySelective(jqBadmintonOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public List<JqBadmintonOrderItemVO> getOrderItems(Long id) {
        JqBadmintonOrder jqBadmintonOrder = jqBadmintonOrderMapper.selectByPrimaryKey(id);
        if (jqBadmintonOrder != null) {
            List<JqBadmintonOrderItem> orderItems = jqBadmintonOrderItemMapper.selectByOrderId(jqBadmintonOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<JqBadmintonOrderItemVO> jqBadmintonOrderItemVOS = BeanUtil.copyList(orderItems, JqBadmintonOrderItemVO.class);
                return jqBadmintonOrderItemVOS;
            }
        }
        return null;
    }
}
