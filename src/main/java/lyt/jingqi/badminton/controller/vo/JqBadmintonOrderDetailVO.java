package lyt.jingqi.badminton.controller.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单详情
 */
public class JqBadmintonOrderDetailVO implements Serializable {
    private String orderNo;
    private Integer totalPrice;
    private Byte payStatus;
    private String payStatusString;
    private Byte payType;
    private String payTypeString;
    private Date payTime;
    private Byte orderStatus;
    private String orderStatusString;
    private String userAddress;
    private Date createTime;
    private List<JqBadmintonOrderItemVO> jqBadmintonOrderItemVOS;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Byte getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayStatusString() {
        return payStatusString;
    }

    public void setPayStatusString(String payStatusString) {
        this.payStatusString = payStatusString;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public String getPayTypeString() {
        return payTypeString;
    }

    public void setPayTypeString(String payTypeString) {
        this.payTypeString = payTypeString;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusString() {
        return orderStatusString;
    }

    public void setOrderStatusString(String orderStatusString) {
        this.orderStatusString = orderStatusString;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<JqBadmintonOrderItemVO> getJqBadmintonOrderItemVOS() {
        return jqBadmintonOrderItemVOS;
    }

    public void setJqBadmintonOrderItemVOS(List<JqBadmintonOrderItemVO> jqBadmintonOrderItemVOS) {
        this.jqBadmintonOrderItemVOS = jqBadmintonOrderItemVOS;
    }
}
