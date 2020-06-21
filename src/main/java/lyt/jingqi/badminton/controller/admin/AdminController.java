package lyt.jingqi.badminton.controller.admin;

import lyt.jingqi.badminton.entity.AdminUser;
import lyt.jingqi.badminton.service.AdminUserService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminUserService adminUserService;

    @GetMapping({"/login"})
    public String login() {
        return "admin/login";
    }
    @RequestMapping("/hello")
    public String hello(){
        return "hello world quick!";
    }

//    @GetMapping({"/test"})
//    public String test() {
//        return "admin/test";
//    }
//
//
    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        request.setAttribute("categoryCount", 0);
        request.setAttribute("blogCount", 0);
        request.setAttribute("linkCount", 0);
        request.setAttribute("tagCount", 0);
        request.setAttribute("commentCount", 0);
        request.setAttribute("path", "index");
        return "admin/index";
    }

    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session) {
        System.out.println("2");
        if (StringUtils.isEmpty(verifyCode)) {
            session.setAttribute("errorMsg", "验证码不能为空");
            return "admin/login";
        }
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/login";
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            session.setAttribute("errorMsg", "验证码错误");
            return "admin/login";
        }
        AdminUser adminUser = adminUserService.login(userName, password);
        if (adminUser != null) {
            session.setAttribute("loginUser", adminUser.getNickName());
            session.setAttribute("loginUserId", adminUser.getAdminUserId());
            //session过期时间设置为7200秒 即两小时
            //session.setMaxInactiveInterval(60 * 60 * 2);
            return "redirect:/admin/index";
        } else {
            session.setAttribute("errorMsg", "登录信息错误");
            return "admin/login";
        }
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request) {
       Integer loginUserId = (int)request.getSession().getAttribute("loginUserId");
       AdminUser adminUser = adminUserService.getUserDetailById(loginUserId);
       if (adminUser == null ) {
           return "admin/login";
       }
       request.setAttribute("path", "profile");
       request.setAttribute("loginUserName",adminUser.getLoginUserName());
       request.setAttribute("nickName", adminUser.getNickName());
       return "admin/profile";
    }

    /**
     * 修改密码
     * 前端请求使用的是Ajax方式
     * 添加@ResponseBody注解之后返回的不再是视图对象而是实际的数据
     * 对应的代码在static/admin/dist/js/profiles.js中
     * @param request
     * @param originalPassword
     * @param newPassword
     * @return
     */
    @PostMapping("profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)){
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if(adminUserService.updatePassword(loginUserId, originalPassword, newPassword)) {
            //修改成功后清空session中的数据，前端控制跳转至首页
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            return "success";
//            return ServiceResultEnum.SUCCESS.getResult();
        } else {
            return "修改失败";
        }
    }
//    @PostMapping("/profile/password")
//    @ResponseBody
//    public String passwordUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
//                                 @RequestParam("newPassword") String newPassword) {
//        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
//            return "参数不能为空";
//        }
//        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
//        if (adminUserService.updatePassword(loginUserId, originalPassword, newPassword)) {
//            //修改成功后清空session中的数据，前端控制跳转至登录页
//            request.getSession().removeAttribute("loginUserId");
//            request.getSession().removeAttribute("loginUser");
//            request.getSession().removeAttribute("errorMsg");
//            return ServiceResultEnum.SUCCESS.getResult();
//        } else {
//            return "修改失败";
//        }
//    }
//
    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request,@RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName") String nickName) {
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if (adminUserService.updateName(loginUserId, loginUserName, nickName)) {
            return "success";
//            return ServiceResultEnum.SUCCESS.getResult();
        } else {
            return "修改失败";
        }
    }


//    @PostMapping("/profile/name")
//    @ResponseBody
//    public String nameUpdate(HttpServletRequest request, @RequestParam("loginUserName") String loginUserName,
//                             @RequestParam("nickName") String nickName) {
//        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName)) {
//            return "参数不能为空";
//        }
//        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
//        if (adminUserService.updateName(loginUserId, loginUserName, nickName)) {
//            return ServiceResultEnum.SUCCESS.getResult();
//        } else {
//            return "修改失败";
//        }
//    }
//
    @GetMapping("/logout")
    public String logout(HttpServletRequest request ) {
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request) {
//        request.getSession().removeAttribute("loginUserId");
//        request.getSession().removeAttribute("loginUser");
//        request.getSession().removeAttribute("errorMsg");
//        return "admin/login";
//    }
}
