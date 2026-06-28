<%--
  Created by IntelliJ IDEA.
  User: 哈哈哈
  Date: 2025/9/26
  Time: 10:00
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  // 页面加载时自动查询商品数据
  if (request.getAttribute("someGoods") == null) {
    response.sendRedirect("cy_QueryHy_Page3?pageNow=1");
    return;
  }
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>商品信息列表</title>
  <script type="text/javascript" src="js/jquery.js"></script>
  <script type="text/javascript">
    $(document).ready(function(){
      // 表格隔行变色
      $('.tablelist tbody tr:odd').addClass('odd');
      // 提示框控制
      $(".click").click(function(){
        $(".tip").fadeIn(200);
      });
      $(".tiptop a, .sure, .cancel").click(function(){
        $(".tip").fadeOut(100);
      });

      // 表单提交验证（条形码数字校验、价格正数校验）
      $("#searchForm").submit(function(e){
        var barCode = $("#barCode").val().trim();
        var minPrice = $("#minPrice").val().trim();
        var maxPrice = $("#maxPrice").val().trim();

        // 条形码仅允许数字
        if(barCode && !/^\d+$/.test(barCode)){
          alert("条形码仅支持数字！");
          return false;
        }
        // 价格需为正数（若输入）
        if((minPrice && isNaN(minPrice)) || minPrice < 0){
          alert("最低卖价请输入非负数字！");
          return false;
        }
        if((maxPrice && isNaN(maxPrice)) || maxPrice < 0){
          alert("最高卖价请输入非负数字！");
          return false;
        }
        // 最低价格不大于最高价格
        if(minPrice && maxPrice && parseFloat(minPrice) > parseFloat(maxPrice)){
          alert("最低卖价不能大于最高卖价！");
          return false;
        }
        return true;
      });
    });

    // 分页跳转：携带商品查询条件
    function goPage(page) {
      var totalPages = ${page.pageCount};
      if (page < 1) page = 1;
      if (page > totalPages) page = totalPages;

      // 获取商品查询条件（与表单参数名对应）
      var goodsName = $("#goodsName").val().trim();
      var barCode = $("#barCode").val().trim();
      var minPrice = $("#minPrice").val().trim();
      var maxPrice = $("#maxPrice").val().trim();
      var stockStatus = $("#stockStatus").val();

      // 拼接分页URL（中文编码、条件筛选）
      var url = "cy_QueryHy_Page3?pageNow=" + page;
      if(goodsName) url += "&goodsName=" + encodeURIComponent(goodsName);
      if(barCode) url += "&txm=" + barCode;
      if(minPrice) url += "&minPrice=" + minPrice;
      if(maxPrice) url += "&maxPrice=" + maxPrice;
      if(stockStatus && stockStatus !== "all") url += "&stockStatus=" + stockStatus;

      window.location.href = url;
    }

    // 输入框跳转：复用分页逻辑
    function jumpToPage() {
      var pageInput = document.getElementById("jumpPage");
      var page = pageInput.value.trim();
      var totalPages = ${page.pageCount};

      if (/^\d+$/.test(page)) {
        page = parseInt(page);
        if (page >= 1 && page <= totalPages) {
          goPage(page);
        } else {
          alert("请输入1~" + totalPages + "之间的页码");
        }
      } else {
        alert("请输入有效的数字页码");
      }
      pageInput.focus();
    }

    // 删除商品确认（防止误操作）
    function deleteGoods(id) {
      if (confirm('确定要删除ID为【' + id + '】的商品吗？删除后关联销售数据可能异常！')) {
        window.location.href = "cy_DeleteGoods?id=" + id;
      }
    }
  </script>
  <style type="text/css">
    /* 基础样式：与会员页面保持风格统一 */
    body {
      font-family: "Microsoft YaHei", sans-serif;
      margin: 0;
      padding: 20px;
    }
    .place {
      margin-bottom: 20px;
      color: #666;
    }
    .placeul {
      list-style: none;
      display: inline-block;
      margin: 0;
      padding: 0;
    }
    .placeul li {
      float: left;
      margin: 0 5px;
    }
    .rightinfo {
      width: 100%;
      overflow: hidden;
    }
    /* 商品搜索表单样式：适配多条件布局 */
    .tools {
      margin-bottom: 20px;
      padding: 15px;
      background-color: #f5f9fc;
      border-radius: 4px;
    }
    .toolbar {
      list-style: none;
      padding: 0;
      margin: 0;
      display: flex;
      align-items: center;
      gap: 15px;
      flex-wrap: wrap; /* 自适应换行，避免小屏幕溢出 */
    }
    .toolbar li {
      float: left;
      display: flex;
      align-items: center;
      gap: 5px;
    }
    .dfinput, .select-input {
      padding: 6px 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
      width: 180px;
      box-sizing: border-box;
    }
    /* 价格输入框宽度适配 */
    .price-input {
      width: 120px;
    }
    /* 按钮样式：区分功能按钮 */
    .toolbar a, .search-btn {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: 5px;
      padding: 6px 15px;
      background-color: #2e8b57; /* 商品页面用深绿色主题，区别会员页面 */
      color: #fff;
      border: none;
      border-radius: 4px;
      text-decoration: none;
      cursor: pointer;
    }
    .toolbar a:hover, .search-btn:hover {
      background-color: #236b43;
    }
    .search-btn.reset {
      background-color: #666; /* 重置按钮灰色 */
    }

    /* 商品表格样式：适配多列布局 */
    .tablelist {
      width: 100%;
      border-collapse: collapse;
      margin-bottom: 20px;
    }
    .tablelist th, .tablelist td {
      border: 1px solid #ddd;
      padding: 8px 10px; /* 缩小内边距，避免列过宽 */
      text-align: center;
      font-size: 13px;
    }
    .tablelist th {
      background-color: #f5f5f5;
      font-weight: bold;
    }
    .tablelist .odd {
      background-color: #f9f9f9;
    }
    /* 库存预警样式：库存<=5时标红 */
    .stock-warning {
      color: #ff0000;
      font-weight: bold;
    }
    .tablelink {
      color: #2e8b57;
      text-decoration: none;
      margin: 0 5px;
    }
    .tablelink:hover {
      text-decoration: underline;
    }

    /* 分页控件样式：与会员页面一致 */
    .pagin {
      margin: 30px 0;
      text-align: center;
      overflow: hidden;
      border: 1px dashed #ccc;
      padding: 15px;
    }
    .message {
      margin-bottom: 15px;
      color: #333;
      font-size: 14px;
    }
    .paginList {
      list-style: none;
      padding: 0;
      margin: 0;
      display: inline-block;
    }
    .paginItem {
      float: left;
      margin: 0 5px;
    }
    .paginItem a {
      display: inline-block;
      padding: 6px 12px;
      border: 1px solid #2e8b57;
      color: #2e8b57;
      text-decoration: none;
      border-radius: 4px;
      font-size: 14px;
      background-color: #fff;
    }
    .paginItem.current a {
      background-color: #2e8b57;
      color: #fff;
    }
    .paginItem.disabled a {
      color: #ccc;
      border-color: #ccc;
      cursor: not-allowed;
      background-color: #f5f5f5;
    }
    .jump-box {
      display: inline-block;
      margin-left: 20px;
      font-size: 14px;
    }
    .jump-box input {
      width: 50px;
      padding: 6px;
      margin: 0 5px;
      border: 1px solid #ddd;
      text-align: center;
      border-radius: 4px;
    }

    /* 无数据提示样式 */
    .no-data {
      text-align: center;
      padding: 30px 0;
      color: #999;
    }

    /* 提示框样式：商品操作提示 */
    .tip {
      position: fixed;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      width: 300px;
      background: #fff;
      border: 1px solid #ddd;
      border-radius: 4px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      display: none;
    }
    .tiptop {
      padding: 10px 15px;
      border-bottom: 1px solid #ddd;
      background-color: #f5f5f5;
    }
    .tiptop a {
      float: right;
      color: #999;
      text-decoration: none;
    }
    .tipinfo {
      padding: 20px;
      display: flex;
      align-items: center;
      gap: 15px;
    }
    .tipright p {
      margin: 0 0 5px;
    }
    .tipright cite {
      color: #666;
      font-style: normal;
      font-size: 14px;
    }
    .tipbtn {
      padding: 10px 0;
      border-top: 1px solid #ddd;
      text-align: center;
    }
    .sure, .cancel {
      padding: 6px 15px;
      margin: 0 5px;
      border: none;
      border-radius: 4px;
      color: #fff;
      cursor: pointer;
    }
    .sure {
      background: #2e8b57;
    }
    .cancel {
      background: #666;
    }
  </style>
