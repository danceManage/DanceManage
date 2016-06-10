package com.dance.manage.module.product;
import com.dance.manage.bean.product.ProductInfo;
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
 * @Date 16/5/28
 */
@IocBean
@At("/product")
@Fail("http:500")
@Ok("json:{locked:'password|salt',ignoreNull:true}")
@Filters(@By(type=CheckSession.class, args={UserInfo.USER_SESSION_ID, "/"}))
public class ProductModule {

    @Inject
    protected Dao dao;

    @At("/productList")
    @Ok("re:jsp:jsp/product/productList")
    @Filters
    public String productList(@Param("order") Boolean order,HttpServletRequest request)
    {
        List<ProductInfo> productList = dao.query(ProductInfo.class,null);
        int total = dao.count(ProductInfo.class,null);
        request.setAttribute("productList",productList);
        request.setAttribute("order",order);
        request.setAttribute("total",total);
       return null;
    }

    @At("/addProduct")
    @Ok("re:jsp:jsp/product/addProduct")
    public String addProduct(@Param("id")Integer id,HttpServletRequest request)
    {
        if(id != null)
        {
            ProductInfo productInfo = dao.fetch(ProductInfo.class,Cnd.where("id","=",id));
            request.setAttribute("productInfo",productInfo);
        }
        return null;
    }

    @AdaptBy(type=JsonAdaptor.class)
    @At("/saveProduct")
    @Ok("json:{locked:'password|salt'}")
    public Object saveUser(@Param("..")ProductInfo product) {
        NutMap re = new NutMap();
        if(StringUtils.isBlank(String.valueOf(product.getId())) || product.getId() == 0)
        {
            ProductInfo productInfo = dao.fetch(ProductInfo.class, Cnd.where("productName", "=", product.getProductName()));
            if(productInfo != null)
            {
                re.put("ok", false);
                re.put("errorMsg", "已存在["+product.getProductName()+"]的品种");
            }
            else
            {
                ProductInfo productInfo1 = new ProductInfo();
                productInfo1.setPrice(product.getPrice());
                productInfo1.setProductName(product.getProductName());
                productInfo1.setProductShort(product.getProductShort());
                productInfo1.setUnit(product.getUnit());
                productInfo1.setCreateTime(new Date());
                dao.insert(productInfo1);
                re.put("ok", true);
            }
        }
        else
        {
            ProductInfo productInfo = dao.fetch(ProductInfo.class,Cnd.where("id","=",product.getId()));
            productInfo.setPrice(product.getPrice());
            productInfo.setProductName(product.getProductName());
            productInfo.setProductShort(product.getProductShort());
            productInfo.setUnit(product.getUnit());
            dao.update(productInfo);
            re.put("ok", true);
        }
        return re;
    }


    @AdaptBy(type=JsonAdaptor.class)
    @At("/delProduct")
    @Ok("json:{locked:'password|salt'}")
    public Object delProduct(@Param("..")ProductInfo product) {
        NutMap re = new NutMap();
        ProductInfo productInfo = dao.fetch(ProductInfo.class,Cnd.where("id","=",product.getId()));
        if(productInfo != null)
        {
            dao.delete(productInfo);
            re.put("ok", true);
        }
        else
        {
            re.put("ok", false);
        }
        return re;
    }

}
