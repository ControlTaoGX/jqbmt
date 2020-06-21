package lyt.jingqi.badminton.service.Impl;

import lyt.jingqi.badminton.common.Constants;
import lyt.jingqi.badminton.common.ServiceResultEnum;
import lyt.jingqi.badminton.controller.vo.JqBadmintonShoppingCartItemVO;
import lyt.jingqi.badminton.dao.JqBadmintonGoodsMapper;
import lyt.jingqi.badminton.dao.JqBadmintonShoppingCartItemMapper;
import lyt.jingqi.badminton.entity.JqBadmintonGoods;
import lyt.jingqi.badminton.entity.JqBadmintonShoppingCartItem;
import lyt.jingqi.badminton.service.JqBadmintonShoppingCartService;
import lyt.jingqi.badminton.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JqBadmintonShoppingCartServiceImpl implements JqBadmintonShoppingCartService {

    @Autowired
    private JqBadmintonGoodsMapper jqBadmintonGoodsMapper;

    @Autowired
    private JqBadmintonShoppingCartItemMapper jqBadmintonShoppingCartItemMapper;

    @Override
    public String saveJqBadmintonCartItem(JqBadmintonShoppingCartItem jqBadmintonShoppingCartItem) {
        JqBadmintonShoppingCartItem temp = jqBadmintonShoppingCartItemMapper.selectByUserIdAndGoodsId(jqBadmintonShoppingCartItem.getUserId(), jqBadmintonShoppingCartItem.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            //todo count = tempCount + 1
            temp.setGoodsCount(jqBadmintonShoppingCartItem.getGoodsCount());
            return updateJqBadmintonCartItem(temp);
        }
        JqBadmintonGoods jqBadmintonGoods = jqBadmintonGoodsMapper.selectByPrimaryKey(jqBadmintonShoppingCartItem.getGoodsId());
        //商品为空
        if (jqBadmintonGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = jqBadmintonShoppingCartItemMapper.selectCountByUserId(jqBadmintonShoppingCartItem.getUserId());
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //保存记录
        if (jqBadmintonShoppingCartItemMapper.insertSelective(jqBadmintonShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateJqBadmintonCartItem(JqBadmintonShoppingCartItem jqBadmintonShoppingCartItem) {
        JqBadmintonShoppingCartItem jqBadmintonShoppingCartItemUpdate = jqBadmintonShoppingCartItemMapper.selectByPrimaryKey(jqBadmintonShoppingCartItem.getCartItemId());
        if (jqBadmintonShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出最大数量
        if (jqBadmintonShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //todo 数量相同不会进行修改
        //todo userId不同不能修改
        jqBadmintonShoppingCartItemUpdate.setGoodsCount(jqBadmintonShoppingCartItem.getGoodsCount());
        jqBadmintonShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (jqBadmintonShoppingCartItemMapper.updateByPrimaryKeySelective(jqBadmintonShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public JqBadmintonShoppingCartItem getJqBadmintonCartItemById(Long jqBadmintonShoppingCartItemId) {
        return jqBadmintonShoppingCartItemMapper.selectByPrimaryKey(jqBadmintonShoppingCartItemId);
    }

    @Override
    public Boolean deleteById(Long jqBadmintonShoppingCartItemId) {
        //todo userId不同不能删除
        return jqBadmintonShoppingCartItemMapper.deleteByPrimaryKey(jqBadmintonShoppingCartItemId) > 0;
    }

    @Override
    public List<JqBadmintonShoppingCartItemVO> getMyShoppingCartItems(Long jqBadmintonUserId) {
        List<JqBadmintonShoppingCartItemVO> jqBadmintonShoppingCartItemVOS = new ArrayList<>();
        List<JqBadmintonShoppingCartItem> jqBadmintonShoppingCartItems = jqBadmintonShoppingCartItemMapper.selectByUserId(jqBadmintonUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        if (!CollectionUtils.isEmpty(jqBadmintonShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> jqBadmintonGoodsIds = jqBadmintonShoppingCartItems.stream().map(JqBadmintonShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<JqBadmintonGoods> jqBadmintonGoods = jqBadmintonGoodsMapper.selectByPrimaryKeys(jqBadmintonGoodsIds);
            Map<Long, JqBadmintonGoods> jqBadmintonGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(jqBadmintonGoods)) {
                jqBadmintonGoodsMap = jqBadmintonGoods.stream().collect(Collectors.toMap(JqBadmintonGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (JqBadmintonShoppingCartItem jqBadmintonShoppingCartItem : jqBadmintonShoppingCartItems) {
                JqBadmintonShoppingCartItemVO jqBadmintonShoppingCartItemVO = new JqBadmintonShoppingCartItemVO();
                BeanUtil.copyProperties(jqBadmintonShoppingCartItem, jqBadmintonShoppingCartItemVO);
                if (jqBadmintonGoodsMap.containsKey(jqBadmintonShoppingCartItem.getGoodsId())) {
                    JqBadmintonGoods jqBadmintonGoodsTemp = jqBadmintonGoodsMap.get(jqBadmintonShoppingCartItem.getGoodsId());
                    jqBadmintonShoppingCartItemVO.setGoodsCoverImg(jqBadmintonGoodsTemp.getGoodsCoverImg());
                    String goodsName = jqBadmintonGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    jqBadmintonShoppingCartItemVO.setGoodsName(goodsName);
                    jqBadmintonShoppingCartItemVO.setSellingPrice(jqBadmintonGoodsTemp.getSellingPrice());
                    jqBadmintonShoppingCartItemVOS.add(jqBadmintonShoppingCartItemVO);
                }
            }
        }
        return jqBadmintonShoppingCartItemVOS;
    }
}

