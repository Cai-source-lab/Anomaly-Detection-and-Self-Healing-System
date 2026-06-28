package action;

import dao.xlc_HyDao;
import entity.xlc_Hy;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

// 会员充值功能
@WebServlet("/xlc_Recharge")
public class xlc_Recharge extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 设置编码
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();

        // 2. 获取参数
        String memberIdStr = request.getParameter("memberId");
        String amountStr = request.getParameter("amount");
        String paymentMethod = request.getParameter("paymentMethod");

        // 3. 参数验证
        if (memberIdStr == null || memberIdStr.trim().isEmpty()) {
            out.print("<script>alert('会员ID不能为空！');history.back();</script>");
            out.close();
            return;
        }

        if (amountStr == null || amountStr.trim().isEmpty()) {
            out.print("<script>alert('充值金额不能为空！');history.back();</script>");
            out.close();
            return;
        }

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            out.print("<script>alert('请选择充值方式！');history.back();</script>");
            out.close();
            return;
        }

        // 4. 数据类型转换和验证
        int memberId;
        double amount;
        try {
            memberId = Integer.parseInt(memberIdStr.trim());
        } catch (NumberFormatException e) {
            out.print("<script>alert('会员ID格式错误！');history.back();</script>");
            out.close();
            return;
        }

        try {
            amount = Double.parseDouble(amountStr.trim());
            if (amount <= 0) {
                out.print("<script>alert('充值金额必须大于0！');history.back();</script>");
                out.close();
                return;
            }
        } catch (NumberFormatException e) {
            out.print("<script>alert('充值金额格式错误！');history.back();</script>");
            out.close();
            return;
        }

        // 5. 查询当前会员信息
        xlc_HyDao hyDao = new xlc_HyDao();
        xlc_Hy member = hyDao.getMemberById(memberId);
        if (member == null) {
            out.print("<script>alert('会员不存在！');history.back();</script>");
            out.close();
            return;
        }

        // 6. 计算新的余额
        double currentBalance = Double.parseDouble(member.getQb());
        double newBalance = currentBalance + amount;

        // 7. 更新会员余额
        String updateSql = "UPDATE xlc_vip SET qb = ? WHERE id = ?";
        Object[] updateParams = {newBalance, memberId};
        
        int result = hyDao.editHy(updateSql, updateParams);

        // 8. 处理结果
        if (result > 0) {
            // 充值成功
            String successMsg = String.format("充值成功！\\n会员：%s\\n充值金额：¥%.2f\\n充值方式：%s\\n原余额：¥%.2f\\n新余额：¥%.2f", 
                member.getVname(), amount, paymentMethod, currentBalance, newBalance);
            out.print("<script>alert('" + successMsg + "');window.location.href='xlc_QueryHy_Page1';</script>");
        } else {
            // 充值失败
            out.print("<script>alert('充值失败，请重试！');history.back();</script>");
        }
        out.close();
    }

}
