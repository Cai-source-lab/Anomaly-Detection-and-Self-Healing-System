package action;

import dao.cy_HyDao;
import entity.cy_Hy;
import untils.Page;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cy_QueryHy_Page1") // 确保URL与左侧菜单链接一致
public class cy_QueryHy_Page1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        // 1. 获取查询参数：变量名与前端参数名统一（vname/jb），避免混淆
        String vname = request.getParameter("vname"); // 会员姓名（与前端name="vname"一致）
        String phone = request.getParameter("phone"); // 会员手机号
        String jb = request.getParameter("jb");       // 会员等级（与前端name="jb"一致）

        // 2. 保存查询参数用于页面回显：key与前端回显变量一致（vname/jb）
        request.setAttribute("vname", vname);
        request.setAttribute("phone", phone);
        request.setAttribute("jb", jb);

        // 3. 处理分页参数（逻辑不变）
        String pageNowStr = request.getParameter("pageNow");
        int pageNow = 1; // 当前页码（默认第1页）
        int pageSize = 5; // 每页显示5条

        if (pageNowStr != null && !pageNowStr.trim().isEmpty()) {
            try {
                pageNow = Integer.parseInt(pageNowStr);
                pageNow = Math.max(pageNow, 1); // 页码不能小于1
            } catch (NumberFormatException e) {
                pageNow = 1; // 非法参数默认第1页
            }
        }

        // 4. 初始化分页对象与DAO
        Page page = new Page();
        page.setPageNow(pageNow);
        page.setPageSize(pageSize);
        cy_HyDao hyDao = new cy_HyDao();

        // 5. 构建查询条件：字段名与数据库/实体类一致（vname/jb）
        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM cy_vip WHERE 1=1");
        StringBuilder dataSql = new StringBuilder("SELECT * FROM cy_vip WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // 5.1 姓名查询条件：用vname（匹配数据库/实体类字段）
        if (vname != null && !vname.trim().isEmpty()) {
            countSql.append(" AND vname LIKE ?"); // 错误点1：将name改为vname
            dataSql.append(" AND vname LIKE ?");
            params.add("%" + vname.trim() + "%"); // 模糊查询（支持部分匹配）
        }

        // 5.2 手机号查询条件：逻辑不变（字段phone与数据库一致）
        if (phone != null && !phone.trim().isEmpty()) {
            countSql.append(" AND phone LIKE ?");
            dataSql.append(" AND phone LIKE ?");
            params.add("%" + phone.trim() + "%");
        }

        // 5.3 会员级别查询条件：用jb（匹配数据库/实体类字段）
        if (jb != null && !jb.trim().isEmpty() && !"all".equals(jb)) {
            countSql.append(" AND jb = ?"); // 错误点2：将vip_level改为jb
            dataSql.append(" AND jb = ?");
            params.add(jb); // 级别是精确匹配，无需加%
        }

        // 6. 分页逻辑（不变）
        // 6.1 查询符合条件的总记录数
        int rowCount = hyDao.getRowCount(countSql.toString(), params.toArray());
        page.setRowCount(rowCount);

        // 6.2 计算总页数
        int pageCount = (rowCount % pageSize == 0) ? (rowCount / pageSize) : (rowCount / pageSize + 1);
        pageCount = Math.max(pageCount, 1); // 避免总页数为0
        page.setPageCount(pageCount);

        // 6.3 校正当前页码（不超过总页数）
        pageNow = Math.min(pageNow, pageCount);
        page.setPageNow(pageNow);

        // 6.4 计算分页起始位置
        int start = (pageNow - 1) * pageSize;
        page.setStart(start);

        // 7. 添加分页参数（LIMIT）
        dataSql.append(" LIMIT ?, ?");
        params.add(start);
        params.add(pageSize);

        // 8. 执行查询并传递数据
        List<cy_Hy> somehy = hyDao.QuerySomehy(dataSql.toString(), params.toArray());
        request.setAttribute("somehy", somehy);
        request.setAttribute("page", page);

        // 9. 转发到列表页
        try {
            request.getRequestDispatcher("cy_hyzl.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("Servlet转发异常：" + e.getMessage());
            e.printStackTrace();
        }
    }
}