package lyt.jingqi.badminton.service;

import lyt.jingqi.badminton.controller.vo.JqBadmintonUserVO;
import lyt.jingqi.badminton.entity.BadmintonUser;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.PageResult;

import javax.servlet.http.HttpSession;

public interface JqBadmintonUserService {

    PageResult getJqBadmintonUsersPage(PageQueryUtil pageQueryUtil);

    String register(String loginName, String password);

    String login(String loginName, String passwordMD5, HttpSession httpSession);

    JqBadmintonUserVO updateUserInfo(BadmintonUser badmintonUser, HttpSession httpSession);

    boolean lockUsers(Integer[] ids, int lockStatus);

}
