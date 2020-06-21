package lyt.jingqi.badminton.controller.badminton;


import lyt.jingqi.badminton.common.Constants;
import lyt.jingqi.badminton.common.ServiceResultEnum;
import lyt.jingqi.badminton.controller.vo.JqBadmintonUserVO;
import lyt.jingqi.badminton.entity.BadmintonUser;
import lyt.jingqi.badminton.service.JqBadmintonUserService;
import lyt.jingqi.badminton.util.MD5Util;
import lyt.jingqi.badminton.util.Result;
import lyt.jingqi.badminton.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class PersonalController {

    @Resource
    private JqBadmintonUserService jqBadmintonUserService;

    @GetMapping("/personal")
    public String personalPage(HttpServletRequest request, HttpSession httpSession) {
        request.setAttribute("path", "personal");
        return "badminton/personal";
    }

    /**
     * 登录页面跳转
     * @return
     */
    @GetMapping({"/login", "login.html"})
    public String loginPage() {
        return "badminton/login";
    }

    /**
     * 注册页面跳转
     * @return
     */
    @GetMapping({"/register", "register.html"})
    public String registerPage() {
        return "badminton/register";
    }

    /**
     * 退出
     * @param httpSession
     * @return
     */
    @GetMapping("logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute(Constants.BADMINTON_USER_SESSION_KEY);
        return "badminton/login";
    }

    /**
     * 个人地址跳转
     * @return
     */
    @GetMapping("/personal/addresses")
    public String addressesPage() {
        return "badminton/addresses";
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestParam("loginName") String loginName,
                        @RequestParam("verifyCode") String verifyCode,
                        @RequestParam("password") String password,
                        HttpSession httpSession) {
        if (StringUtils.isEmpty(loginName)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
        }
        if (StringUtils.isEmpty(password)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
        }
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_NULL.getResult());
        }
        String kaptchaCode = httpSession.getAttribute(Constants.BADMINTON_VERIFY_CODE_KEY) + "";
        if(StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
        }
//        System.out.println("1...");
        String loginResult = jqBadmintonUserService.login(loginName, MD5Util.MD5Encode(password, "UTF-8"),httpSession);
        //登录成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(loginResult)) {
//            System.out.println("2...");
            return ResultGenerator.genSuccessResult();
        }
        //登录失败
        return ResultGenerator.genSuccessResult(loginResult);
    }


    /**
     * 注册
     * @param loginName
     * @param verifyCode
     * @param password
     * @param httpSession
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public Result register(@RequestParam("loginName") String loginName,
                           @RequestParam("verifyCode") String verifyCode,
                           @RequestParam("password") String password,
                           HttpSession httpSession) {
        if (StringUtils.isEmpty(loginName)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
        }
        if (StringUtils.isEmpty(password)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
        }
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_NULL.getResult());
        }
        String kaptchaCode = httpSession.getAttribute(Constants.BADMINTON_VERIFY_CODE_KEY) + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
        }
        String registerResult = jqBadmintonUserService.register(loginName, password);
        //注册成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //注册失败
        return ResultGenerator.genFailResult(registerResult);
    }


    /**
     *
     * @param badmintonUser
     * @param httpSession
     * @return
     */
    @PostMapping("/personal/updateInfo")
    @ResponseBody
    public Result updateInfo(@RequestBody BadmintonUser badmintonUser, HttpSession httpSession) {
        JqBadmintonUserVO badmintonUesrTemp = jqBadmintonUserService.updateUserInfo(badmintonUser, httpSession);
        if (badmintonUesrTemp == null) {
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        } else {
            return ResultGenerator.genSuccessResult();
        }
    }
}
