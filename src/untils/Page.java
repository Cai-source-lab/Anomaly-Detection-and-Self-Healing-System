package untils;

public class Page {
    private int pageNow = 1; // 当前页码（默认第1页）
    private int pageSize = 5; // 每页条数（默认5条，与需求一致）
    private int pageCount = 1; // 总页数（默认1页）
    private int rowCount = 0; // 总记录数（默认0）
    private int start = 0; // SQL查询起始位置

    // Getter和Setter（必须完整，否则JSP无法获取值）
    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    // 移除原setPage方法（避免与Servlet中的计算冲突）
}