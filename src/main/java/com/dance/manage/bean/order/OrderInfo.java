package com.dance.manage.bean.order;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 * <p>名称</p>
 * <p/>
 * <p>wikiURL</p>
 *
 * @author zb.jiang
 * @version 1.0
 * @Date 16/5/28
 */
@Table("orderInfo")
public class OrderInfo {

    @Id
    private int id;

    @Column("orderNo")
    private String orderNo;//订单号

    @Column("orderDate")
    private Date orderDate;//订单日期

    @Column("operatorId")
    private Integer operatorId;//操作人

    @Column("customerId")
    private Integer customerId;//客户名称

    @Column("senderId")
    private String senderId;//送货人

    @Column("senderDate")
    private Date senderDate;//送货日期

    @Column("createTime")
    private Date createTime;//创建日期

    @Column("totalMoney")
    private double totalMoney;//总价

    @Column("state")
    private int state;//状态;0:草稿;1:正常;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public Date getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(Date orderDate)
    {
        this.orderDate = orderDate;
    }

    public Integer getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId)
    {
        this.operatorId = operatorId;
    }

    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Integer customerId)
    {
        this.customerId = customerId;
    }

    public String getSenderId()
    {
        return senderId;
    }

    public void setSenderId(String senderId)
    {
        this.senderId = senderId;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public double getTotalMoney()
    {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney)
    {
        this.totalMoney = totalMoney;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public Date getSenderDate()
    {
        return senderDate;
    }

    public void setSenderDate(Date senderDate)
    {
        this.senderDate = senderDate;
    }
}
