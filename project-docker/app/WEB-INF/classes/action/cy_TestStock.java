package action;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 测试商品入库功能
 */
@WebServlet("/cy_TestStock")
public class cy_TestStock extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html>");
        out.println("<head><title>商品入库功能测试</title></head>");
        out.println("<body>");
        out.println("<h1>商品入库功能测试</h1>");
        out.println("<p>如果您能看到这个页面，说明Action类部署正常。</p>");
        out.println("<h2>测试链接：</h2>");
        out.println("<a href='cy_StockIn'>测试商品入库页面</a><br>");
        out.println("<a href='cy_StockHistory'>测试入库情况页面</a><br>");
        out.println("<a href='cy_GetGoodsByTxm?txm=123456'>测试商品查询</a><br>");
        out.println("<a href='cy_QueryHy_Page3'>返回商品列表</a>");
        out.println("</body>");
        out.println("</html>");
        
        out.close();
    }
}
