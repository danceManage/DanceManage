package com.dance.manage.module.order;

import com.dance.manage.bean.user.CustomerInfo;
import com.dance.manage.bean.order.OrderDetail;
import com.dance.manage.bean.order.OrderInfo;
import com.dance.manage.bean.user.SenderInfo;
import com.dance.manage.bean.user.UserInfo;
import com.dance.manage.utils.ExportExcelUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class OrderModule {

    @Inject
    protected Dao dao;

    @At("/orderList")
    @Ok("re:jsp:jsp/order/orderList")
    @Filters
    public String productList(@Param("startDate")String startDate,@Param("endDate")String endDate,
                              @Param("orderNo")String orderNo,@Param("page")Integer page,
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
        sqlBuffer.append("select odf.id as orderId,odf.orderNo,odf.orderDate,odf.senderDate,sd.userName as sendName,cm.customerName,us.userName \n");
        sqlBuffer.append("from orderinfo odf \n");
        sqlBuffer.append("left join sendInfo sd \n");
        sqlBuffer.append("on odf.senderId = sd.id \n");
        sqlBuffer.append("left join customerInfo cm \n");
        sqlBuffer.append("on odf.customerId = cm.id \n");
        sqlBuffer.append("left join userinfo us \n");
        sqlBuffer.append("on odf.operatorId = us.id \n");
        sqlBuffer.append("where 1=1 \n");
        if(StringUtils.isNotBlank(startDate))
        {
            sqlBuffer.append("and odf.orderDate >= '" + startDate + "'");
        }
        if(StringUtils.isNotBlank(endDate))
        {
            sqlBuffer.append("and odf.orderDate <= '" + endDate + "'");
        }
        if(StringUtils.isNotBlank(orderNo))
        {
            sqlBuffer.append("and odf.orderNo like '%" + orderNo + "%'");
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
        request.setAttribute("total",total);
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
    public Object saveOrder(@Param("..")OrderInfo orderInfo) {
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
            dao.insert(orderInfo1);
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
            re.put("id", orderInfo1.getId());
            re.put("ok", true);
        }
        return re;
    }

    @AdaptBy(type=JsonAdaptor.class)
    @At("/saveOrderDetail")
    @Ok("json:{locked:'password|salt'}")
    public Object saveOrderDetail(@Param("..")OrderDetail orderDetail) {
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
            dao.insert(orderDetail1);
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
            re.put("ok", true);
        }
        return re;
    }

    @AdaptBy(type=JsonAdaptor.class)
    @At("/delOrderDetail")
    @Ok("json:{locked:'password|salt'}")
    public Object delOrderDetail(@Param("..")OrderDetail orderDetail) {
        NutMap re = new NutMap();
        OrderDetail orderDetail1 = dao.fetch(OrderDetail.class,Cnd.where("id","=",orderDetail.getId()));
        if(orderDetail1 != null)
        {
            dao.delete(orderDetail1);
            re.put("ok", true);
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
    public Object delOrder(@Param("..")OrderInfo orderInfo) {
        NutMap re = new NutMap();
        OrderInfo orderInfo1 = dao.fetch(OrderInfo.class,Cnd.where("id","=",orderInfo.getId()));
        if(orderInfo1 != null)
        {

            dao.clear("order_detail", Cnd.where("orderId", "=" ,orderInfo.getId()));
            dao.delete(orderInfo1);
            re.put("ok", true);
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
        sqlBuffer.append("select odf.id as orderId,odf.orderNo,odf.orderDate,odf.senderDate," +
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
            String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xls";
            if(!filePath.endsWith(File.separator))
            {
                filePath = filePath + File.separator;
            }
            List<OrderDetail> orderDetailList = dao.query(OrderDetail.class,Cnd.where("orderId","=",id));
            HSSFWorkbook wb = ExportExcelUtils.exportOrder(mapList.get(0),orderDetailList);
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
