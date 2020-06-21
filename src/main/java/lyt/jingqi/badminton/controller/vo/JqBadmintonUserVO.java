package lyt.jingqi.badminton.controller.vo;

import java.io.Serializable;

public class JqBadmintonUserVO implements Serializable {
    private long userId;
    private String nickName;
    private String loginName;
    private String introduceSign;
    private String address;
    private int shopCartItemCount;

    public long getUserId() {
        return userId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getIntroduceSign() {
        return introduceSign;
    }

    public String getAddress() {
        return address;
    }

    public int getShopCartItemCount() {
        return shopCartItemCount;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setIntroduceSign(String introduceSign) {
        this.introduceSign = introduceSign;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setShopCartItemCount(int shopCartItemCount) {
        this.shopCartItemCount = shopCartItemCount;
    }
}
