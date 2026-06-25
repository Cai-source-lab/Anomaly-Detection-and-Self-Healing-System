package entity;

// 实体类名改为cy_goods，与表名对应
public class cy_goods {
    private int id;          // 对应表id
    private String name;// 商品名称
    private String txm;      // 条形码（txm）
    private String dw;       // 单位（dw）
    private double j_price;  // 进价（j_price）
    private double m_price;  // 卖价（m_price）
    private int zk1;         // 折扣1（zk1）
    private int zk2;         // 折扣2（zk2）
    private int kc;          // 库存（kc）
    private String ms;       // 描述（ms）
    private String dtime;    // 时间（dtime）
    private int cartQuantity; // 购物数量（临时字段）

    public cy_goods() {} // 无参构造

    public cy_goods(int id, String name, String txm, String dw, double j_price,
                    double m_price, int zk1, int zk2, int kc, String ms, String dtime) {
        this.id = id;
        this.name = name;
        this.txm = txm;
        this.dw = dw;
        this.j_price = j_price;
        this.m_price = m_price;
        this.zk1 = zk1;
        this.zk2 = zk2;
        this.kc = kc;
        this.ms = ms;
        this.dtime = dtime;
    }

    // Getter和Setter（全部与字段对应）
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    // 商品名称 - 兼容JSP页面使用的字段名
    public String getGoodsName() { return name; }
    public void setGoodsName(String name) { this.name = name; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    // 条形码 - 兼容JSP页面使用的字段名
    public String getBarCode() { return txm; }
    public void setBarCode(String txm) { this.txm = txm; }
    public String getTxm() { return txm; }
    public void setTxm(String txm) { this.txm = txm; }
    
    public String getDw() { return dw; }
    public void setDw(String dw) { this.dw = dw; }
    
    // 进价 - 兼容JSP页面使用的字段名
    public double getPurchasePrice() { return j_price; }
    public void setPurchasePrice(double j_price) { this.j_price = j_price; }
    public double getJ_price() { return j_price; }
    public void setJ_price(double j_price) { this.j_price = j_price; }
    
    // 卖价 - 兼容JSP页面使用的字段名
    public double getSellPrice() { return m_price; }
    public void setSellPrice(double m_price) { this.m_price = m_price; }
    public double getM_price() { return m_price; }
    public void setM_price(double m_price) { this.m_price = m_price; }
    
    // 折扣1 - 兼容JSP页面使用的字段名
    public int getDiscount() { return zk1; }
    public void setDiscount(int zk1) { this.zk1 = zk1; }
    public int getZk1() { return zk1; }
    public void setZk1(int zk1) { this.zk1 = zk1; }
    
    public int getZk2() { return zk2; }
    public void setZk2(int zk2) { this.zk2 = zk2; }
    
    // 库存 - 兼容JSP页面使用的字段名
    public int getStock() { return kc; }
    public void setStock(int kc) { this.kc = kc; }
    public int getKc() { return kc; }
    public void setKc(int kc) { this.kc = kc; }
    
    // 描述 - 兼容JSP页面使用的字段名
    public String getDescription() { return ms; }
    public void setDescription(String ms) { this.ms = ms; }
    public String getMs() { return ms; }
    public void setMs(String ms) { this.ms = ms; }
    
    // 进货时间 - 兼容JSP页面使用的字段名
    public String getPurchaseTime() { return dtime; }
    public void setPurchaseTime(String dtime) { this.dtime = dtime; }
    public String getDtime() { return dtime; }
    public void setDtime(String dtime) { this.dtime = dtime; }

    public int getCartQuantity() { return cartQuantity; }
    public void setCartQuantity(int cartQuantity) { this.cartQuantity = cartQuantity; }
}