package action;

import dao.cy_customerDao;
import dao.cy_goodsDao;
import entity.cy_customer;
import entity.cy_goods;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 商品扫描加入购物车
 */
@WebServlet("/cy_Spls")
public class cy_Spls extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String role = request.getParameter("role");
        if (role == null || role.trim().isEmpty()) {
            role = (String) request.getSession().getAttribute("role");
        }
        if (role == null || role.trim().isEmpty()) {
            role = "customer1";
        }

        String txm = request.getParameter("txm");
        HttpSession session = request.getSession();

        if (txm == null || txm.trim().isEmpty()) {
            session.setAttribute("flashError", "请输入商品条形码");
            response.sendRedirect("cy_QuerySpls?role=" + role);
            return;
        }

        cy_customerDao customerDao = new cy_customerDao();
        cy_customer customer = customerDao.getCustomerByCode(role);
        if (customer == null) {
            session.setAttribute("flashError", "未找到对应的顾客信息");
            response.sendRedirect("cy_QuerySpls?role=" + role);
            return;
        }

        cy_goodsDao goodsDao = new cy_goodsDao();
        cy_goods goods = goodsDao.getGoodsByTxm(txm.trim());
        if (goods == null) {
            session.setAttribute("flashError", "条形码【" + txm + "】未匹配到商品");
            response.sendRedirect("cy_QuerySpls?role=" + role);
            return;
        }

        boolean added = customerDao.addOrIncrementCartItem(
                customer.getId(),
                goods.getId(),
                1,
                goods.getM_price(),
                customer.getDiscountRate()
        );

        if (added) {
            session.setAttribute("role", role);
            session.setAttribute("flashMsg", "商品【" + goods.getGoodsName() + "】已加入购物清单");
        } else {
            session.setAttribute("flashError", "商品加入购物清单失败，请稍后再试");
        }

        response.sendRedirect("cy_QuerySpls?role=" + role);
    }
}


