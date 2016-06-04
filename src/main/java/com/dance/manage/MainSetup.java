package com.dance.manage;

import com.dance.manage.bean.user.UserInfo;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import java.util.Date;

/**
 * <p>名称</p>
 * <p/>
 * <p>wikiURL</p>
 *
 * @author zb.jiang
 * @version 1.0
 * @Date 16/5/23
 */
public class MainSetup implements Setup{
    @Override
    public void init(NutConfig nutConfig)
    {
        Ioc ioc = nutConfig.getIoc();
        Dao dao = ioc.get(Dao.class);
        Daos.createTablesInPackage(dao, "com.dance.manage", false);
    }

    @Override
    public void destroy(NutConfig nutConfig)
    {

    }
}
