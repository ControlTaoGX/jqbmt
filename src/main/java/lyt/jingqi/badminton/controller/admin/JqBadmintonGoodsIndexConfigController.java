package lyt.jingqi.badminton.controller.admin;

import lyt.jingqi.badminton.common.IndexConfigTypeEnum;
import lyt.jingqi.badminton.common.ServiceResultEnum;
import lyt.jingqi.badminton.entity.IndexConfig;
import lyt.jingqi.badminton.service.JqBadmintonIndexConfigService;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.Result;
import lyt.jingqi.badminton.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class JqBadmintonGoodsIndexConfigController {

    @Resource
    private JqBadmintonIndexConfigService jqBadmintonIndexConfigService;

    @GetMapping("/indexConfigs")
    public String indexConfigsPage(HttpServletRequest request, @RequestParam("configType") int configType) {
        IndexConfigTypeEnum indexConfigTypeEnum = IndexConfigTypeEnum.getIndexConfigTypeEnumByType(configType);
        if (indexConfigTypeEnum.equals(IndexConfigTypeEnum.DEFAULT)) {
            return "error/error_5xx";
        }
        request.setAttribute("path", indexConfigTypeEnum.getName());
        request.setAttribute("configType", configType);
        return "admin/jq_badminton_index_config";
    }

    /**
     * 列表
     *
     * @param params
     * @return
     */
    @GetMapping("/indexConfigs/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常!");
        }
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
//        System.out.println(params);
        return ResultGenerator.genSuccessResult(jqBadmintonIndexConfigService.getConfigsPage(pageQueryUtil));
    }

    /**
     * 添加
     *
     * @param indexConfig
     * @return
     */
    @PostMapping("/indexConfigs/save")
    @ResponseBody
    public Result save(@RequestBody IndexConfig indexConfig) {
        if (Objects.isNull(indexConfig.getConfigType())
                || StringUtils.isEmpty(indexConfig.getConfigName())
                || Objects.isNull(indexConfig.getConfigRank())
        ) {
            return ResultGenerator.genFailResult("参数异常!");
        }
        String result = jqBadmintonIndexConfigService.saveIndexConfig(indexConfig);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 修改
     *
     * @param indexConfig
     * @return
     */
    @PostMapping("/indexConfigs/update")
    @ResponseBody
    public Result update(@RequestBody IndexConfig indexConfig) {
        if (Objects.isNull(indexConfig.getConfigType())
                || Objects.isNull(indexConfig.getConfigId())
                || Objects.isNull(indexConfig.getConfigRank())
                || StringUtils.isEmpty(indexConfig.getConfigName())
        ) {
            return ResultGenerator.genFailResult("参数异常!");
        }
        String result = jqBadmintonIndexConfigService.updateIndexConfig(indexConfig);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     * 这里的数据库查询结果默认为空
     *
     * @param id
     * @return
     */
    @GetMapping("/indexConfigs/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        IndexConfig config = jqBadmintonIndexConfigService.getIndexConfigById(id);
        if (config == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(config);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @PostMapping("/indexConfigs/delete")
    @ResponseBody
    public Result delete(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常!");
        }
        if (jqBadmintonIndexConfigService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败!");
        }
    }
}

