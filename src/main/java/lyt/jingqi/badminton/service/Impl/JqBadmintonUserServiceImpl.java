package lyt.jingqi.badminton.service.Impl;

import lyt.jingqi.badminton.common.Constants;
import lyt.jingqi.badminton.common.ServiceResultEnum;
import lyt.jingqi.badminton.controller.vo.JqBadmintonUserVO;
import lyt.jingqi.badminton.dao.BadmintonUserMapper;

/////////////////
import lyt.jingqi.badminton.entity.BadmintonUser;
import lyt.jingqi.badminton.service.JqBadmintonUserService;
import lyt.jingqi.badminton.util.BeanUtil;
import lyt.jingqi.badminton.util.MD5Util;
import lyt.jingqi.badminton.util.PageQueryUtil;
import lyt.jingqi.badminton.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class JqBadmintonUserServiceImpl implements JqBadmintonUserService {

    @Autowired
    private BadmintonUserMapper badmintonUserMapper;

    @Override
    public PageResult getJqBadmintonUsersPage(PageQueryUtil pageQueryUtil) {
        List<BadmintonUser> badmintonUsers = badmintonUserMapper.findBadmintonUserList(pageQueryUtil);
        int tatal = badmintonUserMapper.getTotalBadmintonUsers(pageQueryUtil);
        PageResult pageResult = new PageResult(badmintonUsers, tatal, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public String register(String loginName, String password) {
        if (badmintonUserMapper.selectByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        BadmintonUser registerUser = new BadmintonUser();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        if (badmintonUserMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5, HttpSession httpSession) {
//        System.out.println("3...");
        BadmintonUser user = badmintonUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
        if (user != null && httpSession != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
            }
            //昵称太长 影响页面展示问题
            if (user.getNickName() != null && user.getNickName().length() > 7) {
                String tempNickName = user.getNickName().substring(0, 7) + "..";
                user.setNickName(tempNickName);
            }
            JqBadmintonUserVO jqBadmintonUserVo = new JqBadmintonUserVO();
            BeanUtil.copyProperties(user, jqBadmintonUserVo);
            //设置购物车中的数量
            httpSession.setAttribute(Constants.BADMINTON_USER_SESSION_KEY, jqBadmintonUserVo);
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public JqBadmintonUserVO updateUserInfo(BadmintonUser badmintonUser, HttpSession httpSession) {
        BadmintonUser user = badmintonUserMapper.selectByPrimaryKey(badmintonUser.getUserId());
        if (user != null) {
            user.setNickName(badmintonUser.getNickName());
            user.setAddress(badmintonUser.getAddress());
            user.setIntroduceSign(badmintonUser.getIntroduceSign());
            if (badmintonUserMapper.updateByPrimaryKeySelective(user) > 0) {
                JqBadmintonUserVO jqBadmintonUserVo = new JqBadmintonUserVO();
                user = badmintonUserMapper.selectByPrimaryKey(badmintonUser.getUserId());
                BeanUtil.copyProperties(user, jqBadmintonUserVo);
                httpSession.setAttribute(Constants.BADMINTON_USER_SESSION_KEY, jqBadmintonUserVo);
                return jqBadmintonUserVo;
            }
        }
        return null;
    }

    @Override
    public boolean lockUsers(Integer[] ids, int lockStatus) {
        if (ids.length < 1) {
            return false;
        }
        return badmintonUserMapper.lockUserBatch(ids, lockStatus) > 0;
    }
}
