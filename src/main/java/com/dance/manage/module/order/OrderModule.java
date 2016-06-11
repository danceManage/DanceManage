package com.dance.manage.module.order;

import com.dance.manage.bean.sys.SysLogEnum;
import com.dance.manage.bean.user.CustomerInfo;
import com.dance.manage.bean.order.OrderDetail;
import com.dance.manage.bean.order.OrderInfo;
import com.dance.manage.bean.user.SenderInfo;
import com.dance.manage.bean.user.UserInfo;
import com.dance.manage.module.sys.SystemLogger;
import com.dance.manage.utils.ExportExcelUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;
import org.nutz.dao.*;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

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
@At("/order")
@Fail("http:500")
@Ok("json:{locked:'password|salt',ignoreNull:true}")
@Filters(@By(type=CheckSession.class, args={UserInfo.USER_SESSION_ID, "/"}))
public class OrderModule {

    @Inject
    protected Dao dao;

    @At("/orderList")
    @Ok("re:jsp:jsp/order/orderList")
    @Filters
    public String productList(@Param("startDate")String startDate,@Param("endDate")String endDate,
                              @Param("orderNo")String orderNo,@Param("customerId") Integer customerId,@Param("page")Integer page,
                              @Param("pageSize")Integer pageSize, HttpServletRequest request)
    {
        if (page == null)
        {
            page = 1;
        }
        if(pageSize == null)
        {
            pageSize = 10;
        }

        int startPos = (page - 1) * pageSize;

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("select odf.id as orderId,odf.orderNo,odf.orderDate,odf.senderDate,odf.totalMoney,sd.userName as sendName,cm.customerName,us.userName \n");
        sqlBuffer.append("from orderinfo odf \n");
        sqlBuffer.append("left join sendInfo sd \n");
        sqlBuffer.append("on odf.senderId = sd.id \n");
        sqlBuffer.append("left join customerInfo cm \n");
        sqlBuffer.append("on odf.customerId = cm.id \n");
        sqlBuffer.append("left join userinfo us \n");
        sqlBuffer.append("on odf.operatorId = us.id \n");
        sqlBuffer.append("where 1=1 \n");
        sqlBuffer.append("and odf.isEnabled = 1 \n");
        if(StringUtils.isNotBlank(startDate))
        {
            sqlBuffer.append("and odf.orderDate >= '" + startDate + "' \n");
        }
        if(StringUtils.isNotBlank(endDate))
        {
            sqlBuffer.append("and odf.orderDate <= '" + endDate + "' \n");
        }
        if(StringUtils.isNotBlank(orderNo))
        {
            sqlBuffer.append("and odf.orderNo like '%" + orderNo + "%' \n");
        }
        if(customerId != null)
        {
            sqlBuffer.append("and odf.customerId = " + customerId + "\n");
        }
        sqlBuffer.append("order by odf.orderDate desc,odf.createTime desc \n");
        sqlBuffer.append("limit " + startPos + "," + pageSize + "\n");
        Sql sql = Sqls.create(sqlBuffer.toString());
        sql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException
            {
                List<Map> list = new LinkedList<Map>();
                while (rs.next())
                {
                    Map map = new HashMap();
                    map.put("orderId",rs.getInt("orderId"));
                    map.put("orderNo",rs.getString("orderNo"));
                    map.put("totalMoney",rs.getString("totalMoney"));
                    map.put("orderDate", rs.getDate("orderDate"));
                    map.put("senderDate", rs.getDate("senderDate"));
                    map.put("sendName", rs.getString("sendName"));
                    map.put("customerName", rs.getString("customerName"));
                    map.put("userName", rs.getString("userName"));
                    list.add(map);
                }
                return list;
            }
        });
        dao.execute(sql);
        int total =  dao.count(OrderInfo.class,null);
        List<Map> mapList = sql.getList(Map.class);
        List<CustomerInfo> customerInfoList = dao.query(CustomerInfo.class,null);

        request.setAttribute("customerInfoList",customerInfoList);
        request.setAttribute("total",total);
        request.setAttribute("customerId",customerId);
        request.setAttribute("page",page);
        request.setAttribute("startDate",startDate);
        request.setAttribute("endDate",endDate);
        request.setAttribute("orderNo",orderNo);
        request.setAttribute("orderList",mapList);
        return null;
    }

    @At("/addOrder")
    @Ok("re:jsp:jsp/order/addOrder")
    public String addOrder(@Param("id")Integer id,HttpServletRequest request)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String orderNo = "DD" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String orderDate = "";
        String sendDate = "";
        if(id != null)
        {
            OrderInfo orderInfo = dao.fetch(OrderInfo.class, Cnd.where("id", "=", id));
            orderNo = orderInfo.getOrderNo();
            orderDate = format.format(orderInfo.getOrderDate());
            sendDate = format.format(orderInfo.getSenderDate());

            if(orderInfo.getOperatorId() != null)
            {
                UserInfo userInfo = dao.fetch(UserInfo.class, Cnd.where("id","=",orderInfo.getOperatorId()));
                request.setAttribute("operatorName",userInfo.getUserName());
            }
            if(orderInfo.getCustomerId() != null)
            {
                CustomerInfo customerInfo = dao.fetch(CustomerInfo.class,Cnd.where("id","=",orderInfo.getCustomerId()));
                request.setAttribute("customerAddress",customerInfo.getCustomerAddress());
                request.setAttribute("customerPhone",customerInfo.getCustomerPhone());
            }

            List<OrderDetail> orderDetailList = dao.query(OrderDetail.class, Cnd.where("orderId","=",orderInfo.getId()));
            request.setAttribute("orderDetailList",orderDetailList);

            request.setAttribute("orderInfo",orderInfo);
        }
        else
        {
            orderDate = format.format(new Date());
            sendDate = format.format(new Date());
        }

        List<SenderInfo> senderInfoList = dao.query(SenderInfo.class,null);
        List<CustomerInfo> customerInfoList = dao.query(CustomerInfo.class,null);

        request.setAttribute("customerInfoList",customerInfoList);
        request.setAttribute("senderInfoList",senderInfoList);
        request.setAttribute("orderDate",orderDate);
        request.setAttribute("sendDate",sendDate);
        request.setAttribute("orderNo",orderNo);
        return null;
    }

    @AdaptBy(type=JsonAdaptor.class)
    @At("/saveOrder")
    @Ok("json:{locked:'password|salt'}")
    public Object saveOrder(@Param("..")OrderInfo orderInfo,HttpSession session) {
        NutMap re = new NutMap();
        if(StringUtils.isBlank(String.valueOf(orderInfo.getId())) || orderInfo.getId() == 0)
        {
            OrderInfo orderInfo1 = new OrderInfo();
            orderInfo1.setTotalMoney(orderInfo.getTotalMoney());
            orderInfo1.setCreateTime(new Date());
            orderInfo1.setCustomerId(orderInfo.getCustomerId());
            orderInfo1.setOperatorId(orderInfo.getOperatorId());
            orderInfo1.setOrderDate(orderInfo.getOrderDate());
            orderInfo1.setOrderNo(orderInfo.getOrderNo());
            orderInfo1.setSenderDate(orderInfo.getSenderDate());
            orderInfo1.setSenderId(orderInfo.getSenderId());
            orderInfo1.setState(1);
            orderInfo1.setIsEnabled(1);
            dao.insert(orderInfo1);

            UserInfo userInfo = (UserInfo) session.getAttribute(UserInfo.USER_SESSION_ID);
            if(userInfo != null)
            {
                SystemLogger.getInstance().logger(userInfo.getId(), SysLogEnum.SYSINSERT.getName(), "添加订单[" + new JSONObject(orderInfo1) + "]");
            }

            re.put("id", orderInfo1.getId());
            re.put("ok", true);
        }
        else
        {
            OrderInfo orderInfo1 = dao.fetch(OrderInfo.class,Cnd.where("id", "=", orderInfo.getId()));
            orderInfo1.setTotalMoney(orderInfo.getTotalMoney());
            orderInfo1.setCustomerId(orderInfo.getCustomerId());
            orderInfo1.setOperatorId(orderInfo.getOperatorId());
            orderInfo1.setOrderDate(orderInfo.getOrderDate());
            orderInfo1.setSenderDate(orderInfo.getSenderDate());
            orderInfo1.setSenderId(orderInfo.getSenderId());
            dao.update(orderInfo1);
            UserInfo userInfo = (UserInfo) session.getAttribute(UserInfo.USER_SESSION_ID);
            if(userInfo != null)
            {
                SystemLogger.getInstance().logger(userInfo.getId(), SysLogEnum.SYSEDIT.getName(), "修改订单[" + new JSONObject(orderInfo1) + "]");
            }
            re.put("id", orderInfo1.getId());
            re.put("ok", true);
        }
        return re;
    }

    @AdaptBy(type=JsonAdaptor.class)
    @At("/saveOrderDetail")
    @Ok("json:{locked:'password|salt'}")
    public Object saveOrderDetail(@Param("..")OrderDetail orderDetail,HttpSession session) {
        NutMap re = new NutMap();
        if(StringUtils.isBlank(String.valueOf(orderDetail.getId())) || orderDetail.getId() == 0)
        {
            OrderDetail orderDetail1 = new OrderDetail();
            orderDetail1.setUnit(orderDetail.getUnit());
            orderDetail1.setProductName(orderDetail.getProductName());
            orderDetail1.setPrice(orderDetail.getPrice());
            orderDetail1.setAmount(orderDetail.getAmount());
            orderDetail1.setOrderId(orderDetail.getOrderId());
            orderDetail1.setTotalMoney(orderDetail.getTotalMoney());
            orderDetail1.setIsEnabled(1);
            dao.insert(orderDetail1);

            UserInfo userInfo = (UserInfo) session.getAttribute(UserInfo.USER_SESSION_ID);
            if(userInfo != null)
            {
                SystemLogger.getInstance().logger(userInfo.getId(), SysLogEnum.SYSINSERT.getName(), "添加订单明细[" + new JSONObject(orderDetail1) + "]");
            }
            re.put("ok", true);
        }
        else
        {
            OrderDetail orderDetail1 = dao.fetch(OrderDetail.class,Cnd.where("id","=",orderDetail.getId()));
            orderDetail1.setUnit(orderDetail.getUnit());
            orderDetail1.setProductName(orderDetail.getProductName());
            orderDetail1.setPrice(orderDetail.getPrice());
            orderDetail1.setAmount(orderDetail.getAmount());
            orderDetail1.setTotalMoney(orderDetail.getTotalMoney());
            dao.update(orderDetail1);

            UserInfo userInfo = (UserInfo) session.getAttribute(UserInfo.USER_SESSION_ID);
            if(userInfo != null)
            {
                SystemLogger.getInstance().logger(userInfo.getId(), SysLogEnum.SYSEDIT.getName(), "修改订单明细[" + new JSONObject(orderDetail1) + "]");
            }
            re.put("ok", true);
        }
        return re;
    }

    @AdaptBy(type=JsonAdaptor.class)
    @At("/delOrderDetail")
    @Ok("json:{locked:'password|salt'}")
    public Object delOrderDetail(@Param("..")OrderDetail orderDetail,HttpSession session) {
        NutMap re = new NutMap();
        OrderDetail orderDetail1 = dao.fetch(OrderDetail.class,Cnd.where("id","=",orderDetail.getId()));
        if(orderDetail1 != null)
        {
            orderDetail1.setIsEnabled(0);
            //dao.delete(orderDetail1);
            dao.update(orderDetail1);
            re.put("ok", true);

            UserInfo userInfo = (UserInfo) session.getAttribute(UserInfo.USER_SESSION_ID);
            if(userInfo != null)
            {
                SystemLogger.getInstance().logger(userInfo.getId(), SysLogEnum.SYSDELETE.getName(), "删除订单明细[" + new JSONObject(orderDetail1) + "]");
            }
        }
        else
        {
            re.put("ok", false);
        }
        return re;
    }

    @AdaptBy(type=JsonAdaptor.class)
    @At("/delOrder")
    @Ok("json:{locked:'password|salt'}")
    public Object delOrder(@Param("..")OrderInfo orderInfo,HttpSession session) {
        NutMap re = new NutMap();
        OrderInfo orderInfo1 = dao.fetch(OrderInfo.class,Cnd.where("id","=",orderInfo.getId()));
        if(orderInfo1 != null)
        {

            dao.update(OrderDetail.class, org.nutz.dao.Chain.make("isEnabled",0),Cnd.where("orderId", "=" ,orderInfo.getId()));
            orderInfo1.setIsEnabled(0);
            dao.update(orderInfo1);
            //dao.clear("order_detail", Cnd.where("orderId", "=" ,orderInfo.getId()));
            //dao.delete(orderInfo1);
            re.put("ok", true);

            UserInfo userInfo = (UserInfo) session.getAttribute(UserInfo.USER_SESSION_ID);
            if(userInfo != null)
            {
                SystemLogger.getInstance().logger(userInfo.getId(), SysLogEnum.SYSDELETE.getName(), "删除订单[" + new JSONObject(orderInfo) + "]");
            }
        }
        else
        {
            re.put("ok", false);
        }
        return re;
    }

    @At("/excel/export")
    @Ok("raw")
    public File exportExcel(@Param("id")Integer id, HttpServletResponse response)
            throws Exception {
        String filePath = "";
        try{
            ResourceBundle bundle = PropertyResourceBundle.getBundle("runtime");
            if(bundle!=null){
                filePath = bundle.getString("filePath");
            }
        }catch (Exception e){
        }

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("select odf.id as orderId,odf.orderNo,odf.orderDate,odf.senderDate,odf.totalMoney," +
                "sd.userName as sendName,cm.customerPhone,cm.customerAddress,cm.customerName,us.userName \n");
        sqlBuffer.append("from orderinfo odf \n");
        sqlBuffer.append("left join sendInfo sd \n");
        sqlBuffer.append("on odf.senderId = sd.id \n");
        sqlBuffer.append("left join customerInfo cm \n");
        sqlBuffer.append("on odf.customerId = cm.id \n");
        sqlBuffer.append("left join userinfo us \n");
        sqlBuffer.append("on odf.operatorId = us.id \n");
        sqlBuffer.append("where 1=1 \n");
        sqlBuffer.append("and odf.id = " + id);
        Sql sql = Sqls.create(sqlBuffer.toString());
        sql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException
            {
                List<Map> list = new LinkedList<Map>();
                while (rs.next())
                {
                    Map map = new HashMap();
                    map.put("orderId", rs.getInt("orderId"));
                    map.put("orderNo",rs.getString("orderNo"));
                    map.put("totalMoney",rs.getDouble("totalMoney"));
                    map.put("orderDate", rs.getDate("orderDate"));
                    map.put("senderDate", rs.getDate("senderDate"));
                    map.put("sendName", rs.getString("sendName"));
                    map.put("customerName", rs.getString("customerName"));
                    map.put("userName", rs.getString("userName"));
                    map.put("customerPhone", rs.getString("customerPhone"));
                    map.put("customerAddress", rs.getString("customerAddress"));
                    list.add(map);
                }
                return list;
            }
        });
        dao.execute(sql);
        List<Map> mapList = sql.getList(Map.class);
        if(mapList != null && mapList.size() > 0)
        {
            if(!filePath.endsWith(File.separator))
            {
                filePath = filePath + File.separator;
            }
            List<OrderDetail> orderDetailList = dao.query(OrderDetail.class,Cnd.where("orderId","=",id));
            HSSFWorkbook wb = ExportExcelUtils.exportOrder(mapList.get(0),orderDetailList);

            String orderNo = (String) mapList.get(0).get("orderNo");
            String fileName = orderNo + ".xls";
            FileOutputStream fileOutputStream = new FileOutputStream(filePath + fileName);
            wb.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            File file = new File(filePath + fileName);
            return file;
        }
        else
        {
            return null;
        }
    }
}
