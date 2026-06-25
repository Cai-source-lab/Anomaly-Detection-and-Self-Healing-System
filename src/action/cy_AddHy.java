package action;

import dao.cy_HyDao;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/cy_AddHy") // 与表单action一致
public class cy_AddHy extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 设置编码（避免中文乱码）
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter(); // 用于输出提示信息

        // 2. 接收表单数据（与表单name属性一致）
        String vname = request.getParameter("vname").trim();
        String phone = request.getParameter("phone").trim();
        // 处理可选参数（若未输入，设为默认值）
        double qb = Double.parseDouble(request.getParameter("qb") == null ? "0.00" : request.getParameter("qb"));
        int jf = Integer.parseInt(request.getParameter("jf") == null ? "0" : request.getParameter("jf"));
        String addr = request.getParameter("addr") == null ? "" : request.getParameter("addr").trim();
        String jb = request.getParameter("jb") == null ? "普通会员" : request.getParameter("jb");

        // 3. 调用DAO层添加会员
        cy_HyDao hyDao = new cy_HyDao();
        String sql = "INSERT INTO cy_vip(vname, phone, qb, jf, addr, jb) VALUES(?, ?, ?, ?, ?, ?)";
        // 封装参数（注意顺序与SQL占位符一致）
        Object[] params = {vname, phone, qb, jf, addr, jb};
        int result = hyDao.addHy(sql, params); // 调用DAO的添加方法

        // 4. 处理添加结果（成功/失败）
        if (result > 0) {
            // 添加成功：提示并跳转到会员列表页
            out.print("<script>alert('会员添加成功！');window.location.href='cy_QueryHy_Page1';</script>");
        } else {
            // 添加失败：提示并返回添加页面（保留输入的内容）
            out.print("<script>alert('会员添加失败！可能手机号已存在');history.back();</script>");
        }
        out.close();
    }
}