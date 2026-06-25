-- ============================================
-- 回滾方案A：刪除顧客與會員的外鍵關聯
-- ============================================

USE `cy_test`;

-- 1. 刪除外鍵約束
ALTER TABLE `cy_customer` 
DROP FOREIGN KEY IF EXISTS `fk_cy_customer_vip`;

-- 2. 刪除索引
ALTER TABLE `cy_customer` 
DROP INDEX IF EXISTS `idx_cy_customer_vip_id`;

-- 3. 刪除 vip_id 字段
ALTER TABLE `cy_customer` 
DROP COLUMN IF EXISTS `vip_id`;

-- ============================================
-- 驗證：查看表結構確認vip_id已刪除
-- ============================================
-- DESCRIBE cy_customer;




