package com.dance.manage.demo;

import com.dance.manage.bean.user.UserInfo;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

/**
 * <p>名称</p>
 * <p/>
 * <p>wikiURL</p>
 *
 * @author zb.jiang
 * @version 1.0
 * @Date 16/5/20
 */
@IocBean
public class HelloWorld{

    @Inject
    protected Dao dao;

    @At("/hello")
    @Ok("jsp:jsp.helloWorld")
    public String doHello()
    {
        System.out.println("=====dao:" + dao.count(UserInfo.class));
        return "Hello Nutz";
    }

}
