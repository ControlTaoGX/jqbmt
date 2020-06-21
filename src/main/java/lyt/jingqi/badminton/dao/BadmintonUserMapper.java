package lyt.jingqi.badminton.dao;

import lyt.jingqi.badminton.entity.BadmintonUser;
import lyt.jingqi.badminton.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BadmintonUserMapper {

    int deleteByPrimaryKey(Long userId);

    int insert(BadmintonUser record);

    int insertSelective(BadmintonUser record);

    BadmintonUser selectByPrimaryKey(Long userId);

    BadmintonUser selectByLoginName(String loginName);

    BadmintonUser selectByLoginNameAndPasswd(@Param("loginName") String loginName, @Param("password") String password);

    int updateByPrimaryKeySelective(BadmintonUser record);

    int updateByPrimaryKey(BadmintonUser record);

    List<BadmintonUser> findBadmintonUserList(PageQueryUtil pageQueryUtil);

    int getTotalBadmintonUsers(PageQueryUtil pageQueryUtil);

    int lockUserBatch(@Param("ids") Integer[] ids, @Param("lockStatus") int lockStatus);
}
