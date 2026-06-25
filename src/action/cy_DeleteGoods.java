package action;

import dao.cy_goodsDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class cy_DeleteGoods extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleDelete(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleDelete(request, response);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                out.print("<script>alert('請選擇要刪除的商品！');window.location.href='cy_QueryHy_Page3?pageNow=1';</script>");
                return;
            }

            int id = Integer.parseInt(idStr.trim());

            cy_goodsDao goodsDao = new cy_goodsDao();
            int result = goodsDao.deleteGoodsById(id);

            if (result > 0) {
                out.print("<script>alert('商品刪除成功！');window.location.href='cy_QueryHy_Page3?pageNow=1';</script>");
            } else {
                out.print("<script>alert('商品刪除失敗，記錄不存在或已被刪除！');window.location.href='cy_QueryHy_Page3?pageNow=1';</script>");
            }
        } catch (NumberFormatException e) {
            out.print("<script>alert('參數錯誤，無法刪除商品！');window.location.href='cy_QueryHy_Page3?pageNow=1';</script>");
        } catch (Exception e) {
            String msg = e.getMessage() == null ? "未知錯誤" : e.getMessage().replace("'", "\\'");
            out.print("<script>alert('刪除失敗：" + msg + "');window.location.href='cy_QueryHy_Page3?pageNow=1';</script>");
        } finally {
            out.close();
        }
    }
}













