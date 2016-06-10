package com.dance.manage.module.user;

import com.dance.manage.bean.user.CustomerInfo;
import com.dance.manage.bean.user.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>名称</p>
 * <p/>
 * <p>wikiURL</p>
 *
 * @author zb.jiang
 * @version 1.0
 * @Date 16/6/4
 */
@IocBean
@At("/customer")
@Fail("http:500")
@Ok("json:{locked:'password|salt',ignoreNull:true}")
@Filters(@By(type=CheckSession.class, args={UserInfo.USER_SESSION_ID, "/"}))
public class CustomerModule {

    @Inject
    protected Dao dao;

    @At("/customerList")
    @Ok("re:jsp:jsp/customer/customerList")
    public String customerList(HttpServletRequest request)
    {
        List<CustomerInfo> customerInfoList = dao.query(CustomerInfo.class,null);
        request.setAttribute("customerList",customerInfoList);
        return null;
    }

    @At("/addCustomer")
    @Ok("re:jsp:jsp/customer/addCustomer")
    public String addCustomer(@Param("id")Integer id,HttpServletRequest request)
    {
        if(id != null)
        {
            CustomerInfo customerInfo = dao.fetch(CustomerInfo.class,Cnd.where("id","=",id));
            request.setAttribute("customerInfo",customerInfo);
        }
        return null;
    }

    @AdaptBy(type=JsonAdaptor.class)
    @At("/saveCustomer")
    @Ok("json:{locked:'password|salt'}")
    public Object saveCustomer(@Param("..")CustomerInfo customerInfo) {
        NutMap re = new NutMap();
        if(StringUtils.isBlank(String.valueOf(customerInfo.getId())) || customerInfo.getId() == 0)
        {
            CustomerInfo customerInfo1 = dao.fetch(CustomerInfo.class, Cnd.where("customerName", "=", customerInfo.getCustomerName()));
            if(customerInfo1 != null)
            {
                re.put("ok", false);
                re.put("errorMsg", "已存在[" + customerInfo.getCustomerName() + "]的用户");
            }
            else
            {
                CustomerInfo customerInfo2 = new CustomerInfo();
                customerInfo2.setCustomerName(customerInfo.getCustomerName());
                customerInfo2.setCustomerAddress(customerInfo.getCustomerAddress());
                customerInfo2.setCustomerPhone(customerInfo.getCustomerPhone());
                customerInfo2.setCreateTIme(new Date());
                dao.insert(customerInfo2);
                re.put("ok", true);
            }
        }
        else
        {
            CustomerInfo customerInfo1 = dao.fetch(CustomerInfo.class, Cnd.where("id", "=", customerInfo.getId()));
            customerInfo1.setCustomerName(customerInfo.getCustomerName());
            customerInfo1.setCustomerAddress(customerInfo.getCustomerAddress());
            customerInfo1.setCustomerPhone(customerInfo.getCustomerPhone());
            dao.update(customerInfo1);
            re.put("ok", true);
        }
        return re;
    }

    @AdaptBy(type=JsonAdaptor.class)
    @At("/delCustomer")
    @Ok("json:{locked:'password|salt'}")
    public Object delCustomer(@Param("..")CustomerInfo customerInfo) {
        NutMap re = new NutMap();
        CustomerInfo customerInfo1 = dao.fetch(CustomerInfo.class,Cnd.where("id","=",customerInfo.getId()));
        if(customerInfo1 != null)
        {
            dao.delete(customerInfo1);
            re.put("ok", true);
        }
        else
        {
            re.put("ok", false);
        }
        return re;
    }
}
