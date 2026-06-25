package dao;

import entity.cy_goods;
import untils.MysqlConn; // 注意包名是untils（与你的一致）
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class cy_goodsDao {

    // 分页查询商品（模仿QuerySomehy方法）
    public List<cy_goods> queryGoodsByPage(String goodsName, String txm,
                                           String minPrice, String maxPrice, String stockStatus,
                                           int start, int pageSize) {
        List<cy_goods> goodsList = new ArrayList<>();
        MysqlConn db = new MysqlConn(); // 实例化MysqlConn
        ResultSet rs = null;

        // 拼接SQL和参数（与你的查询逻辑一致）
        StringBuilder sql = new StringBuilder("SELECT * FROM cy_goods WHERE 1=1");
        List<Object> paramsList = new ArrayList<>();

        if (goodsName != null && !goodsName.trim().isEmpty()) {
            sql.append(" AND name LIKE ?"); // 数据库字段是name，不是goodsName
            paramsList.add("%" + goodsName.trim() + "%");
        }
        if (txm != null && !txm.trim().isEmpty()) {
            sql.append(" AND txm = ?");
            paramsList.add(txm.trim());
        }
        if (minPrice != null && !minPrice.trim().isEmpty()) {
            sql.append(" AND m_price >= ?");
            paramsList.add(Double.parseDouble(minPrice.trim()));
        }
        if (maxPrice != null && !maxPrice.trim().isEmpty()) {
            sql.append(" AND m_price <= ?");
            paramsList.add(Double.parseDouble(maxPrice.trim()));
        }
        if (stockStatus != null && !stockStatus.trim().isEmpty() && !"all".equals(stockStatus)) {
            switch (stockStatus) {
                case "normal":
                    sql.append(" AND kc > 5");
                    break;
                case "warning":
                    sql.append(" AND kc <= 5 AND kc > 0");
                    break;
                case "out":
                    sql.append(" AND kc = 0");
                    break;
            }
        }

        // 分页条件
        sql.append(" LIMIT ?, ?");
        paramsList.add(start);
        paramsList.add(pageSize);

        // 转换为数组（适配doQuery的参数类型）
        Object[] params = paramsList.toArray(new Object[0]);

        try {
            // 调用MysqlConn的doQuery方法执行查询
            rs = db.doQuery(sql.toString(), params);

            // 封装结果集（与你的会员查询逻辑一致）
            while (rs != null && rs.next()) {
                cy_goods goods = new cy_goods();
                goods.setId(rs.getInt("id"));
                goods.setGoodsName(rs.getString("name")); // 数据库字段是name，不是goodsName
                goods.setTxm(rs.getString("txm"));
                goods.setDw(rs.getString("dw"));
                goods.setJ_price(rs.getDouble("j_price"));
                goods.setM_price(rs.getDouble("m_price"));
                goods.setZk1(rs.getInt("zk1"));
                goods.setZk2(rs.getInt("zk2"));
                goods.setKc(rs.getInt("kc"));
                goods.setMs(rs.getString("ms"));
                goods.setDtime(rs.getString("dtime"));
                goodsList.add(goods);
            }
            System.out.println("查询到的商品数量：" + goodsList.size());
        } catch (Exception e) {
            System.out.println("商品分页查询异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close(); // 关闭连接（与你的风格一致）
        }
        return goodsList;
    }

    // 查询总数量（模仿getRowCount方法）
    public int getGoodsCount(String goodsName, String txm, String minPrice, String maxPrice, String stockStatus) {
        int count = 0;
        MysqlConn db = new MysqlConn();
        ResultSet rs = null;

        // 拼接SQL和参数
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS total FROM cy_goods WHERE 1=1");
        List<Object> paramsList = new ArrayList<>();

        if (goodsName != null && !goodsName.trim().isEmpty()) {
            sql.append(" AND name LIKE ?"); // 数据库字段是name，不是goodsName
            paramsList.add("%" + goodsName.trim() + "%");
        }
        if (txm != null && !txm.trim().isEmpty()) {
            sql.append(" AND txm = ?");
            paramsList.add(txm.trim());
        }
        if (minPrice != null && !minPrice.trim().isEmpty()) {
            sql.append(" AND m_price >= ?");
            paramsList.add(Double.parseDouble(minPrice.trim()));
        }
        if (maxPrice != null && !maxPrice.trim().isEmpty()) {
            sql.append(" AND m_price <= ?");
            paramsList.add(Double.parseDouble(maxPrice.trim()));
        }
        if (stockStatus != null && !stockStatus.trim().isEmpty() && !"all".equals(stockStatus)) {
            switch (stockStatus) {
                case "normal":
                    sql.append(" AND kc > 5");
                    break;
                case "warning":
                    sql.append(" AND kc <= 5 AND kc > 0");
                    break;
                case "out":
                    sql.append(" AND kc = 0");
                    break;
            }
        }

        Object[] params = paramsList.toArray(new Object[0]);

        try {
            rs = db.doQuery(sql.toString(), params);
            if (rs != null && rs.next()) {
                count = rs.getInt("total"); // 获取总数量
            }
        } catch (Exception e) {
            System.out.println("商品数量查询异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close(); // 关闭连接
        }
        return count;
    }

    // 根据条形码查询商品
    public cy_goods getGoodsByTxm(String txm) {
        cy_goods goods = null;
        MysqlConn db = new MysqlConn();
        ResultSet rs = null;
        
        String sql = "SELECT * FROM cy_goods WHERE txm = ?";
        Object[] params = {txm};
        
        try {
            rs = db.doQuery(sql, params);
            if (rs != null && rs.next()) {
                goods = new cy_goods();
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
            }
        } catch (Exception e) {
            System.out.println("根据条形码查询商品异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return goods;
    }

    // 删除商品（模仿deleteHy方法）
    public int deleteGoodsById(int id) { // 注意：返回值改为int（与你的deleteHy一致）
        MysqlConn db = new MysqlConn();
        int result = 0;
        String sql = "DELETE FROM cy_goods WHERE id = ?";
        Object[] params = {id}; // 参数数组

        try {
            // 调用doExecute执行删除（与你的增删改逻辑一致）
            result = db.doExecute(sql, params);
            System.out.println("删除商品结果：影响行数=" + result);
        } catch (Exception e) {
            System.out.println("删除商品异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    // 新增：添加商品方法（模仿addHy）
    public int addGoods(String sql, Object[] params) {
        MysqlConn db = new MysqlConn();
        int result = 0;
        try {
            result = db.doExecute(sql, params);
            System.out.println("添加商品结果：影响行数=" + result);
        } catch (Exception e) {
            System.out.println("添加商品异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    // 新增：修改商品方法（模仿editHy）
    public int editGoods(String sql, Object[] params) {
        MysqlConn db = new MysqlConn();
        int result = 0;
        try {
            result = db.doExecute(sql, params);
            System.out.println("修改商品结果：影响行数=" + result);
        } catch (Exception e) {
            System.out.println("修改商品异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    /**
     * 提供給零售頁面使用：列出所有可售商品（庫存>0）
     */
    public List<cy_goods> listRetailGoods() {
        List<cy_goods> goodsList = new ArrayList<>();
        MysqlConn db = new MysqlConn();
        ResultSet rs = null;
        String sql = "SELECT * FROM cy_goods WHERE kc > 0 ORDER BY dtime DESC, id DESC";
        try {
            rs = db.doQuery(sql, null);
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
                goodsList.add(goods);
            }
        } catch (Exception e) {
            System.out.println("查詢可售商品列表異常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return goodsList;
    }

    /**
     * 零售扣庫：基於商品ID減少庫存，並避免庫存變成負數
     */
    public boolean decreaseStockForSale(int goodsId, int saleQuantity) {
        if (saleQuantity <= 0) {
            return false;
        }
        MysqlConn db = new MysqlConn();
        String sql = "UPDATE cy_goods SET kc = kc - ? WHERE id = ? AND kc >= ?";
        Object[] params = {saleQuantity, goodsId, saleQuantity};
        try {
            int affected = db.doExecute(sql, params);
            return affected > 0;
        } catch (Exception e) {
            System.out.println("零售扣庫異常：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }
}