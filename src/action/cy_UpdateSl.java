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
 * 更新购物车数量
 */
@WebServlet("/cy_UpdateSl")
public class cy_UpdateSl extends HttpServlet {

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
        String quantityStr = request.getParameter("sl");
        int goodsId;
        int quantity;
        try {
            goodsId = Integer.parseInt(goodsIdStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "数量格式不正确");
            response.sendRedirect("cy_QuerySpls?role=" + role);
            return;
        }

        cy_customerDao customerDao = new cy_customerDao();
        boolean success;
        if (quantity <= 0) {
            success = customerDao.deleteCartItem(customerId, goodsId);
        } else {
            success = customerDao.updateCartItemQuantity(customerId, goodsId, quantity);
        }

        if (success) {
            session.setAttribute("flashMsg", "购物数量已更新");
        } else {
            session.setAttribute("flashError", "更新购物数量失败");
        }
        response.sendRedirect("cy_QuerySpls?role=" + role);
    }
}


