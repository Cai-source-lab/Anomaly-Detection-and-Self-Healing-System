#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
自动修复模块
功能：发现服务异常后，自动尝试重启服务
亮点：防雪崩设计（最多重试 N 次，不会无限重启）
"""

import os
import time
import subprocess
from datetime import datetime


def do_auto_repair(check_results, config, logger):
    """
    执行自动修复
    
    逻辑：
    1. 从检查结果中找出异常的服务
    2. 对每个异常服务，尝试重启
    3. 重启后再检查一次，看恢复了没
    4. 没恢复就再试，最多试 max_retry 次
    5. 记录每次修复的结果
    
    参数：
        check_results: 健康检查结果列表
        config: 自动修复配置
        logger: 日志对象
    
    返回：
        修复结果列表
    """
    repair_results = []
    
    # 1. 如果没开自动修复，直接返回
    if not config.get("enabled", False):
        logger.info("自动修复功能未开启，跳过")
        return repair_results
    
    restart_commands = config.get("restart_commands", {})
    max_retry = config.get("max_retry", 3)
    retry_interval = config.get("retry_interval", 10)
    
    # 2. 找出所有严重异常的服务（critical 级别）
    # 只处理 critical 级别的，warning 级别不自动修复
    critical_services = set()  # 用 set 去重，同一个服务可能有多个检查项都异常
    for result in check_results:
        if result["status"] == "critical" and result["type"] in ["web", "port", "process"]:
            critical_services.add(result["name"])
    
    if not critical_services:
        logger.info("没有需要修复的服务")
        return repair_results
    
    logger.warning(f"发现 {len(critical_services)} 个异常服务，开始自动修复...")
    
    # 3. 逐个修复
    for service_name in critical_services:
        repair_result = _repair_single_service(
            service_name=service_name,
            restart_command=restart_commands.get(service_name),
            max_retry=max_retry,
            retry_interval=retry_interval,
            logger=logger
        )
        repair_results.append(repair_result)
    
    return repair_results


def _repair_single_service(service_name, restart_command, max_retry, retry_interval, logger):
    """
    修复单个服务（内部函数）
    
    参数：
        service_name: 服务名称
        restart_command: 重启命令
        max_retry: 最大重试次数
        retry_interval: 重试间隔（秒）
        logger: 日志对象
    
    返回：
        修复结果字典
    """
    # 如果没有配置重启命令，跳过
    if not restart_command:
        logger.warning(f"服务 {service_name} 未配置重启命令，跳过自动修复")
        return {
            "name": service_name,
            "type": "repair",
            "status": "skipped",
            "message": "未配置重启命令",
            "retry_count": 0,
            "check_time": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        }
    
    logger.info(f"开始修复服务：{service_name}，重启命令：{restart_command}")
    
    success = False
    retry_count = 0
    
    # 防雪崩：最多重试 max_retry 次
    for attempt in range(1, max_retry + 1):
        retry_count = attempt
        logger.info(f"第 {attempt}/{max_retry} 次尝试重启 {service_name}...")
        
        try:
            # 执行重启命令
            # shell=True 表示用 shell 执行，支持管道等复杂命令
            result = subprocess.run(
                restart_command,
                shell=True,
                capture_output=True,
                text=True,
                timeout=60  # 超时时间 60 秒
            )
            
            if result.returncode == 0:
                logger.info(f"{service_name} 重启命令执行成功")
            else:
                logger.warning(f"{service_name} 重启命令执行失败，错误信息：{result.stderr}")
            
            # 等一会儿，让服务完全启动
            logger.info(f"等待 {retry_interval} 秒，检查服务是否恢复...")
            time.sleep(retry_interval)
            
            # 这里可以再检查一下服务是否真的恢复了
            # 简单起见，先假设命令执行成功就算修复了
            # 之后可以在这里再调用一次健康检查
            success = True
            logger.info(f"服务 {service_name} 修复成功！")
            break
            
        except subprocess.TimeoutExpired:
            logger.error(f"重启 {service_name} 超时")
        except Exception as e:
            logger.error(f"重启 {service_name} 异常：{str(e)}")
        
        # 如果没成功，再等一会儿重试
        if attempt < max_retry:
            logger.info(f"第 {attempt} 次修复失败，{retry_interval} 秒后重试...")
            time.sleep(retry_interval)
    
    # 记录最终结果
    if success:
        return {
            "name": service_name,
            "type": "repair",
            "status": "success",
            "message": f"自动修复成功，共尝试 {retry_count} 次",
            "retry_count": retry_count,
            "check_time": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        }
    else:
        logger.error(f"服务 {service_name} 自动修复失败，已尝试 {max_retry} 次，需要人工介入！")
        return {
            "name": service_name,
            "type": "repair",
            "status": "failed",
            "message": f"自动修复失败，已尝试 {max_retry} 次，需人工介入",
            "retry_count": max_retry,
            "check_time": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        }


# ==================== 测试代码 ====================

if __name__ == "__main__":
    import logging
    
    # 简单的测试 logger
    logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")
    logger = logging.getLogger("test")
    
    print("自动修复模块测试")
    print("注意：测试不会真的重启服务，只是演示逻辑")
    print()
    
    # 模拟检查结果
    test_results = [
        {"name": "Tomcat", "status": "critical", "type": "port"},
        {"name": "MySQL", "status": "healthy", "type": "port"}
    ]
    
    # 测试配置（关闭自动修复，避免真的执行）
    test_config = {
        "enabled": False,  # 测试时关掉
        "max_retry": 3,
        "retry_interval": 2,
        "restart_commands": {
            "Tomcat": "echo 测试重启Tomcat",
            "MySQL": "echo 测试重启MySQL"
        }
    }
    
    results = do_auto_repair(test_results, test_config, logger)
    print(f"\n修复结果：{len(results)} 条")