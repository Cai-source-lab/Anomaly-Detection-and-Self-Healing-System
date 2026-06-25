package action;

import dao.cy_customerDao;
import dao.cy_HyDao;
import entity.cy_customer;
import entity.cy_goods;
import entity.cy_Hy;
import untils.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 顧客零售查詢：對應 cy_shop.jsp
 */
@WebServlet("/cy_QuerySpls")
public class cy_QuerySpls extends HttpServlet {

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
            role = "customer1";
        }

        HttpSession session = request.getSession();

        cy_customerDao customerDao = new cy_customerDao();
        cy_customer customer = customerDao.getCustomerByCode(role);

        if (customer == null) {
            request.setAttribute("errorMsg", "未找到對應的顧客資料：" + role);
            request.getRequestDispatcher("cy_shop.jsp").forward(request, response);
            return;
        }

        // 獲取會員電話（從請求參數或session中的map）
        String hyphone = request.getParameter("hyphone");
        Map<String, String[]> customerMap = (Map<String, String[]>) session.getAttribute("map");
        if (hyphone == null || hyphone.trim().isEmpty()) {
            // 如果請求中沒有，嘗試從session中獲取
            if (customerMap != null) {
                String[] info = customerMap.get(role);
                if (info != null && info.length > 0) {
                    hyphone = info[0];
                }
            }
        }
        
        // 通過電話號碼查詢會員
        cy_HyDao vipDao = new cy_HyDao();
        cy_Hy vip = null;
        double discountRate = 1.0; // 默認原價（折扣率1.0）
        String displayPhone = customer.getPhone() != null ? customer.getPhone() : "";
        String memberLevel = "";
        String memberName = ""; // 會員名字
        
        if (hyphone != null && !hyphone.trim().isEmpty()) {
            // 通過電話號碼查詢會員
            vip = vipDao.getMemberByPhone(hyphone.trim());
            if (vip != null) {
                // 找到會員，使用會員信息
                displayPhone = vip.getPhone();
                memberLevel = vip.getJb() != null ? vip.getJb() : "";
                memberName = vip.getVname() != null ? vip.getVname() : "";
                
                // 根據會員級別設置折扣率（如果會員級別有折扣規則）
                // 這裡可以根據業務需求調整折扣計算邏輯
                // 暫時使用顧客的折扣率，或者根據會員級別計算
                discountRate = customer.getDiscountRate();
                
                // 如果需要根據會員級別設置折扣，可以在這裡添加邏輯
                // 例如：普通會員0.98，銀卡會員0.95，金卡會員0.90等
            } else {
                // 電話號碼對不上，按原價計算
                discountRate = 1.0;
                displayPhone = hyphone; // 顯示輸入的電話號碼
                memberLevel = "";
                memberName = ""; // 未找到會員，名字為空
            }
        } else {
            // 沒有輸入會員電話，使用顧客的默認折扣率
            discountRate = customer.getDiscountRate();
            displayPhone = customer.getPhone() != null ? customer.getPhone() : "";
            memberLevel = customer.getMemberLevel() != null ? customer.getMemberLevel() : "";
            memberName = "";
        }
        
        List<cy_goods> cartItems = customerDao.getCustomerCartItems(customer.getId());
        
        // 獲取所有顧客列表用於麵包屑導航
        List<cy_customer> allCustomers = customerDao.getAllCustomers();
        
        // 創建顯示名稱映射（顯示顧客名稱）
        Map<String, String> customerDisplayNames = new HashMap<>();
        for (cy_customer cust : allCustomers) {
            String displayName = cust.getCustomerName();
            if (displayName == null || displayName.trim().isEmpty()) {
                displayName = cust.getCustomerCode();
            }
            customerDisplayNames.put(cust.getCustomerCode(), displayName);
        }

        session.setAttribute("currentCustomerId", customer.getId());

        // 保存會員電話、名字、級別和折扣信息到session
        if (customerMap == null) {
            customerMap = new HashMap<>();
        }
        customerMap.put(role, new String[]{
                displayPhone != null ? displayPhone : "", // 會員電話或輸入的電話
                String.valueOf(discountRate), // 使用的折扣率
                memberLevel != null ? memberLevel : "", // 會員級別
                memberName != null ? memberName : "" // 會員名字
        });
        session.setAttribute("map", customerMap);
        session.setAttribute("role", role);
        session.setAttribute("SumMoney", calculateTotal(cartItems, discountRate));

        request.setAttribute("somegoodsls", cartItems);
        request.setAttribute("allCustomers", allCustomers); // 傳遞所有顧客列表給JSP
        request.setAttribute("customerDisplayNames", customerDisplayNames); // 傳遞顯示名稱映射給JSP

        Object flashMsg = session.getAttribute("flashMsg");
        if (flashMsg != null) {
            request.setAttribute("flashMsg", flashMsg);
            session.removeAttribute("flashMsg");
        }
        Object flashError = session.getAttribute("flashError");
        if (flashError != null) {
            request.setAttribute("flashError", flashError);
            session.removeAttribute("flashError");
        }

        request.getRequestDispatcher("cy_shop.jsp").forward(request, response);
    }

    private Double calculateTotal(List<cy_goods> cartItems, double discountRate) {
        double total = 0.0;
        if (cartItems != null) {
            for (cy_goods goods : cartItems) {
                int qty = goods.getCartQuantity();
                double price = goods.getSellPrice();
                total += qty * price;
            }
        }
        return total * discountRate;
    }
}


