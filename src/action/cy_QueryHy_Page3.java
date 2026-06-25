package action;

import dao.cy_goodsDao;
import entity.cy_goods;
import untils.Page;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// Servlet映射路径：与商品页面表单action对应
@WebServlet("/cy_QueryHy_Page3")
public class cy_QueryHy_Page3 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 支持GET请求，页面直接访问时显示所有商品数据
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 统一用GET处理，避免中文乱码和参数丢失
        processRequest(request, response);
    }
    
    // 公共处理方法，支持GET和POST请求
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 设置编码（解决中文乱码）
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        // 2. 获取商品页面传递的查询参数（与页面表单name对应）
        String goodsName = request.getParameter("goodsName"); // 商品名称
        String txm = request.getParameter("txm");             // 条形码
        String minPrice = request.getParameter("minPrice");   // 最低卖价
        String maxPrice = request.getParameter("maxPrice");   // 最高卖价
        String stockStatus = request.getParameter("stockStatus"); // 库存状态

        // 3. 保存参数到request，用于页面回显（刷新后查询条件不丢失）
        request.setAttribute("goodsName", goodsName);
        request.setAttribute("txm", txm);
        request.setAttribute("minPrice", minPrice);
        request.setAttribute("maxPrice", maxPrice);
        request.setAttribute("stockStatus", stockStatus);

        // 4. 处理分页参数（默认第1页，每页3条）
        String pageNowStr = request.getParameter("pageNow");
        int pageNow = 1; // 默认当前页
        int pageSize = 3; // 每页显示条数

        // 校验页码（避免非法参数）
        if (pageNowStr != null && !pageNowStr.trim().isEmpty()) {
            try {
                pageNow = Integer.parseInt(pageNowStr);
                pageNow = Math.max(pageNow, 1); // 页码不能小于1
            } catch (NumberFormatException e) {
                pageNow = 1; // 非法参数默认第1页
            }
        }

        // 5. 实例化分页对象和商品DAO（匹配你的DAO实例化风格）
        Page page = new Page();
        cy_goodsDao goodsDao = new cy_goodsDao();

        // 6. 调用DAO查询总记录数，计算总页数
        int totalCount = goodsDao.getGoodsCount(goodsName, txm, minPrice, maxPrice, stockStatus);
        page.setRowCount(totalCount); // 总记录数
        // 计算总页数（整除则为商，否则商+1）
        int totalPage = (totalCount % pageSize == 0) ? (totalCount / pageSize) : (totalCount / pageSize + 1);
        totalPage = Math.max(totalPage, 1); // 总页数至少1页
        page.setPageCount(totalPage);

        // 7. 校正当前页码（避免超过总页数）
        pageNow = Math.min(pageNow, totalPage);
        page.setPageNow(pageNow);
        page.setPageSize(pageSize);

        // 8. 计算分页起始位置（LIMIT 起始位置, 每页条数）
        int start = (pageNow - 1) * pageSize;

        // 9. 调用DAO查询当前页商品数据
        List<cy_goods> someGoods = goodsDao.queryGoodsByPage(goodsName, txm, minPrice, maxPrice, stockStatus, start, pageSize);

        // 10. 将数据传递到商品页面（页面用EL表达式取值）
        request.setAttribute("someGoods", someGoods);
        request.setAttribute("page", page);

        // 11. 转发到商品列表页面（保持在商品信息页）
        try {
            request.getRequestDispatcher("cy_info.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("cy_QueryHy_Page3转发异常：" + e.getMessage());
            e.printStackTrace();
        }
    }
}