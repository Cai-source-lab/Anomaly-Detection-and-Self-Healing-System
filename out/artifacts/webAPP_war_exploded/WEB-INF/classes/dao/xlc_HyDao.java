package dao;

import entity.xlc_Hy;
import untils.MysqlConn;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class xlc_HyDao {

    public int deleteHy(String sql, Object[] params) {
        MysqlConn db = new MysqlConn();
        int result = 0;
        try {
            // 执行删除操作（使用已有的doExecute方法处理增删改）
            result = db.doExecute(sql, params);
            System.out.println("删除会员结果：影响行数=" + result);
        } catch (Exception e) {
            System.out.println("删除会员异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close(); // 确保数据库连接关闭
        }
        return result;
    }


    public int editHy(String sql, Object[] params) {
        MysqlConn db = new MysqlConn();
        int result = 0;
        try {
            result = db.doExecute(sql, params);
            System.out.println("修改会员结果：影响行数=" + result);
        } catch (Exception e) {
            System.out.println("修改会员异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    // 1. 添加会员方法
    public int addHy(String sql, Object[] params) {
        MysqlConn db = new MysqlConn();
        int result = 0;
        try {
            result = db.doExecute(sql, params);
            System.out.println("添加会员结果：影响行数=" + result);
        } catch (Exception e) {
            System.out.println("添加会员异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    // 2. 获取总记录数方法
    public int getRowCount(String sql, Object[] param) {
        int res = 0;
        MysqlConn db = new MysqlConn();
        ResultSet rs = null;
        try {
            rs = db.doQuery(sql, param);
            if (rs != null && rs.next()) {
                res = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return res;
    }


    public List<xlc_Hy> QuerySomehy(String sql, Object[] param) {
        List<xlc_Hy> somehy = new ArrayList<>();
        MysqlConn db = new MysqlConn();
        ResultSet rs = null;
        try {
            rs = db.doQuery(sql, param);
            // 循环封装会员数据
            while (rs != null && rs.next()) {
                xlc_Hy hy = new xlc_Hy();
                hy.setId(rs.getInt("id"));
                hy.setVname(rs.getString("vname"));
                hy.setPhone(rs.getString("phone"));
                // 修复1：qb字段（数据库是DOUBLE类型，先获取double值再转String）
                hy.setQb(String.valueOf(rs.getDouble("qb")));
                // 修复2：jf字段（数据库是INT类型，先获取int值再转String）
                hy.setJf(String.valueOf(rs.getInt("jf")));
                hy.setAddr(rs.getString("addr"));
                hy.setJb(rs.getString("jb"));
                // 修复3：dtime字段（数据库是DATETIME类型，转String避免格式问题）
                hy.setDtime(rs.getString("dtime"));
                somehy.add(hy); // 将封装好的会员对象加入列表
            }
            System.out.println("查询到的会员数量：" + somehy.size()); // 调试用：查看是否有数据
        } catch (Exception e) {
            System.out.println("查询会员异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close(); // 确保连接关闭
        }
        return somehy;
    }

    // 4. 查询单个会员信息
    public xlc_Hy getMemberById(int memberId) {
        System.out.println("xlc_HyDao.getMemberById: 开始查询会员ID = " + memberId);
        xlc_Hy member = null;
        MysqlConn db = new MysqlConn();
        ResultSet rs = null;
        
        String sql = "SELECT * FROM xlc_vip WHERE id = ?";
        Object[] params = {memberId};
        
        try {
            System.out.println("xlc_HyDao.getMemberById: 执行SQL查询");
            rs = db.doQuery(sql, params);
            if (rs != null && rs.next()) {
                System.out.println("xlc_HyDao.getMemberById: 找到会员数据，开始封装");
                member = new xlc_Hy();
                member.setId(rs.getInt("id"));
                member.setVname(rs.getString("vname"));
                member.setPhone(rs.getString("phone"));
                member.setQb(String.valueOf(rs.getDouble("qb")));
                member.setJf(String.valueOf(rs.getInt("jf")));
                member.setAddr(rs.getString("addr"));
                member.setJb(rs.getString("jb"));
                member.setDtime(rs.getString("dtime"));
                System.out.println("xlc_HyDao.getMemberById: 会员数据封装完成 - " + member.getVname());
            } else {
                System.out.println("xlc_HyDao.getMemberById: 没有找到会员数据");
            }
        } catch (Exception e) {
            System.out.println("xlc_HyDao.getMemberById: 查询单个会员异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        System.out.println("xlc_HyDao.getMemberById: 返回结果 = " + (member != null ? "找到会员" : "未找到会员"));
        return member;
    }

    // 5. 无用方法（保留但无需修改）
    public List<xlc_Hy> doQuery(String s, Object[] objects) {
        return new ArrayList<>();
    }
}