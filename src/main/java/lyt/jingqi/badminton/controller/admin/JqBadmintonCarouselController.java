package lyt.jingqi.badminton.controller.admin;

import lyt.jingqi.badminton.common.ServiceResultEnum;
import lyt.jingqi.badminton.entity.Carousel;
import lyt.jingqi.badminton.service.JqBadmintonCarouselService;
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

/**
 * 轮播图控制器
 */
@Controller
@RequestMapping("/admin")
public class JqBadmintonCarouselController {

    @Resource
    JqBadmintonCarouselService jqBadmintonCarouselService;

    @GetMapping("/carousels")
    public String carouselPage (HttpServletRequest request) {
        request.setAttribute("path", "jq_badminton_carousel");
        return "admin/jq_badminton_carousel";
    }

    /**
     * 轮播图列表
     * @param params
     * @return
     */
    @GetMapping("/carousels/list")
    @ResponseBody
    public Result list (@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常!");
        }
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(jqBadmintonCarouselService.getCarouselPage(pageQueryUtil));
    }

    /**
     * 添加
     * @param carousel
     * @return
     */
    @PostMapping("/carousels/save")
    @ResponseBody
    public Result save(@RequestBody Carousel carousel) {
        if (StringUtils.isEmpty(carousel.getCarouselUrl())
        || Objects.isNull(carousel.getCarouselRank())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = jqBadmintonCarouselService.saveCarousel(carousel);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 修改
     * @param carousel
     * @return
     */
    @PostMapping("/carousels/update")
    @ResponseBody
    public Result update(@RequestBody Carousel carousel) {
        if (Objects.isNull(carousel.getCarouselId())
        || StringUtils.isEmpty(carousel.getCarouselUrl())
        || Objects.isNull(carousel.getCarouselRank())) {
            return ResultGenerator.genFailResult("参数异常! ");
        }
        String result = jqBadmintonCarouselService.updateCarousel(carousel);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     * @param id
     * @return
     */
    @GetMapping("/carousels/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Integer id) {
        Carousel carousel = jqBadmintonCarouselService.getCarouselById(id);
        if (carousel == null ) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(carousel);
    }

    @PostMapping("/carousels/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常! ");
        }
        if (jqBadmintonCarouselService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败! ");
        }
    }
}
