package action;

import dao.cy_customerDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 删除购物车商品
 */
@WebServlet("/cy_DeleteRk")
public class cy_DeleteRk extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String role = request.getParameter("role");
        if (role == null || role.trim().isEmpty()) {
            role = (String) session.getAttribute("role");
        }
        if (role == null || role.trim().isEmpty()) {
            role = "customer1";
        }

        Object customerIdAttr = session.getAttribute("currentCustomerId");
        if (customerIdAttr == null) {
            session.setAttribute("flashError", "顾客信息已过期，请重新选择顾客");
            response.sendRedirect("cy_QuerySpls?role=" + role);
            return;
        }
        int customerId = (int) customerIdAttr;

        String goodsIdStr = request.getParameter("id");
        int goodsId;
        try {
            goodsId = Integer.parseInt(goodsIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "商品参数不正确");
            response.sendRedirect("cy_QuerySpls?role=" + role);
            return;
        }

        cy_customerDao customerDao = new cy_customerDao();
        boolean success = customerDao.deleteCartItem(customerId, goodsId);
        session.setAttribute(success ? "flashMsg" : "flashError",
                success ? "商品已从清单中移除" : "删除商品失败");
        response.sendRedirect("cy_QuerySpls?role=" + role);
    }
}


