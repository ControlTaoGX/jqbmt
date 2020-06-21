package lyt.jingqi.badminton.service;

import lyt.jingqi.badminton.controller.vo.JqBadmintonIndexConfigGoodsVO;
import lyt.jingqi.badminton.entity.IndexConfig;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.PageResult;

import java.util.List;

public interface JqBadmintonIndexConfigService {

    PageResult getConfigsPage(PageQueryUtil pageQueryUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    List<JqBadmintonIndexConfigGoodsVO> getConfigGoodsForIndex(int configType, int number);

    Boolean deleteBatch(Long[] ids);
}
