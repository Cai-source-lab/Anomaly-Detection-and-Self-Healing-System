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
 * 清空购物车
 */
@WebServlet("/cy_ClearCart")
public class cy_ClearCart extends HttpServlet {

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

        cy_customerDao customerDao = new cy_customerDao();
        boolean success = customerDao.clearCart(customerId);
        session.setAttribute(success ? "flashMsg" : "flashError",
                success ? "购物清单已清空" : "清空购物清单失败");
        session.setAttribute("SumMoney", 0.0);
        response.sendRedirect("cy_QuerySpls?role=" + role);
    }
}


