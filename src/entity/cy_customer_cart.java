package entity;

import java.io.Serializable;

/**
 * 顧客購買商品/購物車實體：對應 cy_customer_cart 表
 */
public class cy_customer_cart implements Serializable {
    private long id;
    private int customerId;
    private int goodsId;
    private int quantity;
    private double sellPrice;
    private double discountRate;
    private String status; // IN_CART / PAID / CANCELLED

    public cy_customer_cart() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}





