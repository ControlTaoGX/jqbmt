package lyt.jingqi.badminton.controller.badminton;

import lyt.jingqi.badminton.common.Constants;
import lyt.jingqi.badminton.common.JqBadmintonException;
import lyt.jingqi.badminton.common.ServiceResultEnum;
import lyt.jingqi.badminton.controller.vo.JqBadmintonOrderDetailVO;
import lyt.jingqi.badminton.controller.vo.JqBadmintonShoppingCartItemVO;
import lyt.jingqi.badminton.controller.vo.JqBadmintonUserVO;
import lyt.jingqi.badminton.entity.JqBadmintonOrder;
import lyt.jingqi.badminton.service.JqBadmintonOrderService;
import lyt.jingqi.badminton.service.JqBadmintonShoppingCartService;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.Result;
import lyt.jingqi.badminton.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Resource
    private JqBadmintonShoppingCartService jqBadmintonShoppingCartService;

    @Resource
    private JqBadmintonOrderService jqBadmintonOrderService;

    @GetMapping("/orders/{orderNo}")
    public String orderDetailPage(HttpServletRequest request, @PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        JqBadmintonUserVO user = (JqBadmintonUserVO) httpSession.getAttribute(Constants.BADMINTON_USER_SESSION_KEY);
        JqBadmintonOrderDetailVO orderDetailVO = jqBadmintonOrderService.getOrderDetailByOrderNo(orderNo, user.getUserId());
        if (orderDetailVO == null) {
            return "error/error_5xx";
        }
        request.setAttribute("orderDetailVO", orderDetailVO);
        return "badminton/order-detail";
    }

    @GetMapping("/orders")
    public String orderListPage(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession httpSession) {
        JqBadmintonUserVO user = (JqBadmintonUserVO) httpSession.getAttribute(Constants.BADMINTON_USER_SESSION_KEY);
        params.put("userId", user.getUserId());
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        //封装我的订单数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("orderPageResult", jqBadmintonOrderService.getMyOrders(pageUtil));
        request.setAttribute("path", "orders");
        return "badminton/my-orders";
    }

    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession httpSession) {
        JqBadmintonUserVO user = (JqBadmintonUserVO) httpSession.getAttribute(Constants.BADMINTON_USER_SESSION_KEY);
        List<JqBadmintonShoppingCartItemVO> myShoppingCartItems = jqBadmintonShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (StringUtils.isEmpty(user.getAddress().trim())) {
            //无收货地址
            JqBadmintonException.fail(ServiceResultEnum.NULL_ADDRESS_ERROR.getResult());
        }
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物车中无数据则跳转至错误页
            JqBadmintonException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        }
        //保存订单并返回订单号
        String saveOrderResult = jqBadmintonOrderService.saveOrder(user, myShoppingCartItems);
        //跳转到订单详情页
        return "redirect:/orders/" + saveOrderResult;
    }
    @PutMapping("/orders/{orderNo}/cancel")
    @ResponseBody
    public Result cancelOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) { JqBadmintonUserVO user = ( JqBadmintonUserVO) httpSession.getAttribute(Constants.BADMINTON_USER_SESSION_KEY);
        String cancelOrderResult = jqBadmintonOrderService.cancelOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @PutMapping("/orders/{orderNo}/finish")
    @ResponseBody
    public Result finishOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        JqBadmintonUserVO user = (JqBadmintonUserVO) httpSession.getAttribute(Constants.BADMINTON_USER_SESSION_KEY);
        String finishOrderResult = jqBadmintonOrderService.finishOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

    @GetMapping("/selectPayType")
    public String selectPayType(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession) {
        JqBadmintonUserVO user = (JqBadmintonUserVO) httpSession.getAttribute(Constants.BADMINTON_USER_SESSION_KEY);
        JqBadmintonOrder jqBadmintonOrder = jqBadmintonOrderService.getJqBadmintonOrderByOrderNo(orderNo);
        //todo 判断订单userId
        //todo 判断订单状态
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", jqBadmintonOrder.getTotalPrice());
        return "badminton/pay-select";
    }
    @GetMapping("/payPage")
    public String payOrder(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession, @RequestParam("payType") int payType) {
        JqBadmintonUserVO user = (JqBadmintonUserVO) httpSession.getAttribute(Constants.BADMINTON_USER_SESSION_KEY);
        JqBadmintonOrder jqBadmintonOrder = jqBadmintonOrderService.getJqBadmintonOrderByOrderNo(orderNo);
        //todo 判断订单userId
        //todo 判断订单状态
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", jqBadmintonOrder.getTotalPrice());
        if (payType == 1) {
            return "badminton/alipay";
        } else {
            return "badminton/wxpay";
        }
    }

    @GetMapping("/paySuccess")
    @ResponseBody
    public Result paySuccess(@RequestParam("orderNo") String orderNo, @RequestParam("payType") int payType) {
        String payResult = jqBadmintonOrderService.paySuccess(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(payResult);
        }
    }

}
