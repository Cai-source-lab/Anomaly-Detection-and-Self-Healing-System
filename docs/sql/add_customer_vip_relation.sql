-- ============================================
-- 顧客與會員關聯功能 - 數據庫遷移腳本
-- 方案A：在顧客表中添加會員ID外鍵
-- ============================================

USE `xlc_shop`;

-- 1. 添加 vip_id 字段到 cy_customer 表
ALTER TABLE `cy_customer` 
ADD COLUMN `vip_id` INT UNSIGNED NULL COMMENT '關聯會員ID（cy_vip.id）' AFTER `phone`;

-- 2. 添加索引以提升查詢效率
ALTER TABLE `cy_customer` 
ADD INDEX `idx_cy_customer_vip_id` (`vip_id`);

-- 3. 添加外鍵約束（如果 cy_vip 表存在）
-- 注意：如果 cy_vip 表不存在，請先創建該表或修改表名
ALTER TABLE `cy_customer` 
ADD CONSTRAINT `fk_cy_customer_vip` 
  FOREIGN KEY (`vip_id`) REFERENCES `cy_vip`(`id`) 
  ON DELETE SET NULL ON UPDATE CASCADE;

-- 4. 可選：根據電話號碼自動匹配現有會員（如果需要）
-- UPDATE cy_customer c
-- INNER JOIN cy_vip v ON c.phone = v.phone
-- SET c.vip_id = v.id
-- WHERE c.vip_id IS NULL;

-- ============================================
-- 驗證腳本（可選執行）
-- ============================================
-- 查看表結構
-- DESCRIBE cy_customer;

-- 查看關聯關係
-- SELECT 
--   c.id AS customer_id,
--   c.customer_name,
--   c.phone AS customer_phone,
--   c.vip_id,
--   v.vname AS vip_name,
--   v.phone AS vip_phone,
--   v.qb AS vip_balance,
--   v.jf AS vip_points
-- FROM cy_customer c
-- LEFT JOIN cy_vip v ON c.vip_id = v.id
-- WHERE c.status = 1;




