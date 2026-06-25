package dao;

import entity.cy_customer;
import entity.cy_goods;
import entity.cy_Hy;
import untils.MysqlConn;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 顧客相關 DAO
 */
public class cy_customerDao {

    /**
     * 依據 customer_code 取得顧客資料
     */
    public cy_customer getCustomerByCode(String customerCode) {
        if (customerCode == null || customerCode.trim().isEmpty()) {
            return null;
        }
        MysqlConn db = new MysqlConn();
        ResultSet rs = null;
        cy_customer customer = null;
        String sql = "SELECT * FROM cy_customer WHERE customer_code = ? AND status = 1";
        Object[] params = {customerCode.trim()};
        try {
            rs = db.doQuery(sql, params);
            if (rs != null && rs.next()) {
                customer = new cy_customer();
                customer.setId(rs.getInt("id"));
                customer.setCustomerCode(rs.getString("customer_code"));
                customer.setCustomerName(rs.getString("customer_name"));
                customer.setPhone(rs.getString("phone"));
                customer.setMemberLevel(rs.getString("member_level"));
                customer.setDiscountRate(rs.getDouble("discount_rate"));
            }
        } catch (Exception e) {
            System.out.println("查詢顧客資料異常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return customer;
    }

    /**
     * 取得顧客購物清單：JOIN cy_customer_cart + cy_goods
     */
    public List<cy_goods> getCustomerCartItems(int customerId) {
        List<cy_goods> items = new ArrayList<>();
        MysqlConn db = new MysqlConn();
        ResultSet rs = null;
        String sql = "SELECT g.*, cart.quantity, cart.sell_price, cart.discount_rate " +
                "FROM cy_customer_cart cart " +
                "JOIN cy_goods g ON cart.goods_id = g.id " +
                "WHERE cart.customer_id = ? AND cart.status = 'IN_CART'";
        Object[] params = {customerId};
        try {
            rs = db.doQuery(sql, params);
            while (rs != null && rs.next()) {
                cy_goods goods = new cy_goods();
                goods.setId(rs.getInt("id"));
                goods.setGoodsName(rs.getString("name"));
                goods.setTxm(rs.getString("txm"));
                goods.setDw(rs.getString("dw"));
                goods.setJ_price(rs.getDouble("j_price"));
                goods.setM_price(rs.getDouble("m_price"));
                goods.setZk1(rs.getInt("zk1"));
                goods.setZk2(rs.getInt("zk2"));
                goods.setKc(rs.getInt("kc"));
                goods.setMs(rs.getString("ms"));
                goods.setDtime(rs.getString("dtime"));
                goods.setCartQuantity(rs.getInt("quantity"));
                goods.setSellPrice(rs.getDouble("sell_price"));
                goods.setDiscount(rs.getInt("discount_rate") == 0 ? 100 : (int) (rs.getDouble("discount_rate") * 100));
                items.add(goods);
            }
        } catch (Exception e) {
            System.out.println("查詢顧客購物清單異常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return items;
    }

    /**
     * 根據顧客 ID 取得顧客資料
     */
    public cy_customer getCustomerById(int customerId) {
        MysqlConn db = new MysqlConn();
        ResultSet rs = null;
        cy_customer customer = null;
        String sql = "SELECT * FROM cy_customer WHERE id = ? AND status = 1";
        Object[] params = {customerId};
        try {
            rs = db.doQuery(sql, params);
            if (rs != null && rs.next()) {
                customer = new cy_customer();
                customer.setId(rs.getInt("id"));
                customer.setCustomerCode(rs.getString("customer_code"));
                customer.setCustomerName(rs.getString("customer_name"));
                customer.setPhone(rs.getString("phone"));
                customer.setMemberLevel(rs.getString("member_level"));
                customer.setDiscountRate(rs.getDouble("discount_rate"));
            }
        } catch (Exception e) {
            System.out.println("根據 ID 查詢顧客資料異常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return customer;
    }

    /**
     * 添加或增加购物车商品
     */
    public boolean addOrIncrementCartItem(int customerId, int goodsId, int quantity, double sellPrice, double discountRate) {
        MysqlConn queryDb = new MysqlConn();
        ResultSet rs = null;
        try {
            rs = queryDb.doQuery("SELECT id, quantity FROM cy_customer_cart WHERE customer_id = ? AND goods_id = ? AND status = 'IN_CART'",
                    new Object[]{customerId, goodsId});
            if (rs != null && rs.next()) {
                int cartId = rs.getInt("id");
                int newQuantity = rs.getInt("quantity") + quantity;
                MysqlConn updateDb = new MysqlConn();
                try {
                    int rows = updateDb.doExecute("UPDATE cy_customer_cart SET quantity = ?, sell_price = ?, discount_rate = ? WHERE id = ?",
                            new Object[]{newQuantity, sellPrice, discountRate, cartId});
                    return rows > 0;
                } finally {
                    updateDb.close();
                }
            } else {
                MysqlConn insertDb = new MysqlConn();
                try {
                    int rows = insertDb.doExecute(
                            "INSERT INTO cy_customer_cart(customer_id, goods_id, quantity, sell_price, discount_rate, status) VALUES(?, ?, ?, ?, ?, 'IN_CART')",
                            new Object[]{customerId, goodsId, quantity, sellPrice, discountRate});
                    return rows > 0;
                } finally {
                    insertDb.close();
                }
            }
        } catch (Exception e) {
            System.out.println("添加購物車商品異常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            queryDb.close();
        }
        return false;
    }

    /**
     * 更新购物车数量
     */
    public boolean updateCartItemQuantity(int customerId, int goodsId, int quantity) {
        MysqlConn db = new MysqlConn();
        try {
            int rows = db.doExecute(
                    "UPDATE cy_customer_cart SET quantity = ? WHERE customer_id = ? AND goods_id = ? AND status = 'IN_CART'",
                    new Object[]{quantity, customerId, goodsId});
            return rows > 0;
        } catch (Exception e) {
            System.out.println("更新购物车数量异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return false;
    }

    /**
     * 删除购物车商品
     */
    public boolean deleteCartItem(int customerId, int goodsId) {
        MysqlConn db = new MysqlConn();
        try {
            int rows = db.doExecute(
                    "DELETE FROM cy_customer_cart WHERE customer_id = ? AND goods_id = ? AND status = 'IN_CART'",
                    new Object[]{customerId, goodsId});
            return rows > 0;
        } catch (Exception e) {
            System.out.println("删除购物车商品异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return false;
    }

    /**
     * 清空购物车
     */
    public boolean clearCart(int customerId) {
        MysqlConn db = new MysqlConn();
        try {
            int rows = db.doExecute("DELETE FROM cy_customer_cart WHERE customer_id = ? AND status = 'IN_CART'",
                    new Object[]{customerId});
            return rows > 0;
        } catch (Exception e) {
            System.out.println("清空购物车异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return false;
    }

    /**
     * 结算购物车（标记为完成）
     */
    public boolean completeCart(int customerId) {
        MysqlConn queryDb = new MysqlConn();
        ResultSet rs = null;
        try {
            // 先檢查是否有購物車記錄
            rs = queryDb.doQuery(
                    "SELECT COUNT(*) as count FROM cy_customer_cart WHERE customer_id = ? AND status = 'IN_CART'",
                    new Object[]{customerId});
            int cartCount = 0;
            if (rs != null && rs.next()) {
                cartCount = rs.getInt("count");
            }
            System.out.println("結算購物車：顧客ID=" + customerId + "，狀態為 'IN_CART' 的購物車記錄數=" + cartCount);
            
            if (cartCount == 0) {
                System.out.println("警告：沒有找到狀態為 'IN_CART' 的購物車記錄，顧客ID=" + customerId);
                // 檢查是否有其他狀態的記錄
                queryDb.close();
                MysqlConn checkDb = new MysqlConn();
                try {
                    rs = checkDb.doQuery(
                            "SELECT COUNT(*) as count FROM cy_customer_cart WHERE customer_id = ?",
                            new Object[]{customerId});
                    if (rs != null && rs.next()) {
                        int totalCount = rs.getInt("count");
                        System.out.println("該顧客的購物車總記錄數=" + totalCount);
                        if (totalCount > 0) {
                            // 查詢所有記錄的狀態
                            checkDb.close();
                            MysqlConn statusDb = new MysqlConn();
                            try {
                                rs = statusDb.doQuery(
                                        "SELECT status, COUNT(*) as count FROM cy_customer_cart WHERE customer_id = ? GROUP BY status",
                                        new Object[]{customerId});
                                System.out.println("購物車記錄狀態分佈：");
                                while (rs != null && rs.next()) {
                                    System.out.println("  狀態: " + rs.getString("status") + ", 數量: " + rs.getInt("count"));
                                }
                            } finally {
                                statusDb.close();
                            }
                        }
                    }
                } finally {
                    checkDb.close();
                }
                return false;
            }
        } catch (Exception e) {
            System.out.println("檢查購物車記錄異常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            queryDb.close();
        }
        
        // 更新購物車狀態
        MysqlConn updateDb = new MysqlConn();
        try {
            int rows = updateDb.doExecute(
                    "UPDATE cy_customer_cart SET status = 'COMPLETED' WHERE customer_id = ? AND status = 'IN_CART'",
                    new Object[]{customerId});
            System.out.println("結算購物車：更新了 " + rows + " 條記錄，顧客ID=" + customerId);
            if (rows == 0) {
                System.out.println("警告：UPDATE 語句執行成功但沒有更新任何記錄，可能記錄狀態已改變");
            }
            return rows > 0;
        } catch (Exception e) {
            System.out.println("更新購物車狀態異常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            updateDb.close();
        }
        return false;
    }

    // ============================================
    // 顧客與會員關聯相關方法
    // ============================================

    /**
     * 獲取所有啟用的顧客列表
     */
    public List<cy_customer> getAllCustomers() {
        List<cy_customer> customers = new ArrayList<>();
        MysqlConn db = new MysqlConn();
        ResultSet rs = null;
        String sql = "SELECT * FROM cy_customer WHERE status = 1 ORDER BY customer_code";
        try {
            rs = db.doQuery(sql, null);
            while (rs != null && rs.next()) {
                cy_customer customer = new cy_customer();
                customer.setId(rs.getInt("id"));
                customer.setCustomerCode(rs.getString("customer_code"));
                customer.setCustomerName(rs.getString("customer_name"));
                customer.setPhone(rs.getString("phone"));
                customer.setMemberLevel(rs.getString("member_level"));
                customer.setDiscountRate(rs.getDouble("discount_rate"));
                customers.add(customer);
            }
        } catch (Exception e) {
            System.out.println("獲取所有顧客列表異常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return customers;
    }

}

