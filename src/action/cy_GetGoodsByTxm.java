package action;

import dao.cy_goodsDao;
import entity.cy_goods;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据条形码查询商品信息
 */
@WebServlet("/cy_GetGoodsByTxm")
public class cy_GetGoodsByTxm extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 设置编码
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        
        // 获取条形码参数
        String txm = request.getParameter("txm");
        
        Map<String, Object> result = new HashMap<>();
        
        if (txm == null || txm.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "条形码不能为空");
        } else {
            try {
                cy_goodsDao goodsDao = new cy_goodsDao();
                cy_goods goods = goodsDao.getGoodsByTxm(txm.trim());
                
                if (goods != null) {
                    result.put("success", true);
                    result.put("goods", goods);
                } else {
                    result.put("success", false);
                    result.put("message", "未找到该商品");
                }
            } catch (Exception e) {
                System.out.println("查询商品异常：" + e.getMessage());
                e.printStackTrace();
                result.put("success", false);
                result.put("message", "查询商品失败");
            }
        }
        
        // 返回JSON结果
        PrintWriter out = response.getWriter();
        out.print(buildJsonResponse(result));
        out.close();
    }
    
    // 手动构建JSON响应
    private String buildJsonResponse(Map<String, Object> result) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : result.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;
            
            json.append("\"").append(entry.getKey()).append("\":");
            
            Object value = entry.getValue();
            if (value instanceof String) {
                json.append("\"").append(escapeJson((String) value)).append("\"");
            } else if (value instanceof Boolean) {
                json.append(value.toString());
            } else if (value instanceof cy_goods) {
                json.append(buildGoodsJson((cy_goods) value));
            } else {
                json.append("\"").append(escapeJson(value.toString())).append("\"");
            }
        }
        
        json.append("}");
        return json.toString();
    }
    
    // 构建商品JSON
    private String buildGoodsJson(cy_goods goods) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"id\":").append(goods.getId()).append(",");
        json.append("\"name\":\"").append(escapeJson(goods.getGoodsName())).append("\",");
        json.append("\"txm\":\"").append(escapeJson(goods.getTxm())).append("\",");
        json.append("\"dw\":\"").append(escapeJson(goods.getDw())).append("\",");
        json.append("\"j_price\":").append(goods.getJ_price()).append(",");
        json.append("\"m_price\":").append(goods.getM_price()).append(",");
        json.append("\"zk1\":").append(goods.getZk1()).append(",");
        json.append("\"zk2\":").append(goods.getZk2()).append(",");
        json.append("\"kc\":").append(goods.getKc()).append(",");
        json.append("\"ms\":\"").append(escapeJson(goods.getMs())).append("\",");
        json.append("\"dtime\":\"").append(escapeJson(goods.getDtime())).append("\"");
        json.append("}");
        return json.toString();
    }
    
    // 转义JSON字符串
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
