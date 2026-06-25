package entity;

public class cy_Hy {
    private int id;
    private String vname;  // 原类型为int，此处修改
    private String phone;
    private String qb;
    private String jf;
    private String addr;
    private String jb;
    private String dtime;

    // 无参构造函数
    public cy_Hy() {}

    // 全参构造函数（注意vname类型改为String）
    public cy_Hy(int id, String vname, String phone, String qb, String jf, String addr, String jb, String dtime) {
        this.id = id;
        this.vname = vname;
        this.phone = phone;
        this.qb = qb;
        this.jf = jf;
        this.addr = addr;
        this.jb = jb;
        this.dtime = dtime;
    }

    // Getter和Setter
    public String getVname() {
        return vname;
    }
    public void setVname(String vname) {  // 原参数类型为int，此处修改
        this.vname = vname;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getQb() { return qb; }
    public void setQb(String qb) { this.qb = qb; }
    public String getJf() { return jf; }
    public void setJf(String jf) { this.jf = jf; }
    public String getAddr() { return addr; }
    public void setAddr(String addr) { this.addr = addr; }
    public String getJb() { return jb; }
    public void setJb(String jb) { this.jb = jb; }
    public String getDtime() { return dtime; }
    public void setDtime(String dtime) { this.dtime = dtime; }
}