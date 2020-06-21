package lyt.jingqi.badminton.service;

import lyt.jingqi.badminton.controller.vo.JqBadmintonShoppingCartItemVO;
import lyt.jingqi.badminton.entity.JqBadmintonShoppingCartItem;

import java.util.List;

public interface JqBadmintonShoppingCartService {

    /**
     * 保存商品至购物车中
     * @param jqBadmintonShoppingCartItem
     * @return
     */
    String saveJqBadmintonCartItem(JqBadmintonShoppingCartItem jqBadmintonShoppingCartItem);

    /**
     * 修改购物车中的属性
     *
     * @param jqBadmintonShoppingCartItem
     * @return
     */
    String updateJqBadmintonCartItem(JqBadmintonShoppingCartItem jqBadmintonShoppingCartItem);

    /**
     * 获取购物项详情
     *
     * @param jqBadmintonShoppingCartItemId
     * @return
     */
    JqBadmintonShoppingCartItem getJqBadmintonCartItemById(Long jqBadmintonShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     * @param jqBadmintonShoppingCartItemId
     * @return
     */
    Boolean deleteById(Long jqBadmintonShoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param jqBadmintonUserId
     * @return
     */
    List<JqBadmintonShoppingCartItemVO> getMyShoppingCartItems(Long jqBadmintonUserId);

}
