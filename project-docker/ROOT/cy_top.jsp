<%--
  顶部导航栏
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>顶部导航</title>
    <link href="css/style.css" rel="stylesheet" type="text/css" />
    <script language="JavaScript" src="js/jquery.js"></script>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            margin: 0;
            padding: 0;
            overflow: hidden;
            height: 50px;
        }
        .topnav {
            background: #1c77ac;
            height: 50px;
            line-height: 50px;
            padding: 0 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            position: relative;
            z-index: 10000;
            min-height: 50px;
        }
        .topnav-menu {
            display: flex;
            list-style: none;
            margin: 0;
            padding: 0;
        }
        .topnav-menu > li {
            position: relative;
            margin-right: 5px;
        }
        .topnav-menu > li > a {
            display: block;
            padding: 0 20px;
            color: #fff;
            text-decoration: none;
            font-size: 14px;
            height: 50px;
            line-height: 50px;
            transition: background 0.3s;
            cursor: pointer;
            white-space: nowrap;
        }
        .topnav-menu > li > a:hover {
            background: rgba(255,255,255,0.1);
        }
        .topnav-menu > li.active > a {
            background: rgba(255,255,255,0.2);
        }
        .topnav-submenu {
            position: absolute;
            top: 50px;
            left: 0;
            background: #fff;
            min-width: 150px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.15);
            display: none;
            z-index: 10001;
            list-style: none;
            margin: 0;
            padding: 5px 0;
            border: 1px solid #e0e0e0;
            max-height: 250px;
            overflow-y: auto;
        }
        .topnav-menu > li:hover .topnav-submenu,
        .topnav-menu > li.active .topnav-submenu {
            display: block;
        }
        .topnav-submenu li {
            margin: 0;
        }
        .topnav-submenu li a {
            display: block;
            padding: 10px 20px;
            color: #333;
            text-decoration: none;
            font-size: 13px;
            transition: background 0.2s;
            cursor: pointer;
        }
        .topnav-submenu li a:hover {
            background: #f0f9fd;
            color: #1c77ac;
        }
        .topnav-menu > li > a img {
            vertical-align: middle;
            margin-right: 5px;
            width: 16px;
            height: 16px;
        }
    </style>
    <script type="text/javascript">
        $(function(){
            // 点击主菜单项切换显示/隐藏子菜单
            $(".topnav-menu > li > a").click(function(e){
                e.preventDefault();
                e.stopPropagation();
                var $li = $(this).parent();
                var $submenu = $li.find('.topnav-submenu');
                
                // 关闭其他菜单
                $(".topnav-menu > li").not($li).removeClass("active");
                $(".topnav-menu > li").not($li).find('.topnav-submenu').hide();
                
                // 切换当前菜单
                if($submenu.is(':visible')) {
                    $li.removeClass("active");
                    $submenu.hide();
                } else {
                    $li.addClass("active");
                    $submenu.show();
                }
            });
            
            // 点击子菜单项
            $(".topnav-submenu li a").click(function(e){
                var href = $(this).attr('href');
                if(href && href !== '#' && href !== 'javascript:void(0);') {
                    // 让链接正常跳转
                    return true;
                }
                e.preventDefault();
            });
            
            // 点击页面其他地方关闭菜单
            $(document).click(function(e){
                if(!$(e.target).closest('.topnav-menu').length) {
                    $(".topnav-menu > li").removeClass("active");
                    $(".topnav-submenu").hide();
                }
            });
        });
    </script>
</head>
<body style="margin:0;padding:0;">
<div class="topnav">
    <ul class="topnav-menu">
        <li>
            <a href="javascript:void(0);">
                <img src="images/leftico01.png" style="width:16px;height:16px;" />商品管理
            </a>
            <ul class="topnav-submenu">
                <li><a href="cy_shop.jsp" target="rightFrame">商品零售</a></li>
                <li><a href="cy_add_goods.jsp" target="rightFrame">添加商品</a></li>
                <li><a href="cy_info.jsp" target="rightFrame">商品信息</a></li>
                <li><a href="cy_stock_in.jsp" target="rightFrame">商品入库</a></li>
            </ul>
        </li>
        <li>
            <a href="javascript:void(0);">
                <img src="images/leftico02.png" style="width:16px;height:16px;" />会员管理
            </a>
            <ul class="topnav-submenu">
                <li><a href="cy_QueryHy_Page1" target="rightFrame">会员资料</a></li>
                <li><a href="cy_add_hy.jsp" target="rightFrame">添加会员</a></li>
            </ul>
        </li>
    </ul>
</div>
</body>
</html>

