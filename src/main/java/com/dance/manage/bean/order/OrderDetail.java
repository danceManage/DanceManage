package com.dance.manage.bean.order;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * <p>名称</p>
 * <p/>
 * <p>wikiURL</p>
 *
 * @author zb.jiang
 * @version 1.0
 * @Date 16/5/28
 */
@Table("order_detail")
public class OrderDetail {

    @Id
    private int id;

    @Column("orderId")
    private int orderId;//订单Id

    @Column("productName")
    private String productName;//品种

    @Column("price")
    private double price;//价格

    @Column("amount")
    private double amount;//数目

    @Column("unit")
    private String unit;//单位

    @Column("totalMoney")
    private double totalMoney;//单行总价

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public double getTotalMoney()
    {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney)
    {
        this.totalMoney = totalMoney;
    }
}
