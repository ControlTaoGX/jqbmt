package lyt.jingqi.badminton.interceptor;

import lyt.jingqi.badminton.common.Constants;
import lyt.jingqi.badminton.controller.vo.JqBadmintonUserVO;
import lyt.jingqi.badminton.dao.JqBadmintonShoppingCartItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JqBadmintonCartNumberInterceptor implements HandlerInterceptor {

    @Autowired
    private JqBadmintonShoppingCartItemMapper jqBadmintonShoppingCartItemMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //购物车中的数量会更改，但是在这些接口中并没有对session中的数据做修改，这里统一处理一下
        if (null != request.getSession() && null != request.getSession().getAttribute(Constants.BADMINTON_USER_SESSION_KEY)) {
            //如果当前为登陆状态，就查询数据库并设置购物车中的数量值
            JqBadmintonUserVO jqBadmintonUserVO = (JqBadmintonUserVO) request.getSession().getAttribute(Constants.BADMINTON_USER_SESSION_KEY);
            //设置购物车中的数量
            jqBadmintonUserVO.setShopCartItemCount(jqBadmintonShoppingCartItemMapper.selectCountByUserId(jqBadmintonUserVO.getUserId()));
            request.getSession().setAttribute(Constants.BADMINTON_USER_SESSION_KEY, jqBadmintonUserVO);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
