<%--
  Created by IntelliJ IDEA.
  User: 哈哈哈
  Date: 2025/9/26
  Time: 10:00
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="entity.cy_customer" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>商品零售</title>
    <link href="css/style.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="js/jquery.js"></script>

    <style>
        .red { color: red; }
        .black { color: black; }
        .notice { padding: 10px 15px; border-radius: 4px; margin-bottom: 15px; }
        .notice.success { background: #e6ffed; border: 1px solid #52c41a; color: #237804; }
        .notice.error { background: #fff1f0; border: 1px solid #ff4d4f; color: #a8071a; }
    </style>

    <script type="text/javascript">
        window.onload = function() {
            document.getElementById("txm").focus();
        }
        function qk(role) {
            if (confirm('确定要清空当前顾客的清单吗？')) {
                window.location.href = 'cy_ClearCart?role=' + role;
            }
        }
        function reg(role) {
            if (confirm('确认结算当前清单吗？')) {
                window.location.href = 'cy_Checkout?role=' + role;
            }
        }
        function updateQty(id, val, role) {
            window.location.href = 'cy_UpdateSl?id=' + id + '&sl=' + val + '&role=' + role;
        }
    </script>
</head>

<body>
<%
    String selectedCustomer = (String) session.getAttribute("role");
    if(selectedCustomer == null || selectedCustomer.isEmpty()) {
        selectedCustomer = "customer1";
    }
    Double ZK = 1.0;
    String hyphone = "";
    String jb = "";
    String memberName = ""; // 會員名字
    HashMap<String, String[]> map = (HashMap<String, String[]>) session.getAttribute("map");
    if(map != null) {
        String[] info = map.get(selectedCustomer);
        if(info != null) {
            hyphone = info[0];
            String zk = info[1];
            jb = info[2];
            // 獲取會員名字（如果存在）
            if (info.length > 3) {
                memberName = info[3] != null ? info[3] : "";
            }
            try {
                ZK = Double.parseDouble(zk);
            } catch (NumberFormatException e) {
                ZK = 1.0;
            }
        }
    }

    Double sumMoneyAttr = (Double) session.getAttribute("SumMoney");
    double sumMoney = sumMoneyAttr != null ? sumMoneyAttr : 0.0;
    sumMoney = sumMoney * ZK;
%>

<div class="place">
    <span>位置：</span>
    <ul class="placeul">
        <%
            // 從request中獲取所有顧客列表
            List<cy_customer> allCustomers = (List<cy_customer>) request.getAttribute("allCustomers");
            if (allCustomers == null || allCustomers.isEmpty()) {
                // 如果沒有傳遞顧客列表，使用默認的customer1-4作為備用
                %>
                <li><a href="cy_QuerySpls?role=customer1" class="<%= "customer1".equals(selectedCustomer) ? "red" : "black" %>">顾客1</a></li>
                <li><a href="cy_QuerySpls?role=customer2" class="<%= "customer2".equals(selectedCustomer) ? "red" : "black" %>">顾客2</a></li>
                <li><a href="cy_QuerySpls?role=customer3" class="<%= "customer3".equals(selectedCustomer) ? "red" : "black" %>">顾客3</a></li>
                <li><a href="cy_QuerySpls?role=customer4" class="<%= "customer4".equals(selectedCustomer) ? "red" : "black" %>">顾客4</a></li>
                <%
            } else {
                // 獲取顯示名稱映射（如果顧客有關聯會員，顯示會員名稱）
                Map<String, String> displayNames = (Map<String, String>) request.getAttribute("customerDisplayNames");
                
                // 動態顯示所有顧客（如果關聯了會員，顯示會員名稱）
                for (cy_customer cust : allCustomers) {
                    String customerCode = cust.getCustomerCode();
                    String displayName;
                    
                    // 優先使用預先查詢好的顯示名稱（會員名稱或顧客名稱）
                    if (displayNames != null && displayNames.containsKey(customerCode)) {
                        displayName = displayNames.get(customerCode);
                    } else {
                        // 備用方案：直接使用顧客名稱
                        displayName = cust.getCustomerName() != null && !cust.getCustomerName().isEmpty() 
                                ? cust.getCustomerName() 
                                : customerCode;
                    }
                    
                    String cssClass = customerCode.equals(selectedCustomer) ? "red" : "black";
                    %>
                    <li><a href="cy_QuerySpls?role=<%= customerCode %>" class="<%= cssClass %>"><%= displayName %></a></li>
                    <%
                }
            }
        %>
    </ul>
</div>

<div class="rightinfo">
    <div class="tools" style="overflow:hidden;">
        <form action="cy_QuerySpls" method="post" id="memberPhoneForm">
            <input type="hidden" name="role" value="<%= selectedCustomer %>"/>
            会员电话：
            <input name="hyphone" id="hyphone" value="<%= hyphone != null ? hyphone : "" %>" type="text"
                   class="dfinput" style="margin-left:0;margin-right:-10px;width:150px;"
                   placeholder="请输入会员电话"/>
            <input type="submit" class="btn" value="查询会员" style="margin-left:5px;"/>
        </form>
        
        <form action="cy_Spls?role=<%= selectedCustomer %>" method="post" style="margin-top:10px;">
            <ul class="toolbar">
                <li>
                    <input name="txm" id="txm" type="text" class="dfinput" style="margin-left:0;margin-right:-10px;"/>
                </li>
            </ul>
            <div style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:10px;">
                <div style="display:flex;align-items:center;flex-wrap:wrap;gap:10px;">
                    <input type="submit" class="btn" value="扫描商品条形码"/>
                    <label>
                        会员电话：<%= hyphone != null && !hyphone.isEmpty() ? hyphone : "未输入" %>
                        <% if (memberName != null && !memberName.isEmpty()) { %>
                            <span style="color:#1c77ac; font-weight:bold;">（<%= memberName %>）</span>
                        <% } %>
                    </label>
                    <label style="color:red"><%= jb != null ? jb : "" %></label>
                </div>
                <div style="display:flex;align-items:center;gap:15px;">
                    <b><label style="font-size:16px;color:#333;">总金额：<span style="color:red;"><%= sumMoney %>￥</span></label></b>
                    <div style="display:flex;gap:8px;">
                        <a href="#" onclick="qk('<%= selectedCustomer %>'); return false;" style="display:inline-flex;align-items:center;gap:5px;padding:5px 12px;text-decoration:none;color:#555;border:1px solid #ddd;border-radius:4px;font-size:13px;background:#f5f5f5;">
                            <img src="images/t03.png" style="width:14px;height:14px;" />
                            清空列表
                        </a>
                        <a href="#" onclick="reg('<%= selectedCustomer %>'); return false;" style="display:inline-flex;align-items:center;gap:5px;padding:5px 12px;text-decoration:none;color:#fff;border:1px solid #2e8b57;border-radius:4px;font-size:13px;background:#2e8b57;">
                            <img src="images/t01.png" style="width:14px;height:14px;" />
                            商品结算
                        </a>
                    </div>
                </div>
            </div>
            <!--
            <ul class="toolbar1" style="list-style:none;padding:0;margin:0;display:flex;gap:10px;">
                <li><a href="#" onclick="qk('<%= selectedCustomer %>'); return false;" style="display:inline-flex;align-items:center;gap:5px;padding:6px 12px;text-decoration:none;color:#333;border:1px solid #ddd;border-radius:4px;font-size:13px;"><span><img src="images/t03.png" style="width:16px;height:16px;vertical-align:middle;" /></span>清空列表</a></li>
                <li><a href="#" onclick="reg('<%= selectedCustomer %>'); return false;" style="display:inline-flex;align-items:center;gap:5px;padding:6px 12px;text-decoration:none;color:#333;border:1px solid #ddd;border-radius:4px;font-size:13px;"><span><img src="images/t01.png" style="width:16px;height:16px;vertical-align:middle;" /></span>商品结算</a></li>
            </ul>
            -->
        </form>
    </div>

    <c:if test="${not empty flashMsg}">
        <div class="notice success">${flashMsg}</div>
    </c:if>
    <c:if test="${not empty flashError}">
        <div class="notice error">${flashError}</div>
    </c:if>

    <table class="tablelist">
        <thead>
        <tr>
            <th style="text-align:center;">编号</th>
            <th style="text-align:center;">名称</th>
            <th width="45" style="text-align:center;">单位</th>
            <th style="text-align:center;">条形码</th>
            <th style="text-align:center;">商品进价</th>
            <th style="text-align:center;">商品卖价</th>
            <th style="text-align:center;">商品折扣</th>
            <th style="text-align:center;">商品数量</th>
            <th style="text-align:center;width:150px">商品操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${somegoodsls}" var="goods">
            <tr>
                <td align="center">${goods.id }</td>
                <td align="center">${goods.name }</td>
                <td align="center">${goods.dw }</td>
                <td align="center">${goods.txm }</td>
                <td align="center">${goods.j_price }</td>
                <td align="center">${goods.m_price }</td>
                <td align="center"><%= ZK %></td>
                <td align="center">
                    <input size="4" type="text" name="sl" value="${goods.cartQuantity}"
                           onblur="updateQty('${goods.id}', this.value, '<%= selectedCustomer %>')"
                           style="border:1px solid #b6cad2;height:22px;" />
                </td>
                <td align="center">
                    <img src="images/t03.png" height="15" width="15"/>&nbsp;
                    <a href="cy_DeleteRk?role=<%= selectedCustomer %>&id=${goods.id}"
                       onclick="return confirm('确认删除该商品吗？');">删除</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script type="text/javascript">
    $('.tablelist tbody tr:odd').addClass('odd');
</script>
</body>
</html>

