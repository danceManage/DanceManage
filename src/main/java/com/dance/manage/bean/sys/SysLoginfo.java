package com.dance.manage.bean.sys;

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
 * @Date 16/6/10
 */
@Table("sys_loginfo")
public class SysLoginfo {

    @Id
    private int id;

    @Column("operatorId")
    private int operatorId;

    @Column("type")
    private String type;

    @Column("comment")
    private String comment;

    @Column("createTime")
    private Date createTime;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId(int operatorId)
    {
        this.operatorId = operatorId;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
}
