-- 创建商品入库记录表
CREATE TABLE IF NOT EXISTS cy_stock_record02 (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    goodsName VARCHAR(100) NOT NULL COMMENT '商品名称',
    txm VARCHAR(50) NOT NULL COMMENT '条形码',
    quantity INT NOT NULL COMMENT '入库数量',
    beforeStock INT NOT NULL COMMENT '入库前库存',
    afterStock INT NOT NULL COMMENT '入库后库存',
    stockTime VARCHAR(50) NOT NULL COMMENT '入库时间',
    operator VARCHAR(50) DEFAULT '系统管理员' COMMENT '操作员',
    remark TEXT COMMENT '备注',
    INDEX idx_txm (txm),
    INDEX idx_stockTime (stockTime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品入库记录表';














