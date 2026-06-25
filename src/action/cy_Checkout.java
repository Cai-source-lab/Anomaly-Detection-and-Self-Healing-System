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
import java.util.List;

/**
 * 商品结算
 */
@WebServlet("/cy_Checkout")
public class cy_Checkout extends HttpServlet {

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
        List<cy_goods> cartItems = customerDao.getCustomerCartItems(customerId);

        if (cartItems == null || cartItems.isEmpty()) {
            session.setAttribute("flashError", "购物清单为空，无法结算");
            response.sendRedirect("cy_QuerySpls?role=" + role);
            return;
        }

        cy_goodsDao goodsDao = new cy_goodsDao();
        for (cy_goods item : cartItems) {
            int cartQty = item.getCartQuantity();
            if (cartQty <= 0) {
                session.setAttribute("flashError", "存在数量为0的商品，请先调整数量");
                response.sendRedirect("cy_QuerySpls?role=" + role);
                return;
            }
            int currentStock = item.getKc();
            if (currentStock < cartQty) {
                session.setAttribute("flashError", "商品【" + item.getGoodsName() + "】库存不足");
                response.sendRedirect("cy_QuerySpls?role=" + role);
                return;
            }
        }

        // 更新庫存
        boolean stockUpdated = true;
        String stockErrorMsg = null;
        for (cy_goods item : cartItems) {
            int newStock = item.getKc() - item.getCartQuantity();
            int rows = goodsDao.editGoods("UPDATE cy_goods SET kc = ? WHERE id = ?",
                    new Object[]{newStock, item.getId()});
            if (rows <= 0) {
                stockUpdated = false;
                stockErrorMsg = "商品【" + item.getGoodsName() + "】庫存更新失敗";
                System.out.println("結算失敗：庫存更新失敗，商品ID=" + item.getId() + "，商品名稱=" + item.getGoodsName());
                break;
            }
            System.out.println("庫存更新成功：商品ID=" + item.getId() + "，原庫存=" + item.getKc() + "，新庫存=" + newStock);
        }

        if (!stockUpdated) {
            session.setAttribute("flashError", stockErrorMsg != null ? stockErrorMsg : "结算过程中更新库存失败，请稍后再试");
            response.sendRedirect("cy_QuerySpls?role=" + role);
            return;
        }

        // 標記購物車為完成狀態
        System.out.println("開始結算購物車，顧客ID=" + customerId);
        boolean completed = customerDao.completeCart(customerId);
        if (!completed) {
            System.out.println("結算失敗：completeCart 返回 false，顧客ID=" + customerId);
            // 嘗試回滾庫存（雖然不是事務，但至少記錄錯誤）
            System.out.println("警告：購物車標記失敗，但庫存已更新，可能需要手動檢查數據一致性");
            session.setAttribute("flashError", "结算失败，请稍后再试。如果库存已扣减，请联系管理员检查");
            response.sendRedirect("cy_QuerySpls?role=" + role);
            return;
        }

        session.setAttribute("flashMsg", "结算成功，欢迎下次光临！");
        session.setAttribute("SumMoney", 0.0);
        response.sendRedirect("cy_QuerySpls?role=" + role);
    }
}


