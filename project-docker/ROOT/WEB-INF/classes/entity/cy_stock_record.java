package entity;

/**
 * 商品入库记录实体类
 */
public class cy_stock_record {
    private int id;              // 记录ID
    private String goodsName;    // 商品名称
    private String txm;          // 条形码
    private int quantity;        // 入库数量
    private int beforeStock;     // 入库前库存
    private int afterStock;      // 入库后库存
    private String stockTime;    // 入库时间
    private String operator;    // 操作员
    private String remark;       // 备注
    
    // 无参构造函数
    public cy_stock_record() {}
    
    // 全参构造函数
    public cy_stock_record(int id, String goodsName, String txm, int quantity, 
                          int beforeStock, int afterStock, String stockTime, 
                          String operator, String remark) {
        this.id = id;
        this.goodsName = goodsName;
        this.txm = txm;
        this.quantity = quantity;
        this.beforeStock = beforeStock;
        this.afterStock = afterStock;
        this.stockTime = stockTime;
        this.operator = operator;
        this.remark = remark;
    }
    
    // Getter和Setter方法
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getGoodsName() {
        return goodsName;
    }
    
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    
    public String getTxm() {
        return txm;
    }
    
    public void setTxm(String txm) {
        this.txm = txm;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public int getBeforeStock() {
        return beforeStock;
    }
    
    public void setBeforeStock(int beforeStock) {
        this.beforeStock = beforeStock;
    }
    
    public int getAfterStock() {
        return afterStock;
    }
    
    public void setAfterStock(int afterStock) {
        this.afterStock = afterStock;
    }
    
    public String getStockTime() {
        return stockTime;
    }
    
    public void setStockTime(String stockTime) {
        this.stockTime = stockTime;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public void setOperator(String operator) {
        this.operator = operator;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
