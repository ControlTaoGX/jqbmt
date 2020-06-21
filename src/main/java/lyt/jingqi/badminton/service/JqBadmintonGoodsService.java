package lyt.jingqi.badminton.service;

import lyt.jingqi.badminton.entity.JqBadmintonGoods;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.PageResult;

import java.util.List;

public interface JqBadmintonGoodsService {

    PageResult getJqBadmintonGoodsPage(PageQueryUtil pageQueryUtil);

    String saveJqBadmintonGoods(JqBadmintonGoods goods);

    void batchSaveJqBadmintonGoods(List<JqBadmintonGoods> jqBadmintonGoodsList);

    String updateJqBadmintonGoods(JqBadmintonGoods goods);

    JqBadmintonGoods getJqBadmintonGoodsById(Long id);

    Boolean BctchUpdateSellStatus(Long[] ids, int sellStatus);

    PageResult searchJqBadmintonGoods(PageQueryUtil pageQueryUtil);
}
