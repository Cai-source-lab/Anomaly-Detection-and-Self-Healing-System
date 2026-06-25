#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
日志模块
功能：统一管理日志，同时输出到控制台和文件
"""

import os
import logging
from datetime import datetime
from logging.handlers import TimedRotatingFileHandler


def get_logger(log_dir="logs", log_level="INFO", keep_days=30):
    """
    获取日志记录器
    
    参数：
        log_dir: 日志文件存放目录
        log_level: 日志级别（DEBUG/INFO/WARNING/ERROR）
        keep_days: 日志保留天数
    
    返回：
        logger: 日志记录器对象
    """
    # 1. 创建日志目录（如果不存在）
    os.makedirs(log_dir, exist_ok=True)
    
    # 2. 创建 logger 对象
    logger = logging.getLogger("health_monitor")
    
    # 3. 设置日志级别（把字符串转成 logging 的常量）
    level = getattr(logging, log_level.upper(), logging.INFO)
    logger.setLevel(level)
    
    # 4. 避免重复添加 handler（多次调用 get_logger 不会重复输出）
    if logger.handlers:
        return logger
    
    # 5. 定义日志格式：时间 - 级别 - 消息
    log_format = "%(asctime)s - %(levelname)s - %(message)s"
    date_format = "%Y-%m-%d %H:%M:%S"
    formatter = logging.Formatter(log_format, date_format)
    
    # 6. 控制台输出 handler（在黑窗口里能看到）
    console_handler = logging.StreamHandler()
    console_handler.setFormatter(formatter)
    logger.addHandler(console_handler)
    
    # 7. 文件输出 handler（按天切割日志）
    # 日志文件名：monitor_2024-01-01.log
    log_file = os.path.join(log_dir, "monitor.log")
    file_handler = TimedRotatingFileHandler(
        filename=log_file,
        when="midnight",      # 每天凌晨切割
        interval=1,           # 每 1 天切一个
        backupCount=keep_days,  # 保留多少天的日志
        encoding="utf-8"
    )
    file_handler.setFormatter(formatter)
    logger.addHandler(file_handler)
    
    return logger


# 测试用：直接运行这个文件可以测试日志是否正常
if __name__ == "__main__":
    logger = get_logger()
    logger.debug("这是 DEBUG 日志")
    logger.info("这是 INFO 日志")
    logger.warning("这是 WARNING 日志")
    logger.error("这是 ERROR 日志")
    print("日志测试完成，请查看 logs 目录")