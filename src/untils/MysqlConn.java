package untils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MysqlConn {
    private String Driver = "com.mysql.cj.jdbc.Driver";
    // 默认数据库（会员库）：保持不变，确保会员功能不受影响
    private String defaultDb = "cy_test";
    private String url; // 动态生成URL（根据数据库名）
    private String user = "root";
    private String password = "123456";
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    // 1. 默认构造方法（会员功能用）：连接cy_test库，完全兼容原有逻辑
    public MysqlConn() {
        this.url = "jdbc:mysql://localhost:3306/" + defaultDb + "?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false";
    }

    // 2. 新增重载构造方法（商品功能用）：允许指定数据库名
    public MysqlConn(String dbName) {
        this.url = "jdbc:mysql://localhost:3306/" + dbName + "?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false";
    }

    // 获取数据库连接（保持原有逻辑，增加当前连接的库名打印）
    public void getConnect() {
        try {
            Class.forName(Driver);
            conn = DriverManager.getConnection(url, user, password);
            // 打印当前连接的数据库名，方便调试
            if (conn != null && !conn.isClosed()) {
                System.out.println("数据库连接成功！当前库：" + (url.contains(defaultDb) ? defaultDb : url.split("/")[3].split("\\?")[0]));
            } else {
                System.out.println("数据库连接失败：conn为空或已关闭");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("驱动加载失败：" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("数据库连接异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 执行查询（保持不变）
    public ResultSet doQuery(String sql, Object[] param) {
        try {
            this.getConnect();
            if (conn == null || conn.isClosed()) {
                System.out.println("doQuery失败：数据库连接未建立");
                return null;
            }
            pstmt = conn.prepareStatement(sql);
            if (param != null && param.length > 0) {
                for (int i = 0; i < param.length; i++) {
                    pstmt.setObject(i + 1, param[i]);
                }
            }
            rs = pstmt.executeQuery();
        } catch (Exception e) {
            System.out.println("doQuery执行失败：" + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }

    // 执行增删改（保持不变）
    public int doExecute(String sql, Object[] param) {
        int res = 0;
        try {
            this.getConnect();
            if (conn == null || conn.isClosed()) {
                System.out.println("doExecute失败：数据库连接未建立");
                return res;
            }
            pstmt = conn.prepareStatement(sql);
            if (param != null && param.length > 0) {
                for (int i = 0; i < param.length; i++) {
                    pstmt.setObject(i + 1, param[i]);
                }
            }
            res = pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("doExecute执行失败：" + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    // 关闭资源（保持不变）
    public void close() {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
                System.out.println("数据库连接已关闭");
            }
        } catch (Exception e) {
            System.out.println("关闭资源失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}