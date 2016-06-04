package com.dance.manage.bean.user;

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
 * @Date 16/5/29
 */
@Table("customerInfo")
public class CustomerInfo {

    @Id
    private int id;

    @Column("customerName")
    private String customerName;

    @Column("customerPhone")
    private String customerPhone;

    @Column("customerAddress")
    private String customerAddress;

    @Column("createTIme")
    private Date createTIme;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getCustomerPhone()
    {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone)
    {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress()
    {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress)
    {
        this.customerAddress = customerAddress;
    }

    public Date getCreateTIme()
    {
        return createTIme;
    }

    public void setCreateTIme(Date createTIme)
    {
        this.createTIme = createTIme;
    }
}
