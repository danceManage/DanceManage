package com.dance.manage.demo;

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
public class HelloWorld {

    @At("/hello")
    @Ok("jsp:jsp.helloWorld")
    public String doHello()
    {
        return "Hello Nutz";
    }

}
