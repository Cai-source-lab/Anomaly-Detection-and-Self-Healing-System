package action;

import dao.cy_goodsDao;
import dao.cy_stock_recordDao;
import entity.cy_goods;
import entity.cy_stock_record;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 商品入库处理
 */
@WebServlet("/cy_StockIn")
public class cy_StockIn extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // GET请求：显示入库页面
        request.getRequestDispatcher("cy_stock_in.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 设置编码
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        
        try {
            // 获取参数
            String txm = request.getParameter("txm");
            String quantityStr = request.getParameter("quantity");
            String remark = request.getParameter("remark");
            
            // 参数验证
            if (txm == null || txm.trim().isEmpty()) {
                out.print("<script>alert('条形码不能为空！');history.back();</script>");
                return;
            }
            
            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                out.print("<script>alert('入库数量不能为空！');history.back();</script>");
                return;
            }
            
            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr.trim());
                if (quantity <= 0) {
                    out.print("<script>alert('入库数量必须大于0！');history.back();</script>");
                    return;
                }
            } catch (NumberFormatException e) {
                out.print("<script>alert('入库数量格式错误！');history.back();</script>");
                return;
            }
            
            // 查询商品信息
            cy_goodsDao goodsDao = new cy_goodsDao();
            cy_goods goods = goodsDao.getGoodsByTxm(txm.trim());
            
            if (goods == null) {
                out.print("<script>alert('未找到该商品，请检查条形码是否正确！');history.back();</script>");
                return;
            }
            
            // 获取入库前库存
            int beforeStock = goods.getKc();
            int afterStock = beforeStock + quantity;
            
            // 更新商品库存
            String updateSql = "UPDATE cy_goods SET kc = ? WHERE txm = ?";
            Object[] updateParams = {afterStock, txm.trim()};
            int updateResult = goodsDao.editGoods(updateSql, updateParams);
            
            if (updateResult > 0) {
                // 添加入库记录
                cy_stock_recordDao recordDao = new cy_stock_recordDao();
                cy_stock_record record = new cy_stock_record();
                record.setGoodsName(goods.getGoodsName());
                record.setTxm(goods.getTxm());
                record.setQuantity(quantity);
                record.setBeforeStock(beforeStock);
                record.setAfterStock(afterStock);
                record.setStockTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                record.setOperator("系统管理员"); // 可以从session中获取当前用户
                record.setRemark(remark);
                
                int recordResult = recordDao.addStockRecord(record);
                
                if (recordResult > 0) {
                    out.print("<script>alert('商品入库成功！\\n商品：" + goods.getGoodsName() + "\\n入库数量：" + quantity + "\\n入库前库存：" + beforeStock + "\\n入库后库存：" + afterStock + "');window.location.href='cy_QueryHy_Page3';</script>");
                } else {
                    out.print("<script>alert('商品入库成功，但记录保存失败！');window.location.href='cy_QueryHy_Page3';</script>");
                }
            } else {
                out.print("<script>alert('商品入库失败！');history.back();</script>");
            }
            
        } catch (Exception e) {
            System.out.println("商品入库异常：" + e.getMessage());
            e.printStackTrace();
            out.print("<script>alert('系统异常，入库失败！');history.back();</script>");
        } finally {
            out.close();
        }
    }
}
