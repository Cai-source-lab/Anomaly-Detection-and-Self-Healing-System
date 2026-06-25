-- 建議在 MySQL 8.0+ 環境執行
DROP DATABASE IF EXISTS `xlc_shop`;
CREATE DATABASE `xlc_shop`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;
USE `xlc_shop`;

-- 後台管理帳號
CREATE TABLE `xlc_admin` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(100) NOT NULL,
  `nickname` VARCHAR(50) DEFAULT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '1=啟用 0=停用',
  `last_login_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `xlc_admin` (`username`, `password`, `nickname`)
VALUES ('admin', '123456', '超級管理員');

-- 會員資料
CREATE TABLE `xlc_vip` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `vname` VARCHAR(50) NOT NULL,
  `phone` VARCHAR(20) NOT NULL UNIQUE,
  `qb` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '帳戶餘額',
  `jf` INT NOT NULL DEFAULT 0 COMMENT '積分',
  `addr` VARCHAR(255) DEFAULT NULL,
  `jb` VARCHAR(20) DEFAULT '普通會員',
  `dtime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 商品主資料（對應 entity.cy_goods）
CREATE TABLE `cy_goods` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '商品名稱',
  `txm` VARCHAR(64) NOT NULL COMMENT '條形碼',
  `dw` VARCHAR(20) NOT NULL COMMENT '單位',
  `j_price` DECIMAL(10,2) NOT NULL COMMENT '進價',
  `m_price` DECIMAL(10,2) NOT NULL COMMENT '賣價',
  `zk1` INT NOT NULL DEFAULT 100 COMMENT '折扣1(%)',
  `zk2` INT NOT NULL DEFAULT 100 COMMENT '折扣2(%)',
  `kc` INT NOT NULL DEFAULT 0 COMMENT '庫存',
  `ms` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  `dtime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入庫時間',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cy_goods_txm` (`txm`),
  INDEX `idx_cy_goods_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 商品入庫/調整紀錄（對應 entity.cy_stock_record）
CREATE TABLE `cy_stock_record02` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `goodsName` VARCHAR(100) NOT NULL,
  `txm` VARCHAR(64) NOT NULL,
  `quantity` INT NOT NULL,
  `beforeStock` INT NOT NULL,
  `afterStock` INT NOT NULL,
  `stockTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `operator` VARCHAR(50) DEFAULT '系統',
  `remark` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_cy_stock_record_txm` (`txm`),
  INDEX `idx_cy_stock_record_time` (`stockTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始範例資料
INSERT INTO `cy_goods`
  (`name`, `txm`, `dw`, `j_price`, `m_price`, `zk1`, `zk2`, `kc`, `ms`)
VALUES
  ('純牛奶250ml', '6901234567890', '盒', 2.80, 4.50, 100, 95, 200, '常溫乳品'),
  ('高山茶500ml', '6934567890123', '瓶', 3.20, 5.00, 100, 90, 150, '無糖綠茶'),
  ('礦泉水550ml', '6923456789012', '瓶', 1.00, 2.00, 100, 100, 300, 'PET 礦泉水');

INSERT INTO `cy_stock_record02`
  (`goodsName`, `txm`, `quantity`, `beforeStock`, `afterStock`, `operator`, `remark`)
VALUES
  ('純牛奶250ml', '6901234567890', 200, 0, 200, '系統', '初始化庫存'),
  ('高山茶500ml', '6934567890123', 150, 0, 150, '系統', '初始化庫存'),
  ('礦泉水550ml', '6923456789012', 300, 0, 300, '系統', '初始化庫存');