</head>
<body>
<div class="place">
  <span>位置：</span>
  <ul class="placeul">
    <li><a href="#">首页</a></li>
    <li><a href="#">商品管理</a></li>
    <li><a href="#">商品信息</a></li>
  </ul>
</div>
<div class="rightinfo">
  <!-- 商品搜索表单：多条件筛选（名称、条形码、价格区间、库存状态） -->
  <div class="tools">
    <form id="searchForm" action="cy_QueryHy_Page3" method="get">
      <ul class="toolbar">
        <!-- 1. 商品名称查询 -->
        <li>
          <label>商品名称：</label>
          <input type="text" name="goodsName" id="goodsName" class="dfinput"
                 value="${goodsName}" placeholder="请输入商品名称"/>
        </li>
        <!-- 2. 条形码查询（精确匹配数字） -->
        <li>
          <label>条形码：</label>
          <input type="text" name="txm" id="barCode" class="dfinput"
                 value="${txm}" placeholder="请输入商品条形码"/>
        </li>
        <!-- 3. 卖价区间查询 -->
        <li>
          <label>卖价区间：</label>
          <input type="number" name="minPrice" id="minPrice" class="dfinput price-input"
                 value="${minPrice}" placeholder="最低" step="0.01" min="0"/>
          <span>~</span>
          <input type="number" name="maxPrice" id="maxPrice" class="dfinput price-input"
                 value="${maxPrice}" placeholder="最高" step="0.01" min="0"/>
        </li>
        <!-- 4. 库存状态查询 -->
        <li>
          <label>库存状态：</label>
          <select name="stockStatus" id="stockStatus" class="select-input">
            <option value="all" ${empty stockStatus || stockStatus == 'all' ? 'selected' : ''}>全部库存</option>
            <option value="normal" ${stockStatus == 'normal' ? 'selected' : ''}>正常库存(>5)</option>
            <option value="warning" ${stockStatus == 'warning' ? 'selected' : ''}>库存预警(≤5)</option>
            <option value="out" ${stockStatus == 'out' ? 'selected' : ''}>库存为0</option>
          </select>
        </li>
        <!-- 功能按钮：查找、重置、导出 -->
        <li>
          <button type="submit" class="search-btn">
            查找
          </button>
        </li>
        <li>
          <button type="reset" class="search-btn reset">
            重置
          </button>
        </li>
        <!-- 导出功能已移除 -->
      </ul>
    </form>
  </div>

  <!-- 商品表格：包含所有要求字段，添加库存预警样式 -->
  <table class="tablelist">
    <thead>
    <tr>
      <th>序号</th>
      <th>ID</th>
      <th>商品名称</th>
      <th>条形码</th>
      <th>进价(元)</th>
      <th>卖价(元)</th>
      <th>折扣1(%)</th>
      <th>折扣2(%)</th>
      <th>库存</th>
      <th>商品描述</th>
      <th>进货时间</th>
      <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
      <c:when test="${not empty someGoods && someGoods.size() > 0}">
        <c:forEach items="${someGoods}" var="goods" varStatus="status">
          <tr>
            <!-- 序号：分页后连续计数 -->
            <td>${(page.pageNow - 1) * page.pageSize + status.index + 1}</td>
            <td>${goods.id}</td>
            <td>${goods.goodsName}</td>
            <td>${goods.barCode}</td>
            <td>${goods.j_price}</td>         <!-- 进价 -->
            <td>${goods.m_price}</td>         <!-- 卖价 -->
            <td>${goods.zk1}</td>             <!-- 折扣1（百分比） -->
            <td>${goods.zk2}</td>             <!-- 折扣2（百分比） -->
            <!-- 库存预警：<=5标红，=0显示"无货" -->
            <td>
              <c:choose>
                <c:when test="${goods.kc == 0}">
                  <span class="stock-warning">无货</span>
                </c:when>
                <c:when test="${goods.kc <= 5}">
                  <span class="stock-warning">${goods.kc}</span>
                </c:when>
                <c:otherwise>
                  ${goods.kc}
                </c:otherwise>
              </c:choose>
            </td>
            <!-- 商品描述：过长时显示省略号（CSS控制） -->
            <td style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 150px;"
                title="${goods.ms}">${goods.ms}</td>
            <td>${goods.dtime}</td> <!-- 进货时间 -->
            <!-- 操作按钮：删除 -->
            <td>
              <a href="javascript:deleteGoods(${goods.id})" class="tablelink">删除</a>
            </td>
          </tr>
        </c:forEach>
      </c:when>
      <c:otherwise>
        <tr>
          <td colspan="12" class="no-data">
            <c:choose>
              <c:when test="${(not empty goodsName) || (not empty txm) || (not empty minPrice) || (not empty maxPrice) || (not empty stockStatus && stockStatus != 'all')}">
                暂无匹配的商品数据，请调整查询条件
              </c:when>
              <c:otherwise>
                暂无商品数据，请点击【添加商品】录入数据
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </c:otherwise>
    </c:choose>
    </tbody>
  </table>

  <!-- 分页控件：与会员页面逻辑一致，携带商品查询条件 -->
  <div class="pagin">
    <div class="message">
      共<i class="blue">${page.rowCount}</i>条商品记录，共<i class="blue">${page.pageCount}</i>页，
      当前显示第<i class="blue">${page.pageNow}</i>页
    </div>

    <ul class="paginList">
      <!-- 首页 -->
      <li class="paginItem <c:if test='${page.pageNow == 1}'>disabled</c:if>">
        <a href="javascript:goPage(1)">首页</a>
      </li>
      <!-- 上一页 -->
      <li class="paginItem <c:if test='${page.pageNow == 1}'>disabled</c:if>">
        <a href="javascript:goPage(${page.pageNow - 1})">上一页</a>
      </li>
      <!-- 页码按钮 -->
      <c:forEach begin="1" end="${page.pageCount}" var="i">
        <li class="paginItem <c:if test='${page.pageNow == i}'>current</c:if>">
          <a href="javascript:goPage(${i})">${i}</a>
        </li>
      </c:forEach>
      <!-- 下一页 -->
      <li class="paginItem <c:if test='${page.pageNow == page.pageCount}'>disabled</c:if>">
        <a href="javascript:goPage(${page.pageNow + 1})">下一页</a>
      </li>
      <!-- 末页 -->
      <li class="paginItem <c:if test='${page.pageNow == page.pageCount}'>disabled</c:if>">
        <a href="javascript:goPage(${page.pageCount})">末页</a>
      </li>
    </ul>

    <div class="jump-box">
      到第<input type="text" id="jumpPage" value="${page.pageNow}" />页
      <button onclick="jumpToPage()">确定</button>
    </div>
  </div>

  <!-- 操作提示框 -->
  <div class="tip">
    <div class="tiptop"><span>商品操作提示</span><a href="javascript:;"></a></div>
    <div class="tipinfo">
      <div class="tipright">
        <p>是否确认执行此操作？</p>
        <cite>删除商品将无法恢复，请确认关联数据已处理。</cite>
      </div>
    </div>
    <div class="tipbtn">
      <input type="button" class="sure" value="确定" />
      <input type="button" class="cancel" value="取消" />
    </div>
  </div>
</div>
</body>
</html>