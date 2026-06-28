package action;

import dao.cy_stock_recordDao;
import entity.cy_stock_record;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * 入库历史查询
 */
@WebServlet("/cy_StockHistory")
public class cy_StockHistory extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 设置编码
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        
        try {
            // 获取分页参数
            String pageNowStr = request.getParameter("pageNow");
            int pageNow = 1;
            if (pageNowStr != null && !pageNowStr.trim().isEmpty()) {
                try {
                    pageNow = Integer.parseInt(pageNowStr.trim());
                    if (pageNow < 1) pageNow = 1;
                } catch (NumberFormatException e) {
                    pageNow = 1;
                }
            }
            
            int pageSize = 10; // 每页显示10条记录
            
            // 查询入库记录
            cy_stock_recordDao recordDao = new cy_stock_recordDao();
            List<cy_stock_record> stockRecords = recordDao.getStockRecordsByPage(pageNow, pageSize);
            int totalCount = recordDao.getStockRecordCount();
            int totalPages = (totalCount + pageSize - 1) / pageSize;
            
            System.out.println("cy_StockHistory: 查询到入库记录数量 = " + stockRecords.size());
            System.out.println("cy_StockHistory: 总记录数 = " + totalCount);
            System.out.println("cy_StockHistory: 总页数 = " + totalPages);
            
            // 设置请求属性
            request.setAttribute("stockRecords", stockRecords);
            request.setAttribute("pageNow", pageNow);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);
            
            // 转发到入库历史页面
            request.getRequestDispatcher("cy_stock_history_simple.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.out.println("查询入库历史异常：" + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("cy_QueryHy_Page3");
        }
    }
}
