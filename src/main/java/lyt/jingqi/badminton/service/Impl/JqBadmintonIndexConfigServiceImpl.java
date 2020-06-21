package lyt.jingqi.badminton.service.Impl;

import lyt.jingqi.badminton.common.ServiceResultEnum;
import lyt.jingqi.badminton.controller.vo.JqBadmintonIndexConfigGoodsVO;
import lyt.jingqi.badminton.dao.IndexConfigMapper;
import lyt.jingqi.badminton.dao.JqBadmintonGoodsMapper;
import lyt.jingqi.badminton.entity.IndexConfig;
import lyt.jingqi.badminton.entity.JqBadmintonGoods;
import lyt.jingqi.badminton.service.JqBadmintonIndexConfigService;
import lyt.jingqi.badminton.util.BeanUtil;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JqBadmintonIndexConfigServiceImpl implements JqBadmintonIndexConfigService {

    @Autowired
    private JqBadmintonGoodsMapper goodsMapper;

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Override
    public PageResult getConfigsPage(PageQueryUtil pageQueryUtil) {
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageQueryUtil);
        int total = indexConfigMapper.getTotalIndexConfigs(pageQueryUtil);
        PageResult pageResult = new PageResult(indexConfigs, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        //判断是否存在该商品
        if (indexConfigMapper.insertSelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        //判断是否存在该商品
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public IndexConfig getIndexConfigById(Long id) {
        return null;
    }

    @Override
    public List<JqBadmintonIndexConfigGoodsVO> getConfigGoodsForIndex(int configType, int number) {
        List<JqBadmintonIndexConfigGoodsVO> jqBadmintonIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有goodsId                        这一句高深莫测呀
            List<Long> goodsId = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<JqBadmintonGoods> jqBadmintonGoods = goodsMapper.selectByPrimaryKeys(goodsId);
            jqBadmintonIndexConfigGoodsVOS = BeanUtil.copyList(jqBadmintonGoods, JqBadmintonIndexConfigGoodsVO.class);
            for (JqBadmintonIndexConfigGoodsVO jqBadmintonIndexConfigGoodsVO : jqBadmintonIndexConfigGoodsVOS) {
                String goodsName = jqBadmintonIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = jqBadmintonIndexConfigGoodsVO.getGoodsIntro();
                //字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    jqBadmintonIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    jqBadmintonIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return jqBadmintonIndexConfigGoodsVOS;
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        return indexConfigMapper.deleteBatch(ids) > 0;
    }
}
