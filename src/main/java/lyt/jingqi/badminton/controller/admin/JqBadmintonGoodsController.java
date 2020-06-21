package lyt.jingqi.badminton.controller.admin;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import lyt.jingqi.badminton.common.Constants;
import lyt.jingqi.badminton.common.JqBadmintonCategoryLevelEnum;
import lyt.jingqi.badminton.common.ServiceResultEnum;
import lyt.jingqi.badminton.entity.GoodsCategory;
import lyt.jingqi.badminton.entity.JqBadmintonGoods;
import lyt.jingqi.badminton.service.JqBadmintonCategoryService;
import lyt.jingqi.badminton.service.JqBadmintonGoodsService;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.Result;
import lyt.jingqi.badminton.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class JqBadmintonGoodsController {

    @Resource
    private JqBadmintonGoodsService jqBadmintonGoodsService;
    @Resource
    private JqBadmintonCategoryService jqBadmintonCategoryService;

    @GetMapping("/goods")
    public String goodsPage(HttpServletRequest request) {
        request.setAttribute("path", "jq_badminton_goods");
        return "admin/jq_badminton_goods";
    }

    /**
     * 添加商品
     * @param request
     * @return
     */
    @GetMapping("/goods/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        //查询所有一级分类
        List<GoodsCategory> firstLevelCategories =
                jqBadmintonCategoryService.selectByLevelAndParentIdsAndNumber(
                        Collections.singletonList(0L), JqBadmintonCategoryLevelEnum.LEVEL_ONE.getLevel()
        );
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            //查询一级分类列表中第一个实体的所有二级分类
            List<GoodsCategory> secondLevelCategories =
                    jqBadmintonCategoryService.selectByLevelAndParentIdsAndNumber(
                            Collections.singletonList(firstLevelCategories.get(0).getCategoryId()),
                            JqBadmintonCategoryLevelEnum.LEVEL_TWO.getLevel()
            );
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表在第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories =
                        jqBadmintonCategoryService.selectByLevelAndParentIdsAndNumber(
                                Collections.singletonList(secondLevelCategories.get(0).getCategoryId()),
                                JqBadmintonCategoryLevelEnum.LEVEL_THREE.getLevel()
                        );
                request.setAttribute("firstLevelCategories", firstLevelCategories);
                request.setAttribute("secondLevelCategories", secondLevelCategories);
                request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                request.setAttribute("path", "goods-edit");
                return "admin/jq_badminton_goods_edit";
            }
        }
        return "error/error_5xx";
    }

    /**
     * 修改商品
     * @param request
     * @param goodsId
     * @return
     */
    @GetMapping("/goods/edit/{goodsId}")
    public String edit(HttpServletRequest request, @PathVariable("goodsId") Long goodsId) {
        request.setAttribute("path", "edit");
        JqBadmintonGoods jqBadmintonGoods = jqBadmintonGoodsService.getJqBadmintonGoodsById(goodsId);
        if (jqBadmintonGoods == null) {
            return "error/error_400";
        }
        if (jqBadmintonGoods.getGoodsCategoryId() > 0) {
            if (jqBadmintonGoods.getGoodsCategoryId() != null || jqBadmintonGoods.getGoodsCategoryId() > 0) {
                //有分类字段则查询相关数据返回给前端以供分类的三级联动显示
                GoodsCategory currentGoodsCategory =
                        jqBadmintonCategoryService.getGoodsCategoryById(jqBadmintonGoods.getGoodsCategoryId());
                //商品变种存储的分类id字段为三级分类的id，不为三级分类则是错误数据
                if (currentGoodsCategory != null &&
                        currentGoodsCategory.getCategoryLevel() == JqBadmintonCategoryLevelEnum.LEVEL_THREE.getLevel()){
                    //查询所有一级分类
                    List<GoodsCategory> firstLevelCategories =
                            jqBadmintonCategoryService.selectByLevelAndParentIdsAndNumber(
                                    Collections.singletonList(0L), JqBadmintonCategoryLevelEnum.LEVEL_ONE.getLevel()
                            );
                    //根据parentId的查询当前parentId下的所有三级分类
                    List<GoodsCategory> thirdLevelCategories =
                            jqBadmintonCategoryService.selectByLevelAndParentIdsAndNumber(
                                    Collections.singletonList(currentGoodsCategory.getParentId()),
                                    JqBadmintonCategoryLevelEnum.LEVEL_THREE.getLevel()
                            );
                    //查询当前三级分类的父级二级分类
                    GoodsCategory sevondCategory =
                            jqBadmintonCategoryService.getGoodsCategoryById(currentGoodsCategory.getParentId());
                    if (sevondCategory != null) {
                        //根据parentId查询当前parentId下所有的二级分类
                        List<GoodsCategory> secondLevelCategories =
                                jqBadmintonCategoryService.selectByLevelAndParentIdsAndNumber(
                                        Collections.singletonList(sevondCategory.getParentId()),
                                                JqBadmintonCategoryLevelEnum.LEVEL_TWO.getLevel()
                                );
                        //查询当前二级人类的父级一级分类
                        GoodsCategory firestCategory = jqBadmintonCategoryService.getGoodsCategoryById(
                                sevondCategory.getParentId()
                        );
                        if (firestCategory != null ) {
                            //所有分类数据都得到之后放到request对象供前端读取
                            request.setAttribute("firstLevelCategories", firstLevelCategories);
                            request.setAttribute("secondLevelCategories", secondLevelCategories);
                            request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                            request.setAttribute("firstLevelCategoryId", firestCategory.getCategoryId());
                            request.setAttribute("secondLevelCategoryId", sevondCategory.getCategoryId());
                            request.setAttribute("thirdLevelCategoryId", currentGoodsCategory.getCategoryId());
                        }
                    }
                }
            }
        }
        if (jqBadmintonGoods.getGoodsCategoryId() == 0) {
            //查询所有一级分类
            List<GoodsCategory> firstLevelCategories =
                    jqBadmintonCategoryService.selectByLevelAndParentIdsAndNumber(
                            Collections.singletonList(0L), JqBadmintonCategoryLevelEnum.LEVEL_ONE.getLevel()
                    );
            if (!CollectionUtils.isEmpty(firstLevelCategories)) {
                //查询一级分类列表中第一个实体的所有二级分类
                List<GoodsCategory> secondLevelCategories =
                        jqBadmintonCategoryService.selectByLevelAndParentIdsAndNumber(
                                Collections.singletonList(firstLevelCategories.get(0).getCategoryId()),
                                JqBadmintonCategoryLevelEnum.LEVEL_TWO.getLevel()
                        );
                if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                    //查询二级分类列表中第一个实体的所有三级分类
                    List<GoodsCategory> thirdLevelCategories =
                            jqBadmintonCategoryService.selectByLevelAndParentIdsAndNumber(
                                    Collections.singletonList(secondLevelCategories.get(0).getCategoryId()),
                                    JqBadmintonCategoryLevelEnum.LEVEL_THREE.getLevel()
                            );
                }
            }
        }
        request.setAttribute("goods", jqBadmintonGoods);
        request.setAttribute("path", "goods-edit");
        return "admin/jq_badminton_goods_edit";
    }

    /**
     * 商品列表
     * @param params
     * @return
     */
    @GetMapping("/goods/list")
    @ResponseBody
    public Result list(@RequestParam Map<String ,Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常!");
        }
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(jqBadmintonGoodsService.getJqBadmintonGoodsPage(pageQueryUtil));
    }

    /**
     * 添加
     * @param jqBadmintonGoods
     * @return
     */
    @PostMapping("/goods/save")
    @ResponseBody
    public Result save(@RequestBody JqBadmintonGoods jqBadmintonGoods) {
        if (StringUtils.isEmpty(jqBadmintonGoods.getGoodsName())
                || StringUtils.isEmpty(jqBadmintonGoods.getGoodsIntro())
                || StringUtils.isEmpty(jqBadmintonGoods.getTag())
                || StringUtils.isEmpty(jqBadmintonGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(jqBadmintonGoods.getGoodsDetailContent())
                || Objects.isNull(jqBadmintonGoods.getOriginalPrice())
                || Objects.isNull(jqBadmintonGoods.getGoodsCategoryId())
                || Objects.isNull(jqBadmintonGoods.getSellingPrice())
                || Objects.isNull(jqBadmintonGoods.getStockNum())
                || Objects.isNull(jqBadmintonGoods.getGoodsSellStatus())
        ) {
            return ResultGenerator.genFailResult("参数异常!");
        }
        String result = jqBadmintonGoodsService.saveJqBadmintonGoods(jqBadmintonGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 修改
     * @param jqBadmintonGoods
     * @return
     */
    @PostMapping("/goods/update")
    @ResponseBody
    public Result update(@RequestBody JqBadmintonGoods jqBadmintonGoods) {
        if (StringUtils.isEmpty(jqBadmintonGoods.getGoodsName())
                || StringUtils.isEmpty(jqBadmintonGoods.getGoodsIntro())
                || StringUtils.isEmpty(jqBadmintonGoods.getTag())
                || StringUtils.isEmpty(jqBadmintonGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(jqBadmintonGoods.getGoodsDetailContent())
                || Objects.isNull(jqBadmintonGoods.getOriginalPrice())
                || Objects.isNull(jqBadmintonGoods.getGoodsCategoryId())
                || Objects.isNull(jqBadmintonGoods.getSellingPrice())
                || Objects.isNull(jqBadmintonGoods.getStockNum())
                || Objects.isNull(jqBadmintonGoods.getGoodsSellStatus())
                || Objects.isNull(jqBadmintonGoods.getGoodsId())
        ) {
            return ResultGenerator.genFailResult("参数异常!");
        }
        String result = jqBadmintonGoodsService.updateJqBadmintonGoods(jqBadmintonGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 商品详情
     * @param id
     * @return
     */
    @GetMapping("/goods/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        JqBadmintonGoods goods = jqBadmintonGoodsService.getJqBadmintonGoodsById(id);
        if (goods == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(goods);
    }

    @PutMapping("/goods/status/{sellStatus}")
    @ResponseBody
    public Result delete(@RequestBody Long[] ids, @PathVariable("sellStatus") int sellStatus) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常!");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
            return ResultGenerator.genFailResult("状态异常!");
        }
        if (jqBadmintonGoodsService.BctchUpdateSellStatus(ids ,sellStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败!");
        }
    }
}
