-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: cy_test
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cy_customer`
--

DROP TABLE IF EXISTS `cy_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cy_customer` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `customer_code` varchar(32) NOT NULL COMMENT '如 customer1/customer2',
  `customer_name` varchar(50) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `member_level` varchar(20) DEFAULT '普通顧客',
  `discount_rate` decimal(5,2) NOT NULL DEFAULT '1.00' COMMENT '折扣，1 等於不打折',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1=啟用 0=停用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_code` (`customer_code`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cy_customer`
--

LOCK TABLES `cy_customer` WRITE;
/*!40000 ALTER TABLE `cy_customer` DISABLE KEYS */;
INSERT INTO `cy_customer` VALUES (1,'customer1','顾客A','13800000001','普通顧客',1.00,1,'2025-11-19 17:58:07','2025-12-31 17:45:33'),(2,'customer2','顾客B','13800000002','銀卡會員',0.95,1,'2025-11-19 17:58:07','2025-12-31 17:45:39'),(3,'customer3','顾客C','13800000003','金卡會員',0.90,1,'2025-11-19 17:58:07','2025-12-31 17:45:44'),(4,'customer4','顾客D','13800000004','鉑金會員',0.85,1,'2025-11-19 17:58:07','2025-12-31 17:45:54');
/*!40000 ALTER TABLE `cy_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cy_customer_cart`
--

DROP TABLE IF EXISTS `cy_customer_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cy_customer_cart` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` int NOT NULL COMMENT '顾客ID，对应 cy_customer.id',
  `goods_id` int NOT NULL COMMENT '商品ID，对应 cy_goods.id',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '购买数量',
  `sell_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '成交单价',
  `discount_rate` decimal(5,2) NOT NULL DEFAULT '1.00' COMMENT '折扣(0~1)',
  `status` enum('IN_CART','COMPLETED') NOT NULL DEFAULT 'IN_CART' COMMENT '状态：购物中/已结算',
  `checkout_time` datetime DEFAULT NULL COMMENT '结算时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_customer_goods` (`customer_id`,`goods_id`,`status`),
  KEY `idx_customer` (`customer_id`,`status`),
  KEY `idx_goods` (`goods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='顾客购物清单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cy_customer_cart`
--

LOCK TABLES `cy_customer_cart` WRITE;
/*!40000 ALTER TABLE `cy_customer_cart` DISABLE KEYS */;
INSERT INTO `cy_customer_cart` VALUES (1,1,4,1,900.00,1.00,'COMPLETED',NULL,'2025-11-26 16:37:09','2025-11-26 16:37:23'),(2,1,6,1,30.00,1.00,'COMPLETED',NULL,'2025-11-26 16:37:18','2025-11-26 16:37:23'),(7,1,9,1,120.00,1.00,'COMPLETED',NULL,'2026-01-06 11:08:27','2026-01-06 11:34:06'),(9,1,8,1,1100.00,1.00,'COMPLETED',NULL,'2026-01-06 13:13:43','2026-01-06 13:13:55');
/*!40000 ALTER TABLE `cy_customer_cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cy_goods`
--

DROP TABLE IF EXISTS `cy_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cy_goods` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL,
  `txm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL,
  `dw` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL,
  `j_price` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL,
  `m_price` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL,
  `zk1` int DEFAULT NULL,
  `zk2` int DEFAULT NULL,
  `kc` int DEFAULT NULL,
  `ms` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL,
  `dtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cy_goods`
--

LOCK TABLES `cy_goods` WRITE;
/*!40000 ALTER TABLE `cy_goods` DISABLE KEYS */;
INSERT INTO `cy_goods` VALUES (2,'衬衫','987654321','件','50','78',89,92,78,'李宁','2025-11-15 17:10:46'),(3,'鞋子','15462106','双','188','288',82,89,23,'安踏','2025-10-03 17:11:33'),(4,'艾路底壳卫衣','1111111111','件','800.0','900.0',80,91,81,'李宁','2025-10-22 00:00:00'),(6,'纯牛奶','333','件','20.0','30.0',100,100,287,'','2025-11-19 00:00:00'),(8,'皮鞋','666','只','555.0','1100.0',100,100,49,'','2025-12-31 00:00:00'),(9,'裤子','6666','只','80.0','120.0',66,77,148,'','2026-01-06 00:00:00');
/*!40000 ALTER TABLE `cy_goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cy_stock_record`
--

DROP TABLE IF EXISTS `cy_stock_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cy_stock_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `goodsName` varchar(100) NOT NULL COMMENT '商品名称',
  `txm` varchar(50) NOT NULL COMMENT '条形码',
  `quantity` int NOT NULL COMMENT '入库数量',
  `beforeStock` int NOT NULL COMMENT '入库前库存',
  `afterStock` int NOT NULL COMMENT '入库后库存',
  `stockTime` varchar(50) NOT NULL COMMENT '入库时间',
  `operator` varchar(50) DEFAULT '系统管理员' COMMENT '操作员',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_txm` (`txm`),
  KEY `idx_stockTime` (`stockTime`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品入库记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cy_stock_record`
--

LOCK TABLES `cy_stock_record` WRITE;
/*!40000 ALTER TABLE `cy_stock_record` DISABLE KEYS */;
INSERT INTO `cy_stock_record` VALUES (1,'艾路底壳卫衣','1111111111',2,77,79,'2025-10-22 17:53:53','系统管理员',''),(2,'艾路底壳卫衣','1111111111',4,79,83,'2025-10-22 17:54:40','系统管理员',''),(3,'艾路底壳卫衣','1111111111',6,83,89,'2025-10-22 18:01:13','系统管理员',''),(4,'鞋子','15462106',10,13,23,'2025-10-29 14:51:33','系统管理员','tzhdsb'),(5,'艾路底壳卫衣','1111111111',1,90,91,'2025-11-26 14:52:26','系统管理员',''),(6,'纯牛奶','333',222,66,288,'2025-12-31 17:43:14','系统管理员','');
/*!40000 ALTER TABLE `cy_stock_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cy_vip`
--

DROP TABLE IF EXISTS `cy_vip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cy_vip` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL,
  `qb` double DEFAULT NULL,
  `jf` int DEFAULT NULL,
  `addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL,
  `jb` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL,
  `dtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cy_vip`
--

LOCK TABLES `cy_vip` WRITE;
/*!40000 ALTER TABLE `cy_vip` DISABLE KEYS */;
INSERT INTO `cy_vip` VALUES (1,'cy','19922966188',1150,100,'ccbw','黄金会员','2025-09-16 09:44:20'),(2,'ycq','19945688762',500,200,'ccibe','普通会员','2025-09-19 09:44:54'),(3,'林俊杰','19956299845',0,0,'','钻石会员',NULL),(4,'tzh','15657586524',0,0,'四季大粪','钻石会员',NULL),(6,'蔡宇','15823989745',800,20,'','银卡会员',NULL);
/*!40000 ALTER TABLE `cy_vip` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-28  0:18:46
