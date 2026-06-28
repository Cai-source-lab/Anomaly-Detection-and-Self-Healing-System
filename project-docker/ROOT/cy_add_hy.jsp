<%--
  Created by IntelliJ IDEA.
  User: 哈哈哈
  Date: 2025/9/25
  Time: 10:00
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>添加会员</title>
    <link href="css/style.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript">
        // 表单验证：确保必填项（姓名、电话）已填写
        function checkForm() {
            var vname = document.getElementById("vname").value.trim();
            var phone = document.getElementById("phone").value.trim();

            if (vname === "") {
                alert("请输入会员姓名！");
                return false; // 阻止表单提交
            }
            if (phone === "" || !/^1[3-9]\d{9}$/.test(phone)) { // 简单手机号校验
                alert("请输入正确的11位手机号！");
                return false;
            }
            // 验证通过，提交表单
            return true;
        }
    </script>
    <style type="text/css">
        .form-box {
            width: 600px;
            margin: 50px auto;
            border: 1px solid #ddd;
            padding: 30px;
            border-radius: 8px;
            background-color: #f9f9f9;
        }
        .form-item {
            margin-bottom: 20px;
            overflow: hidden;
        }
        .form-item label {
            float: left;
            width: 120px;
            text-align: right;
            margin-right: 20px;
            line-height: 34px;
            font-size: 14px;
            color: #333;
        }
        .form-item input, .form-item select, .form-item textarea {
            float: left;
            width: 350px;
            padding: 8px 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }
        .form-item textarea {
            height: 80px;
            resize: none; // 禁止拉伸
        }
        .btn-group {
            margin-left: 140px; // 与label对齐
        }
        .btn {
            padding: 10px 25px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
        }
        .submit-btn {
            background-color: #1c77ac;
            color: white;
            margin-right: 20px;
        }
        .reset-btn {
            background-color: #eee;
            color: #666;
        }
    </style>
</head>
<body>
<div class="place">
    <span>位置：</span>
    <ul class="placeul">
        <li><a href="#">首页</a></li>
        <li><a href="cy_QueryHy_Page1" target="rightFrame">会员资料</a></li>
        <li><a href="#">添加会员</a></li>
    </ul>
</div>

<div class="form-box">
    <!-- 表单提交到Servlet：xlc_AddHy -->
    <form action="cy_AddHy" method="post" onsubmit="return checkForm()">
        <div class="form-item">
            <label>会员姓名：</label>
            <input type="text" id="vname" name="vname" placeholder="请输入会员姓名" required />
        </div>
        <div class="form-item">
            <label>联系电话：</label>
            <input type="text" id="phone" name="phone" placeholder="请输入11位手机号" required />
        </div>
        <div class="form-item">
            <label>初始钱包：</label>
            <input type="number" name="qb" value="0.00" step="0.01" min="0" placeholder="默认0元" />
        </div>
        <div class="form-item">
            <label>初始积分：</label>
            <input type="number" name="jf" value="0" min="0" placeholder="默认0积分" />
        </div>
        <div class="form-item">
            <label>会员住址：</label>
            <textarea name="addr" placeholder="请输入会员住址（可选）"></textarea>
        </div>
        <div class="form-item">
            <label>会员级别：</label>
            <select name="jb">
                <option value="普通会员" selected>普通会员</option>
                <option value="银卡会员">银卡会员</option>
                <option value="金卡会员">金卡会员</option>
                <option value="钻石会员">钻石会员</option>
            </select>
        </div>
        <div class="btn-group">
            <button type="submit" class="btn submit-btn">添加会员</button>
            <button type="reset" class="btn reset-btn">重置</button>
        </div>
    </form>
</div>
</body>
</html>