package com.dance.manage.module.user;

import com.dance.manage.bean.sys.SysLogEnum;
import com.dance.manage.module.sys.SystemLogger;
import com.dance.manage.bean.user.UserInfo;
import org.json.JSONObject;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * <p>名称</p>
 * <p/>
 * <p>wikiURL</p>
 *
 * @author zb.jiang
 * @version 1.0
 * @Date 16/5/23
 */
@IocBean
@At("/user")
@Fail("http:500")
@Ok("json:{locked:'password|salt',ignoreNull:true}")
@Filters(@By(type=CheckSession.class, args={UserInfo.USER_SESSION_ID, "/"}))
public class UserModule {

    @Inject
    protected Dao dao;

    @At("/logon")
    @Ok("re:jsp:jsp/index")
    @Filters
    public String login(String logonId,String password,HttpServletRequest request)
    {
        UserInfo userInfo = dao.fetch(UserInfo.class, Cnd.where("logonId", "=", logonId).and("password", "=", password));

        if(userInfo != null)
        {
            request.getSession().setAttribute(UserInfo.USER_SESSION_ID,userInfo);
            return "redirect:/user/index.do";
        }
        else
        {
            request.setAttribute("errorMsg", "用户名或密码错误");
            return "jsp:/logon";
        }
    }

    @At("/index")
    @Ok("re:jsp:jsp/index")
    public String toIndex()
    {
        return null;
    }

    @At("/addUser")
    @Ok("re:jsp:jsp/user/addUser")
    public String addUser()
    {
        return null;
    }

    @AdaptBy(type=JsonAdaptor.class)
    @At("/saveUser")
    @Ok("json:{locked:'password|salt'}")
    public Object saveUser(@Param("..")UserInfo user) {
        NutMap re = new NutMap();
        UserInfo userInfo = dao.fetch(UserInfo.class,Cnd.where("logonId","=",user.getLogonId()));
        if(userInfo != null)
        {
            re.put("ok", false);
            re.put("errorMsg", "已存在["+user.getLogonId()+"]的用户");
        }
        else
        {
            UserInfo userInfo1 = new UserInfo();
            userInfo1.setLogonId(user.getLogonId());
            userInfo1.setUserName(user.getUserName());
            userInfo1.setPassword(user.getPassword());
            userInfo1.setCreateTime(new Date());
            userInfo1.setState(0);
            dao.insert(userInfo1);
            re.put("ok", true);

            SystemLogger.getInstance().logger(userInfo1.getId(), SysLogEnum.SYSINSERT.getName(), "添加用户[" + new JSONObject(user) + "]");
        }
        return re;
    }

    @At("/userList")
    @Ok("re:jsp:jsp/user/userList")
    public String userList(HttpServletRequest request)
    {
        List<UserInfo> userInfoList = dao.query(UserInfo.class,null);
        request.setAttribute("userInfoList",userInfoList);
        return null;
    }

    @AdaptBy(type=JsonAdaptor.class)
    @At("/delUser")
    @Ok("json:{locked:'password|salt'}")
    public Object delUser(@Param("..")UserInfo user) {
        NutMap re = new NutMap();
        UserInfo userInfo = dao.fetch(UserInfo.class,Cnd.where("id","=",user.getId()));
        if(userInfo != null)
        {
            dao.delete(userInfo);
            SystemLogger.getInstance().logger(userInfo.getId(), SysLogEnum.SYSDELETE.getName(), "删除用户[" + new JSONObject(user) + "]");
            re.put("ok", true);
        }
        else
        {
            re.put("ok", false);
        }
        return re;
    }

    @At("/logout")
    @Ok("re:jsp:jsp/index")
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/user/index.do";
    }
}
