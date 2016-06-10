package com.dance.manage.module.user;

import com.dance.manage.bean.user.SenderInfo;
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
@At("/sender")
@Fail("http:500")
@Ok("json:{locked:'password|salt',ignoreNull:true}")
@Filters(@By(type=CheckSession.class, args={UserInfo.USER_SESSION_ID, "/"}))
public class SenderInfoModule {

    @Inject
    protected Dao dao;

    @At("/addSender")
    @Ok("re:jsp:jsp/sender/addSender")
    public String addSender(@Param("id")Integer id,HttpServletRequest request)
    {
        if(id != null)
        {
            SenderInfo senderInfo = dao.fetch(SenderInfo.class, Cnd.where("id", "=", id));
            request.setAttribute("senderInfo",senderInfo);
        }
        return null;
    }

    @AdaptBy(type=JsonAdaptor.class)
    @At("/saveSender")
    @Ok("json:{locked:'password|salt'}")
    public Object saveSender(@Param("..")SenderInfo senderInfo) {
        NutMap re = new NutMap();
        if(StringUtils.isBlank(String.valueOf(senderInfo.getId())) || senderInfo.getId() == 0)
        {
            SenderInfo senderInfo2 = dao.fetch(SenderInfo.class, Cnd.where("userName", "=", senderInfo.getUserName()));
            if(senderInfo2 != null)
            {
                re.put("ok", false);
                re.put("errorMsg", "已存在[" + senderInfo.getUserName() + "]的送货员");
            }
            else
            {
                SenderInfo senderInfo1 = new SenderInfo();
                senderInfo1.setPhone(senderInfo.getPhone());
                senderInfo1.setUserName(senderInfo.getUserName());
                senderInfo1.setCreateTime(new Date());
                dao.insert(senderInfo1);
                re.put("ok", true);
            }
        }
        else
        {
            SenderInfo senderInfo2 = dao.fetch(SenderInfo.class, Cnd.where("id", "=", senderInfo.getId()));
            senderInfo2.setPhone(senderInfo.getPhone());
            senderInfo2.setUserName(senderInfo.getUserName());
            dao.update(senderInfo2);
            re.put("ok", true);
        }
        return re;
    }

    @At("/senderList")
    @Ok("re:jsp:jsp/sender/senderList")
    public String senderInfoList(HttpServletRequest request)
    {
        List<SenderInfo> senderInfoList = dao.query(SenderInfo.class,null);
        request.setAttribute("senderInfoList",senderInfoList);
        return null;
    }

    @AdaptBy(type=JsonAdaptor.class)
    @At("/delSender")
    @Ok("json:{locked:'password|salt'}")
    public Object delSender(@Param("..")SenderInfo senderInfo) {
        NutMap re = new NutMap();
        SenderInfo senderInfo1 = dao.fetch(SenderInfo.class,Cnd.where("id","=",senderInfo.getId()));
        if(senderInfo1 != null)
        {
            dao.delete(senderInfo1);
            re.put("ok", true);
        }
        else
        {
            re.put("ok", false);
        }
        return re;
    }
}
