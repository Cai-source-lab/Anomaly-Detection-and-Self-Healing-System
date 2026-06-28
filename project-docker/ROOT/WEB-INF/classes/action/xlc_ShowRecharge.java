package action;

import dao.xlc_HyDao;
import entity.xlc_Hy;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 显示会员充值页面
@WebServlet("/xlc_ShowRecharge")
public class xlc_ShowRecharge extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 设置编码
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        // 2. 获取会员ID参数
        String memberIdStr = request.getParameter("id");
        System.out.println("xlc_ShowRecharge: 接收到的会员ID参数 = " + memberIdStr);
        
        if (memberIdStr == null || memberIdStr.trim().isEmpty()) {
            System.out.println("xlc_ShowRecharge: 会员ID参数为空，重定向到会员列表");
            response.sendRedirect("xlc_QueryHy_Page1");
            return;
        }

        // 3. 查询会员信息
        try {
            int memberId = Integer.parseInt(memberIdStr.trim());
            System.out.println("xlc_ShowRecharge: 解析后的会员ID = " + memberId);
            
            xlc_HyDao hyDao = new xlc_HyDao();
            System.out.println("xlc_ShowRecharge: 开始查询会员信息");
            xlc_Hy member = hyDao.getMemberById(memberId);
            
            if (member == null) {
                System.out.println("xlc_ShowRecharge: 没有找到ID为 " + memberId + " 的会员，重定向到会员列表");
                response.sendRedirect("xlc_QueryHy_Page1");
                return;
            }
            
            System.out.println("xlc_ShowRecharge: 找到会员信息 - ID:" + member.getId() + ", 姓名:" + member.getVname() + ", 余额:" + member.getQb());

            // 4. 将会员信息传递到充值页面
            request.setAttribute("memberId", member.getId());
            request.setAttribute("memberName", member.getVname());
            request.setAttribute("memberPhone", member.getPhone());
            request.setAttribute("currentBalance", member.getQb());

            // 5. 转发到充值页面
            System.out.println("xlc_ShowRecharge: 准备转发到充值页面");
            request.getRequestDispatcher("xlc_recharge.jsp").forward(request, response);
            System.out.println("xlc_ShowRecharge: 转发完成");
            
        } catch (NumberFormatException e) {
            System.out.println("xlc_ShowRecharge: 会员ID格式错误 - " + e.getMessage());
            response.sendRedirect("xlc_QueryHy_Page1");
        } catch (Exception e) {
            System.out.println("xlc_ShowRecharge: 显示充值页面异常：" + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("xlc_QueryHy_Page1");
        }
    }
}
