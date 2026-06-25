<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>商品入库</title>
    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            // 条形码输入后自动查询商品信息
            $("#txm").on('input', function(){
                var txm = $(this).val().trim();
                if(txm.length >= 3) { // 至少3位数字才查询
                    searchGoods(txm);
                } else {
                    clearGoodsInfo();
                }
            });
            
            // 表单提交验证
            $("#stockForm").submit(function(e){
                var txm = $("#txm").val().trim();
                var quantity = $("#quantity").val().trim();
                var remark = $("#remark").val().trim();
                
                if(!txm){
                    alert("请输入商品条形码！");
                    $("#txm").focus();
                    return false;
                }
                if(!quantity || isNaN(quantity) || parseInt(quantity) <= 0){
                    alert("请输入有效的入库数量！");
                    $("#quantity").focus();
                    return false;
                }
                
                if(!confirm("确认入库 " + quantity + " 件商品吗？")){
                    return false;
                }
                
                return true;
            });
            
            // 返回按钮
            $(".back-btn").click(function(){
                window.location.href = "cy_QueryHy_Page3";
            });
        });
        
        // 查询商品信息
        function searchGoods(txm) {
            $.ajax({
                url: "cy_GetGoodsByTxm",
                type: "GET",
                data: {txm: txm},
                dataType: "json",
                success: function(data) {
                    if(data.success) {
                        displayGoodsInfo(data.goods);
                    } else {
                        clearGoodsInfo();
                        if(txm.length >= 6) { // 条形码足够长时才显示未找到
                            $("#goodsInfo").html("<div class='error-msg'>未找到该商品，请检查条形码是否正确</div>");
                        }
                    }
                },
                error: function() {
                    clearGoodsInfo();
                }
            });
        }
        
        // 显示商品信息
        function displayGoodsInfo(goods) {
            var html = '<div class="goods-info">' +
                '<h3>商品信息</h3>' +
                '<div class="info-row"><label>商品名称：</label><span>' + goods.name + '</span></div>' +
                '<div class="info-row"><label>条形码：</label><span>' + goods.txm + '</span></div>' +
                '<div class="info-row"><label>单位：</label><span>' + goods.dw + '</span></div>' +
                '<div class="info-row"><label>进价：</label><span>¥' + goods.j_price + '</span></div>' +
                '<div class="info-row"><label>卖价：</label><span>¥' + goods.m_price + '</span></div>' +
                '<div class="info-row"><label>当前库存：</label><span class="stock-count">' + goods.kc + '</span></div>' +
                '</div>';
            $("#goodsInfo").html(html);
        }
        
        // 清除商品信息
        function clearGoodsInfo() {
            $("#goodsInfo").html("");
        }
    </script>
    <style type="text/css">
        body {
            font-family: "Microsoft YaHei", sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .stock-container {
            max-width: 800px;
            margin: 0 auto;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        .stock-header {
            background: linear-gradient(135deg, #2e8b57, #1c77ac);
            color: #fff;
            padding: 20px;
            text-align: center;
        }
        .stock-header h2 {
            margin: 0;
            font-size: 24px;
        }
        .stock-body {
            padding: 30px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #333;
        }
        .form-group input, .form-group textarea {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 6px;
            font-size: 16px;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }
        .form-group input:focus, .form-group textarea:focus {
            outline: none;
            border-color: #2e8b57;
        }
        .form-row {
            display: flex;
            gap: 15px;
        }
        .form-row .form-group {
            flex: 1;
        }
        .btn-group {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }
        .btn {
            flex: 1;
            padding: 12px 20px;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.3s;
        }
        .btn-primary {
            background: #2e8b57;
            color: #fff;
        }
        .btn-primary:hover {
            background: #236b43;
        }
        .btn-secondary {
            background: #6c757d;
            color: #fff;
        }
        .btn-secondary:hover {
            background: #5a6268;
        }
        .required {
            color: #dc3545;
        }
        .form-help {
            font-size: 12px;
            color: #666;
            margin-top: 5px;
        }
        .goods-info {
            background: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 6px;
            padding: 20px;
            margin: 20px 0;
        }
        .goods-info h3 {
            margin: 0 0 15px 0;
            color: #2e8b57;
        }
        .info-row {
            display: flex;
            margin-bottom: 10px;
        }
        .info-row label {
            width: 100px;
            font-weight: bold;
            color: #666;
        }
        .info-row span {
            color: #333;
        }
        .stock-count {
            color: #2e8b57;
            font-weight: bold;
        }
        .error-msg {
            color: #dc3545;
            text-align: center;
            padding: 20px;
        }
        .loading {
            text-align: center;
            color: #666;
            padding: 20px;
        }
    </style>
</head>
<body>
    <div class="stock-container">
        <div class="stock-header">
            <h2>商品入库</h2>
        </div>
        
        <div class="stock-body">
            <form id="stockForm" action="cy_StockIn" method="post">
                <!-- 条形码输入 -->
                <div class="form-group">
                    <label for="txm">商品条形码 <span class="required">*</span></label>
                    <input type="text" id="txm" name="txm" placeholder="请输入商品条形码" required />
                    <div class="form-help">输入条形码后会自动查询商品信息</div>
                </div>
                
                <!-- 商品信息显示区域 -->
                <div id="goodsInfo"></div>
                
                <!-- 入库数量 -->
                <div class="form-group">
                    <label for="quantity">入库数量 <span class="required">*</span></label>
                    <input type="number" id="quantity" name="quantity" placeholder="请输入入库数量" min="1" required />
                    <div class="form-help">请输入要入库的商品数量</div>
                </div>
                
                <!-- 入库备注 -->
                <div class="form-group">
                    <label for="remark">入库备注</label>
                    <textarea id="remark" name="remark" rows="3" placeholder="请输入入库备注（可选）"></textarea>
                </div>
                
                <!-- 按钮组 -->
                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">确认入库</button>
                    <button type="button" class="btn btn-secondary back-btn">返回列表</button>
                </div>
            </form>
        </div>
    </div>
    
    <script type="text/javascript">
        // 条形码输入限制（仅数字）
        document.getElementById('txm').addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    </script>
</body>
</html>


















