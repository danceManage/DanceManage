package com.dance.manage.bean.sys;

/**
 * <p>名称</p>
 * <p/>
 * <p>wikiURL</p>
 *
 * @author zb.jiang
 * @version 1.0
 * @Date 16/6/10
 */
public enum SysLogEnum {

    SYSINSERT("INSERT","新增"),SYSEDIT("EDIT","编辑"),SYSDELETE("DELETE","删除");

    private String value;

    private String name;

    SysLogEnum( String value, String name )
    {
        this.value = value;
        this.name = name;
    }

    public static String getNameByValue( String value )
    {
        for ( SysLogEnum e : SysLogEnum.values() )
        {
            if ( e.getValue().equals( value ) )
            {
                return e.getName();
            }
        }

        return null;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
