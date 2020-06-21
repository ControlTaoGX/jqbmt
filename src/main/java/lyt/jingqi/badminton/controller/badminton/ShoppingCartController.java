package lyt.jingqi.badminton.controller.badminton;

import lyt.jingqi.badminton.common.Constants;
import lyt.jingqi.badminton.common.ServiceResultEnum;
import lyt.jingqi.badminton.controller.vo.JqBadmintonShoppingCartItemVO;
import lyt.jingqi.badminton.controller.vo.JqBadmintonUserVO;
import lyt.jingqi.badminton.entity.JqBadmintonShoppingCartItem;
import lyt.jingqi.badminton.service.JqBadmintonShoppingCartService;
import lyt.jingqi.badminton.util.Result;
import lyt.jingqi.badminton.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingCartController {

    @Resource
    private JqBadmintonShoppingCartService jqBadmintonShoppingCartService;

    @GetMapping("/shop-cart")
    public String cartListPage(HttpServletRequest request,
                               HttpSession httpSession) {
        JqBadmintonUserVO user = (JqBadmintonUserVO) httpSession.getAttribute(Constants.BADMINTON_USER_SESSION_KEY);
        int itemsTotal = 0;
        int priceTotal = 0;
        List<JqBadmintonShoppingCartItemVO> myShoppingCartItems = jqBadmintonShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物项总数
            itemsTotal = myShoppingCartItems.stream().mapToInt(JqBadmintonShoppingCartItemVO::getGoodsCount).sum();
            if (itemsTotal < 1) {
                return "error/error_5xx";
            }
            //总价
            for (JqBadmintonShoppingCartItemVO jqBadmintonShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += jqBadmintonShoppingCartItemVO.getGoodsCount() * jqBadmintonShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "badminton/cart";
    }

    @PostMapping("/shop-cart")
    @ResponseBody
    public Result saveJqBadmintonShoppingCartItem(@RequestBody JqBadmintonShoppingCartItem jqBadmintonShoppingCartItem,
                                                 HttpSession httpSession) {
        JqBadmintonUserVO user = (JqBadmintonUserVO) httpSession.getAttribute(Constants.BADMINTON_USER_SESSION_KEY);
        jqBadmintonShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String saveResult = jqBadmintonShoppingCartService.saveJqBadmintonCartItem(jqBadmintonShoppingCartItem);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ResponseBody
    public Result updateJqBadmintonShoppingCartItem(@RequestBody JqBadmintonShoppingCartItem jqBadmintonShoppingCartItem,
                                                   HttpSession httpSession) {
        JqBadmintonUserVO user = (JqBadmintonUserVO) httpSession.getAttribute(Constants.BADMINTON_USER_SESSION_KEY);
        jqBadmintonShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String saveResult = jqBadmintonShoppingCartService.updateJqBadmintonCartItem(jqBadmintonShoppingCartItem);
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @DeleteMapping("/shop-cart/{jqBadmintonShoppingCartItemId}")
    @ResponseBody
    public Result updateJqBadmintonShoppingCartItem(@PathVariable("jqBadmintonShoppingCartItemId") Long jqBadmintonShoppingCartItemId,
                                                   HttpSession httpSession) {
        JqBadmintonUserVO user = (JqBadmintonUserVO) httpSession.getAttribute(Constants.BADMINTON_USER_SESSION_KEY);
        Boolean deleteResult = jqBadmintonShoppingCartService.deleteById(jqBadmintonShoppingCartItemId);
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    public String settlePage(HttpServletRequest request,
                             HttpSession httpSession) {
        int priceTotal = 0;
        JqBadmintonUserVO user = (JqBadmintonUserVO) httpSession.getAttribute(Constants.BADMINTON_USER_SESSION_KEY);
        List<JqBadmintonShoppingCartItemVO> myShoppingCartItems = jqBadmintonShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //无数据则不跳转至结算页
            return "/shop-cart";
        } else {
            //总价
            for (JqBadmintonShoppingCartItemVO jqBadmintonShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += jqBadmintonShoppingCartItemVO.getGoodsCount() * jqBadmintonShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "badminton/order-settle";
    }
}
