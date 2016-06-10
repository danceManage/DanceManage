package com.dance.manage.module.sys;

import com.dance.manage.bean.sys.SysLoginfo;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.mvc.Mvcs;

import java.util.Date;

/**
 * <p>名称</p>
 * <p/>
 * <p>wikiURL</p>
 *
 * @author zb.jiang
 * @version 1.0
 * @Date 16/6/10
 */
public class SystemLogger{

    @Inject
    private Dao dao;

    private static SystemLogger systemLogger;

    public SystemLogger (){};

    public static synchronized SystemLogger getInstance() {
        if (systemLogger == null)
        {
            systemLogger = new SystemLogger();
        }
        return systemLogger;
    }

    public void logger(Integer operatorId, String type, String content)
    {
        SysLoginfo sysLoginfo = new SysLoginfo();
        sysLoginfo.setOperatorId(operatorId);
        sysLoginfo.setType(type);
        sysLoginfo.setComment(content);
        sysLoginfo.setCreateTime(new Date());
        Mvcs.ctx().getDefaultIoc().get(Dao.class).insert(sysLoginfo);
    }
}
