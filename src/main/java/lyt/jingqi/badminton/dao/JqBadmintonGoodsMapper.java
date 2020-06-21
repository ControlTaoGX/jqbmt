package lyt.jingqi.badminton.dao;

import lyt.jingqi.badminton.entity.JqBadmintonGoods;
import lyt.jingqi.badminton.entity.StockNumDTO;
import lyt.jingqi.badminton.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JqBadmintonGoodsMapper {

    int deleteByPrimaryKey(Long goodsId);

    int insert(JqBadmintonGoods record);

    int insertSelective(JqBadmintonGoods record);

    JqBadmintonGoods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(JqBadmintonGoods record);

    int updateByPrimaryKeyWithBLOBs(JqBadmintonGoods record);//这是干嘛的

    int updateByPrimaryKey(JqBadmintonGoods record);

    List<JqBadmintonGoods> findJqBadmintonGoodsList(PageQueryUtil pageUtil);

    int getTotalJqBadmintonGoods(PageQueryUtil pageUtil);

    List<JqBadmintonGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<JqBadmintonGoods> findJqBadmintonGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalJqBadmintonGoodsBySearch(PageQueryUtil pageUtil);

    /**
     *
     * @param jqBadmintonGoodsList
     * @return
     */
    int batchInsert(@Param("jqBadmintonGoodsList") List<JqBadmintonGoods> jqBadmintonGoodsList);

    /**
     *
     * @param stockNumDTOS
     * @return
     */
    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    /**
     * 更新商品状态
     * @param orderIds
     * @param sellStatus
     * @return
     */
    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

}
