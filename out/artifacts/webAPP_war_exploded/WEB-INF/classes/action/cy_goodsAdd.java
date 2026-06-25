package action;

import dao.cy_goodsDao;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

// 映射路径：与商品添加表单的action对应
@WebServlet("/cy_goodsAdd")
public class cy_goodsAdd extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // GET请求：显示商品添加页面
        request.getRequestDispatcher("cy_add_goods.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 设置编码，解决中文乱码
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();

        // 2. 接收表单提交的商品参数（与表单name属性对应）
        String goodsName = request.getParameter("goodsName") == null ? "" : request.getParameter("goodsName").trim();
        String txm = request.getParameter("txm") == null ? "" : request.getParameter("txm").trim();
        String dw = request.getParameter("dw") == null ? "" : request.getParameter("dw").trim();

        // 处理数值类型参数（避免空指针和格式错误）
        double j_price = 0.00;
        try {
            j_price = Double.parseDouble(request.getParameter("j_price") == null ? "0.00" : request.getParameter("j_price").trim());
        } catch (NumberFormatException e) {
            out.print("<script>alert('进价格式错误！请输入数字');history.back();</script>");
            out.close();
            return;
        }

        double m_price = 0.00;
        try {
            m_price = Double.parseDouble(request.getParameter("m_price") == null ? "0.00" : request.getParameter("m_price").trim());
        } catch (NumberFormatException e) {
            out.print("<script>alert('卖价格式错误！请输入数字');history.back();</script>");
            out.close();
            return;
        }

        int zk1 = 100; // 默认折扣100%（不打折）
        try {
            zk1 = Integer.parseInt(request.getParameter("zk1") == null ? "100" : request.getParameter("zk1").trim());
        } catch (NumberFormatException e) {
            out.print("<script>alert('折扣1格式错误！请输入整数');history.back();</script>");
            out.close();
            return;
        }

        int zk2 = 100; // 默认折扣100%
        try {
            zk2 = Integer.parseInt(request.getParameter("zk2") == null ? "100" : request.getParameter("zk2").trim());
        } catch (NumberFormatException e) {
            out.print("<script>alert('折扣2格式错误！请输入整数');history.back();</script>");
            out.close();
            return;
        }

        int kc = 0; // 默认库存0
        try {
            kc = Integer.parseInt(request.getParameter("kc") == null ? "0" : request.getParameter("kc").trim());
        } catch (NumberFormatException e) {
            out.print("<script>alert('库存格式错误！请输入整数');history.back();</script>");
            out.close();
            return;
        }

        String ms = request.getParameter("ms") == null ? "" : request.getParameter("ms").trim();
        // 自动填充当前时间（格式：yyyy-MM-dd）
        String dtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // 3. 校验必填参数（商品名称、条形码、单位、进价、卖价不能为空）
        if (goodsName.isEmpty() || txm.isEmpty() || dw.isEmpty() || j_price <= 0 || m_price <= 0) {
            out.print("<script>alert('必填项不能为空，且价格必须大于0！');history.back();</script>");
            out.close();
            return;
        }

        // 4. 调用商品DAO执行添加操作
        cy_goodsDao goodsDao = new cy_goodsDao();
        // SQL语句：与cy_goods表字段对应
        String sql = "INSERT INTO cy_goods(" +
                "name, txm, dw, j_price, m_price, zk1, zk2, kc, ms, dtime" +
                ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // 参数数组：顺序与SQL占位符一致
        Object[] params = {
                goodsName, txm, dw, j_price, m_price,
                zk1, zk2, kc, ms, dtime
        };
        int result = goodsDao.addGoods(sql, params); // 调用DAO的添加方法

        // 5. 处理添加结果
        if (result > 0) {
            // 添加成功：提示并跳转到商品列表页（替换为你的商品列表Servlet路径）
            out.print("<script>alert('商品添加成功！');window.location.href='cy_QueryHy_Page3';</script>");
        } else {
            // 添加失败：提示并返回添加页面（保留输入内容）
            out.print("<script>alert('商品添加失败！可能条形码已存在');history.back();</script>");
        }
        out.close();
    }
}