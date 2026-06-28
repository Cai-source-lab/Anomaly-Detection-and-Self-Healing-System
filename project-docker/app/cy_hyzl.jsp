<%--
  Created by IntelliJ IDEA.
  User: 哈哈哈
  Date: 2025/9/17
  Time: 15:40
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>会员资料列表</title>
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

            // 表单提交验证
            $("#searchForm").submit(function(e){
                var phone = $("#phone").val().trim();
                if(phone && !/^1[3-9]\d{9}$/.test(phone)){
                    alert("请输入有效的11位手机号");
                    return false;
                }
                return true;
            });
        });

        // 分页跳转
        function goPage(page) {
            var totalPages = ${page.pageCount};
            if (page < 1) page = 1;
            if (page > totalPages) page = totalPages;

            var vname = $("#vname").val().trim();
            var phone = $("#phone").val().trim();
            var jb = $("#jb").val();

            var url = "cy_QueryHy_Page1?pageNow=" + page;
            if(vname) url += "&vname=" + encodeURIComponent(vname);
            if(phone) url += "&phone=" + phone;
            if(jb && jb !== "all") url += "&jb=" + jb;

            window.location.href = url;
        }

        // 输入框跳转
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

        // 删除会员功能已移除
    </script>
    <style type="text/css">
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
            flex-wrap: wrap;
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
        .toolbar a, .search-btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 5px;
            padding: 6px 15px;
            background-color: #1c77ac;
            color: #fff;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            cursor: pointer;
        }
        .toolbar a:hover, .search-btn:hover {
            background-color: #16689e;
        }
        .search-btn.reset {
            background-color: #666;
        }
        .tablelist {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        .tablelist th, .tablelist td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: center;
        }
        .tablelist th {
            background-color: #f5f5f5;
        }
        .tablelist .odd {
            background-color: #f9f9f9;
        }
        .tablelink {
            color: #1c77ac;
            text-decoration: none;
            margin: 0 5px;
        }
        .tablelink:hover {
            text-decoration: underline;
        }
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
            border: 1px solid #1c77ac;
            color: #1c77ac;
            text-decoration: none;
            border-radius: 4px;
            font-size: 14px;
            background-color: #fff;
        }
        .paginItem.current a {
            background-color: #1c77ac;
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
        .no-data {
            text-align: center;
            padding: 30px 0;
            color: #999;
        }
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
            background: #1c77ac;
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
        <li><a href="#">会员资料</a></li>
    </ul>
</div>
<div class="rightinfo">
    <!-- 搜索表单 -->
    <div class="tools">
        <form id="searchForm" action="cy_QueryHy_Page1" method="post">
            <ul class="toolbar">
                <li>
                    <label>会员姓名：</label>
                    <input type="text" name="vname" id="vname" class="dfinput"
                           value="${vname}" placeholder="请输入会员姓名"/>
                </li>
                <li>
                    <label>联系电话：</label>
                    <input type="text" name="phone" id="phone" class="dfinput"
                           value="${phone}" placeholder="请输入11位手机号"/>
                </li>
                <li>
                    <label>会员级别：</label>
                    <select name="jb" id="jb" class="select-input">
                        <option value="all" ${empty jb || jb == 'all' ? 'selected' : ''}>全部级别</option>
                        <option value="普通会员" ${jb == '普通会员' ? 'selected' : ''}>普通会员</option>
                        <option value="银卡会员" ${jb == '银卡会员' ? 'selected' : ''}>银卡会员</option>
                        <option value="金卡会员" ${jb == '金卡会员' ? 'selected' : ''}>金卡会员</option>
                        <option value="钻石会员" ${jb == '钻石会员' ? 'selected' : ''}>钻石会员</option>
                    </select>
                </li>
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

    <!-- 会员表格 -->
    <table class="tablelist">
        <thead>
        <tr>
            <th>序号</th>
            <th>会员ID</th>
            <th>会员名称</th>
            <th>联系电话</th>
            <th>会员钱包</th>
            <th>会员积分</th>
            <th>会员住址</th>
            <th>会员级别</th>
            <th>注册时间</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty somehy && somehy.size() > 0}">
                <c:forEach items="${somehy}" var="hy" varStatus="status">
                    <tr>
                        <td>${(page.pageNow - 1) * page.pageSize + status.index + 1}</td>
                        <td>${hy.id}</td>
                        <td>${hy.vname}</td>
                        <td>${hy.phone}</td>
                        <td>${hy.qb}</td>
                        <td>${hy.jf}</td>
                        <td>${hy.addr}</td>
                        <td>${hy.jb}</td>
                        <td>${hy.dtime}</td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="9" class="no-data">
                        <c:choose>
                            <c:when test="${(not empty vname) || (not empty phone) || (not empty jb && jb != 'all')}">
                                暂无匹配的会员数据，请调整查询条件
                            </c:when>
                            <c:otherwise>
                                暂无会员数据，请先添加会员
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>

    <!-- 分页控件 -->
    <div class="pagin">
        <div class="message">
            共<i class="blue">${page.rowCount}</i>条记录，共<i class="blue">${page.pageCount}</i>页，
            当前显示第<i class="blue">${page.pageNow}</i>页
        </div>

        <ul class="paginList">
            <li class="paginItem <c:if test='${page.pageNow == 1}'>disabled</c:if>">
                <a href="javascript:goPage(1)">首页</a>
            </li>
            <li class="paginItem <c:if test='${page.pageNow == 1}'>disabled</c:if>">
                <a href="javascript:goPage(${page.pageNow - 1})">上一页</a>
            </li>
            <c:forEach begin="1" end="${page.pageCount}" var="i">
                <li class="paginItem <c:if test='${page.pageNow == i}'>current</c:if>">
                    <a href="javascript:goPage(${i})">${i}</a>
                </li>
            </c:forEach>
            <li class="paginItem <c:if test='${page.pageNow == page.pageCount}'>disabled</c:if>">
                <a href="javascript:goPage(${page.pageNow + 1})">下一页</a>
            </li>
            <li class="paginItem <c:if test='${page.pageNow == page.pageCount}'>disabled</c:if>">
                <a href="javascript:goPage(${page.pageCount})">末页</a>
            </li>
        </ul>

        <div class="jump-box">
            到第<input type="text" id="jumpPage" value="${page.pageNow}" />页
            <button onclick="jumpToPage()">确定</button>
        </div>
    </div>

    <!-- 提示框 -->
    <div class="tip">
        <div class="tiptop"><span>提示信息</span><a href="javascript:;"></a></div>
        <div class="tipinfo">
            <div class="tipright">
                <p>是否确认对信息的操作？</p>
                <cite>如果是请点击确定按钮，否则请点取消。</cite>
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
