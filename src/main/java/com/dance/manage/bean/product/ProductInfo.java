package com.dance.manage.bean.product;

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
@Table("productInfo")
public class ProductInfo {

    @Id
    private int id;

    @Column("productName")
    private String productName;

    @Column("productShort")
    private String productShort;

    @Column("price")
    private double price;

    @Column("createTime")
    private Date createTime;

    @Column("unit")
    private String unit;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductShort()
    {
        return productShort;
    }

    public void setProductShort(String productShort)
    {
        this.productShort = productShort;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }
}
