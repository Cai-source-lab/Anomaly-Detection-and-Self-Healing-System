#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
健康巡检系统 - 主入口
功能：串联所有模块，执行完整的巡检流程

使用方法：
    python monitor.py
"""

import os
import sys
import yaml
import time
from datetime import datetime

# 导入自己写的模块
from logger import get_logger
from health_checker import (
    check_web_services,
    check_ports,
    check_processes,
    check_system_resources
)
from auto_repair import do_auto_repair
from report_generator import generate_report


def load_config(config_path="config.yaml"):
    """
    加载配置文件
    
    参数：
        config_path: 配置文件路径
    
    返回：
        配置字典
    """
    if not os.path.exists(config_path):
        print(f"错误：配置文件 {config_path} 不存在！")
        sys.exit(1)
    
    try:
        with open(config_path, "r", encoding="utf-8") as f:
            config = yaml.safe_load(f)
        return config
    except Exception as e:
        print(f"加载配置文件失败：{str(e)}")
        sys.exit(1)


def main():
    """主函数：巡检总流程"""
    
    # ========== 1. 加载配置 ==========
    config = load_config()
    
    # ========== 2. 初始化日志 ==========
    log_config = config.get("log", {})
    logger = get_logger(
        log_dir="logs",
        log_level=log_config.get("level", "INFO"),
        keep_days=log_config.get("keep_days", 30)
    )
    
    logger.info("=" * 60)
    logger.info("🚀 健康巡检开始")
    logger.info("=" * 60)
    
    start_time = time.time()
    all_results = []
    
    # ========== 3. 执行各项健康检查 ==========
    
    # 3.1 Web 服务检查
    web_services = config.get("web_services", [])
    if web_services:
        logger.info("📡 开始检查 Web 服务...")
        web_results = check_web_services(web_services)
        all_results.extend(web_results)
        logger.info(f"   Web 服务检查完成，共 {len(web_results)} 项")
    
    # 3.2 端口检查
    ports = config.get("ports", [])
    if ports:
        logger.info("🔌 开始检查端口...")
        port_results = check_ports(ports)
        all_results.extend(port_results)
        logger.info(f"   端口检查完成，共 {len(port_results)} 项")
    
    # 3.3 进程检查
    processes = config.get("processes", [])
    if processes:
        logger.info("🔍 开始检查进程...")
        proc_results = check_processes(processes)
        all_results.extend(proc_results)
        logger.info(f"   进程检查完成，共 {len(proc_results)} 项")
    
    # 3.4 系统资源检查
    sys_thresholds = config.get("system_thresholds", {})
    if sys_thresholds:
        logger.info("💻 开始检查系统资源...")
        sys_results = check_system_resources(sys_thresholds)
        all_results.extend(sys_results)
        logger.info(f"   系统资源检查完成，共 {len(sys_results)} 项")
    
    # ========== 4. 统计检查结果 ==========
    healthy = sum(1 for r in all_results if r["status"] == "healthy")
    warning = sum(1 for r in all_results if r["status"] == "warning")
    critical = sum(1 for r in all_results if r["status"] == "critical")
    
    logger.info("-" * 40)
    logger.info(f"📊 检查结果统计：")
    logger.info(f"   总检查项：{len(all_results)}")
    logger.info(f"   ✅ 正常：{healthy}")
    logger.info(f"   ⚠️ 告警：{warning}")
    logger.info(f"   ❌ 严重异常：{critical}")
    
    # ========== 5. 自动修复 ==========
    repair_results = []
    if critical > 0:
        logger.info("-" * 40)
        logger.info("🔧 发现严重异常，开始自动修复...")
        repair_config = config.get("auto_repair", {})
        repair_results = do_auto_repair(all_results, repair_config, logger)
    else:
        logger.info("✅ 没有严重异常，无需修复")
    
    # ========== 6. 生成报告 ==========
    logger.info("-" * 40)
    logger.info("📝 生成健康报告...")
    
    report_config = config.get("report", {})
    report_path = generate_report(
        results=all_results,
        repair_results=repair_results,
        output_dir=report_config.get("output_dir", "logs/reports"),
        format=report_config.get("format", "markdown")
    )
    logger.info(f"   报告已生成：{report_path}")
    
    # ========== 7. 输出总结 ==========
    elapsed = time.time() - start_time
    logger.info("=" * 60)
    logger.info(f"✅ 巡检完成，耗时 {elapsed:.2f} 秒")
    logger.info(f"📄 报告文件：{report_path}")
    logger.info("=" * 60)
    
    # 如果有严重异常，返回非 0 退出码（方便定时任务判断）
    if critical > 0:
        sys.exit(2)
    elif warning > 0:
        sys.exit(1)
    else:
        sys.exit(0)


if __name__ == "__main__":
    main()