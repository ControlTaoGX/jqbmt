package lyt.jingqi.badminton.service.Impl;

import lyt.jingqi.badminton.common.ServiceResultEnum;
import lyt.jingqi.badminton.controller.vo.JqBadmintonSearchGoodsVO;
import lyt.jingqi.badminton.dao.JqBadmintonGoodsMapper;
import lyt.jingqi.badminton.entity.JqBadmintonGoods;
import lyt.jingqi.badminton.service.JqBadmintonGoodsService;
import lyt.jingqi.badminton.util.BeanUtil;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.PageResult;
import lyt.jingqi.badminton.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JqBadmintonGoodsServiceImpl implements JqBadmintonGoodsService {

    @Autowired
    private JqBadmintonGoodsMapper jqBadmintonGoodsMapper;

    @Override
    public PageResult getJqBadmintonGoodsPage(PageQueryUtil pageQueryUtil) {
        List<JqBadmintonGoods> goodsList = jqBadmintonGoodsMapper.findJqBadmintonGoodsList(pageQueryUtil);
        int tatal = jqBadmintonGoodsMapper.getTotalJqBadmintonGoods(pageQueryUtil);
        PageResult pageResult = new PageResult(goodsList, tatal ,pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveJqBadmintonGoods(JqBadmintonGoods goods) {
        if (jqBadmintonGoodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSaveJqBadmintonGoods(List<JqBadmintonGoods> jqBadmintonGoodsList) {
        if (!CollectionUtils.isEmpty(jqBadmintonGoodsList)) {
            jqBadmintonGoodsMapper.batchInsert(jqBadmintonGoodsList);
        }
    }

    @Override
    public String updateJqBadmintonGoods(JqBadmintonGoods goods) {
        JqBadmintonGoods temp = jqBadmintonGoodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (jqBadmintonGoodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public JqBadmintonGoods getJqBadmintonGoodsById(Long id) {
        return jqBadmintonGoodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean BctchUpdateSellStatus(Long[] ids, int sellStatus) {
        return jqBadmintonGoodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }

    @Override
    public PageResult searchJqBadmintonGoods(PageQueryUtil pageQueryUtil) {
        List<JqBadmintonGoods> goodsList = jqBadmintonGoodsMapper.findJqBadmintonGoodsListBySearch(pageQueryUtil);
        int tatal = jqBadmintonGoodsMapper.getTotalJqBadmintonGoodsBySearch(pageQueryUtil);
        List<JqBadmintonSearchGoodsVO> jqBadmintonSearchGoodsVOS = new ArrayList<>();
        if(!CollectionUtils.isEmpty(goodsList)) {
            //这个就有点高深了呀
            jqBadmintonSearchGoodsVOS = BeanUtil.copyList(goodsList, JqBadmintonSearchGoodsVO.class);
            for (JqBadmintonSearchGoodsVO jqBadmintonSearchGoodsVO : jqBadmintonSearchGoodsVOS) {
                String goodsName = jqBadmintonSearchGoodsVO.getGoodsName();
                String goodsIntro = jqBadmintonSearchGoodsVO.getGoodsIntro();
                //字符串过长导致文字超出问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    jqBadmintonSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    jqBadmintonSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(jqBadmintonSearchGoodsVOS, tatal, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }
}
