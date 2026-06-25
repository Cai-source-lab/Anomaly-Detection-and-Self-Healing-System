package dao;

import entity.cy_stock_record;
import untils.MysqlConn;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品入库记录DAO类
 */
public class cy_stock_recordDao {
    
    // 添加入库记录
    public int addStockRecord(cy_stock_record record) {
        MysqlConn db = new MysqlConn();
        ResultSet rs = null;
        
        String sql = "INSERT INTO cy_stock_record(goodsName, txm, quantity, beforeStock, afterStock, stockTime, operator, remark) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {
            record.getGoodsName(),
            record.getTxm(),
            record.getQuantity(),
            record.getBeforeStock(),
            record.getAfterStock(),
            record.getStockTime(),
            record.getOperator(),
            record.getRemark()
        };
        
        try {
            int result = db.doExecute(sql, params);
            System.out.println("入库记录添加结果：" + result);
            return result;
        } catch (Exception e) {
            System.out.println("添加入库记录异常：" + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            db.close();
        }
    }
    
    // 分页查询入库记录
    public List<cy_stock_record> getStockRecordsByPage(int pageNow, int pageSize) {
        List<cy_stock_record> records = new ArrayList<>();
        MysqlConn db = new MysqlConn();
        ResultSet rs = null;
        
        int offset = (pageNow - 1) * pageSize;
        String sql = "SELECT * FROM cy_stock_record ORDER BY stockTime DESC LIMIT ?, ?";
        Object[] params = {offset, pageSize};
        
        try {
            rs = db.doQuery(sql, params);
            while (rs != null && rs.next()) {
                cy_stock_record record = new cy_stock_record();
                record.setId(rs.getInt("id"));
                record.setGoodsName(rs.getString("goodsName"));
                record.setTxm(rs.getString("txm"));
                record.setQuantity(rs.getInt("quantity"));
                record.setBeforeStock(rs.getInt("beforeStock"));
                record.setAfterStock(rs.getInt("afterStock"));
                record.setStockTime(rs.getString("stockTime"));
                record.setOperator(rs.getString("operator"));
                record.setRemark(rs.getString("remark"));
                records.add(record);
            }
        } catch (Exception e) {
            System.out.println("查询入库记录异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return records;
    }
    
    // 获取入库记录总数
    public int getStockRecordCount() {
        MysqlConn db = new MysqlConn();
        ResultSet rs = null;
        int count = 0;
        
        String sql = "SELECT COUNT(*) FROM cy_stock_record";
        
        try {
            rs = db.doQuery(sql, null);
            if (rs != null && rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("查询入库记录总数异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return count;
    }
}
