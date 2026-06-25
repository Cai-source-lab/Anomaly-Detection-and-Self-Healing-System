<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>添加商品</title>
    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            // 表单验证
            $("#goodsForm").submit(function(e){
                var goodsName = $("#goodsName").val().trim();
                var txm = $("#txm").val().trim();
                var dw = $("#dw").val().trim();
                var j_price = $("#j_price").val().trim();
                var m_price = $("#m_price").val().trim();
                
                // 验证必填字段
                if(!goodsName){
                    alert("请输入商品名称！");
                    $("#goodsName").focus();
                    return false;
                }
                if(!txm){
                    alert("请输入条形码！");
                    $("#txm").focus();
                    return false;
                }
                if(!dw){
                    alert("请输入单位！");
                    $("#dw").focus();
                    return false;
                }
                if(!j_price || isNaN(j_price) || parseFloat(j_price) <= 0){
                    alert("请输入有效的进价！");
                    $("#j_price").focus();
                    return false;
                }
                if(!m_price || isNaN(m_price) || parseFloat(m_price) <= 0){
                    alert("请输入有效的卖价！");
                    $("#m_price").focus();
                    return false;
                }
                
                // 确认添加
                if(!confirm("确认添加商品【" + goodsName + "】吗？")){
                    return false;
                }
                
                return true;
            });
            
            // 返回按钮
            $(".back-btn").click(function(){
                window.location.href = "cy_QueryHy_Page3";
            });
        });
    </script>
    <style type="text/css">
        body {
            font-family: "Microsoft YaHei", sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .add-container {
            max-width: 600px;
            margin: 0 auto;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        .add-header {
            background: linear-gradient(135deg, #2e8b57, #1c77ac);
            color: #fff;
            padding: 20px;
            text-align: center;
        }
        .add-header h2 {
            margin: 0;
            font-size: 24px;
        }
        .add-body {
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
        .form-group input, .form-group select, .form-group textarea {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 6px;
            font-size: 16px;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }
        .form-group input:focus, .form-group select:focus, .form-group textarea:focus {
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
    </style>    
</head>
<body>
    <div class="add-container">
        <div class="add-header">
            <h2>添加商品</h2>
        </div>
        
        <div class="add-body">
            <form id="goodsForm" action="cy_goodsAdd" method="post">
                <!-- 基本信息 -->
                <div class="form-group">
                    <label for="goodsName">商品名称 <span class="required">*</span></label>
                    <input type="text" id="goodsName" name="goodsName" placeholder="请输入商品名称" required />
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="txm">条形码 <span class="required">*</span></label>
                        <input type="text" id="txm" name="txm" placeholder="请输入条形码" required />
                        <div class="form-help">条形码用于商品识别，请确保唯一性</div>
                    </div>
                    <div class="form-group">
                        <label for="dw">单位 <span class="required">*</span></label>
                        <select id="dw" name="dw" required>
                            <option value="">请选择单位</option>
                            <option value="个">个</option>
                            <option value="件">件</option>
                            <option value="包">包</option>
                            <option value="盒">盒</option>
                            <option value="瓶">瓶</option>
                            <option value="袋">袋</option>
                            <option value="箱">箱</option>
                            <option value="套">套</option>
                            <option value="台">台</option>
                            <option value="只">只</option>
                        </select>
                    </div>
                </div>
                
                <!-- 价格信息 -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="j_price">进价(元) <span class="required">*</span></label>
                        <input type="number" id="j_price" name="j_price" placeholder="0.00" step="0.01" min="0" required />
                    </div>
                    <div class="form-group">
                        <label for="m_price">卖价(元) <span class="required">*</span></label>
                        <input type="number" id="m_price" name="m_price" placeholder="0.00" step="0.01" min="0" required />
                    </div>
                </div>
                
                <!-- 折扣信息 -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="zk1">折扣1(%)</label>
                        <input type="number" id="zk1" name="zk1" value="100" min="0" max="100" />
                        <div class="form-help">默认100%表示无折扣</div>
                    </div>
                    <div class="form-group">
                        <label for="zk2">折扣2(%)</label>
                        <input type="number" id="zk2" name="zk2" value="100" min="0" max="100" />
                        <div class="form-help">默认100%表示无折扣</div>
                    </div>
                </div>
                
                <!-- 库存信息 -->
                <div class="form-group">
                    <label for="kc">库存数量</label>
                    <input type="number" id="kc" name="kc" value="0" min="0" />
                    <div class="form-help">初始库存数量，默认为0</div>
                </div>
                
                <!-- 商品描述 -->
                <div class="form-group">
                    <label for="ms">商品描述</label>
                    <textarea id="ms" name="ms" rows="3" placeholder="请输入商品描述（可选）"></textarea>
                </div>
                
                <!-- 按钮组 -->
                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">确认添加</button>
                    <button type="button" class="btn btn-secondary back-btn">返回列表</button>
                </div>
            </form>
        </div>
    </div>
    
    <script type="text/javascript">
        // 自动计算卖价（进价 * 1.5）
        document.getElementById('j_price').addEventListener('input', function() {
            var j_price = parseFloat(this.value);
            if (j_price > 0) {
                var m_price = j_price * 1.5;
                document.getElementById('m_price').value = m_price.toFixed(2);
            }
        });
        
        // 条形码输入限制（仅数字）
        document.getElementById('txm').addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    </script>
</body>
</html>


















