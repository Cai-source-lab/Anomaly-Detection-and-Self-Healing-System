#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
健康检查模块
功能：检查 Web 服务、端口、进程、系统资源是否正常
"""

import os
import time
import socket
import psutil
import requests
from datetime import datetime


def _make_result(name, check_type, status, message, value=None):
    """
    构造统一格式的检查结果（内部工具函数）
    
    为什么要统一格式？
    - 后面生成报告方便
    - 自动修复模块也好判断
    - 所有检查项输出格式一致，规范
    
    参数：
        name: 检查项名称（比如"Tomcat"）
        check_type: 检查类型（web/port/process/system）
        status: 状态（healthy/warning/critical）
        message: 详细描述
        value: 检查到的值（可选）
    
    返回：
        字典格式的检查结果
    """
    return {
        "name": name,
        "type": check_type,
        "status": status,           # healthy=正常, warning=告警, critical=严重
        "message": message,
        "value": value,
        "check_time": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    }


# ==================== 1. Web 服务检查 ====================

def check_web_service(name, url, timeout=5, expected_status=200):
    """
    检查 Web 服务是否正常
    
    原理：
    - 用 requests 发一个 GET 请求
    - 看返回的状态码是不是期望的（比如 200）
    - 看响应时间是不是在可接受范围内
    
    参数：
        name: 服务名称
        url: 要检查的网址
        timeout: 超时时间（秒）
        expected_status: 期望的 HTTP 状态码
    
    返回：
        检查结果字典
    """
    try:
        # 记录开始时间，用于计算响应时间
        start_time = time.time()
        
        # 发请求，allow_redirects=False 不跟随重定向
        response = requests.get(url, timeout=timeout, allow_redirects=False)
        
        # 计算响应时间（毫秒）
        response_time = (time.time() - start_time) * 1000
        
        # 判断状态码
        if response.status_code == expected_status:
            # 状态码正常，再看看响应时间是不是太慢
            if response_time > 3000:  # 超过 3 秒算告警
                return _make_result(
                    name, "web", "warning",
                    f"服务响应慢，状态码 {response.status_code}，响应时间 {response_time:.0f}ms",
                    value=response_time
                )
            else:
                return _make_result(
                    name, "web", "healthy",
                    f"服务正常，状态码 {response.status_code}，响应时间 {response_time:.0f}ms",
                    value=response_time
                )
        else:
            # 状态码不对，算严重异常
            return _make_result(
                name, "web", "critical",
                f"服务异常，状态码 {response.status_code}（期望 {expected_status}）",
                value=response.status_code
            )
    
    except requests.exceptions.Timeout:
        return _make_result(name, "web", "critical", f"请求超时（{timeout}秒）")
    except requests.exceptions.ConnectionError:
        return _make_result(name, "web", "critical", "无法连接到服务")
    except Exception as e:
        return _make_result(name, "web", "critical", f"检查异常：{str(e)}")


def check_web_services(web_list):
    """
    批量检查多个 Web 服务
    
    参数：
        web_list: Web 服务配置列表
    
    返回：
        检查结果列表
    """
    results = []
    for web in web_list:
        result = check_web_service(
            name=web["name"],
            url=web["url"],
            timeout=web.get("timeout", 5),
            expected_status=web.get("expected_status", 200)
        )
        results.append(result)
    return results


# ==================== 2. 端口检查 ====================

def check_port(name, host, port, timeout=3):
    """
    检查端口是否在监听
    
    原理：
    - 用 socket 尝试连接目标 IP 和端口
    - 能连上说明端口是开的，服务在运行
    - 连不上说明端口没开，服务可能挂了
    
    参数：
        name: 服务名称
        host: 主机地址
        port: 端口号
        timeout: 超时时间（秒）
    
    返回：
        检查结果字典
    """
    try:
        # 创建 socket 对象
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.settimeout(timeout)
        
        # 尝试连接
        result = sock.connect_ex((host, port))
        sock.close()
        
        if result == 0:
            return _make_result(name, "port", "healthy", f"端口 {port} 正常监听", value=port)
        else:
            return _make_result(name, "port", "critical", f"端口 {port} 未开放", value=port)
    
    except Exception as e:
        return _make_result(name, "port", "critical", f"端口检查异常：{str(e)}")


def check_ports(port_list):
    """批量检查多个端口"""
    results = []
    for p in port_list:
        result = check_port(
            name=p["name"],
            host=p.get("host", "localhost"),
            port=p["port"]
        )
        results.append(result)
    return results


# ==================== 3. 进程检查 ====================

def check_process(name, keyword):
    """
    检查进程是否存在
    
    原理：
    - 遍历系统所有进程
    - 看进程名里有没有包含关键词
    - 有说明进程在运行
    
    参数：
        name: 服务名称
        keyword: 进程名关键词（不区分大小写）
    
    返回：
        检查结果字典
    """
    try:
        keyword_lower = keyword.lower()
        found = False
        process_count = 0
        
        # 遍历所有进程
        for proc in psutil.process_iter(['pid', 'name', 'cmdline']):
            try:
                proc_info = proc.info
                # 检查进程名和命令行参数里有没有包含关键词
                proc_name = (proc_info['name'] or '').lower()
                cmdline = ' '.join(proc_info['cmdline'] or []).lower()
                
                if keyword_lower in proc_name or keyword_lower in cmdline:
                    found = True
                    process_count += 1
            except (psutil.NoSuchProcess, psutil.AccessDenied):
                # 有些进程可能访问不到，跳过就行
                continue
        
        if found:
            return _make_result(
                name, "process", "healthy",
                f"进程正常运行，共 {process_count} 个进程",
                value=process_count
            )
        else:
            return _make_result(name, "process", "critical", f"进程不存在，关键词：{keyword}")
    
    except Exception as e:
        return _make_result(name, "process", "critical", f"进程检查异常：{str(e)}")


def check_processes(process_list):
    """批量检查多个进程"""
    results = []
    for p in process_list:
        result = check_process(name=p["name"], keyword=p["keyword"])
        results.append(result)
    return results


# ==================== 4. 系统资源检查 ====================

def check_system_resources(thresholds):
    """
    检查系统资源使用情况（CPU、内存、磁盘）
    
    参数：
        thresholds: 阈值配置字典
    
    返回：
        检查结果列表
    """
    results = []
    
    # --- 4.1 CPU 使用率 ---
    # interval=1 表示采样 1 秒，更准确
    cpu_percent = psutil.cpu_percent(interval=1)
    cpu_threshold = thresholds.get("cpu_percent", 80)
    
    if cpu_percent >= cpu_threshold:
        results.append(_make_result(
            "CPU使用率", "system", "warning",
            f"CPU 使用率过高：{cpu_percent}%（阈值 {cpu_threshold}%）",
            value=cpu_percent
        ))
    else:
        results.append(_make_result(
            "CPU使用率", "system", "healthy",
            f"CPU 使用率正常：{cpu_percent}%",
            value=cpu_percent
        ))
    
    # --- 4.2 内存使用率 ---
    mem = psutil.virtual_memory()
    mem_percent = mem.percent
    mem_threshold = thresholds.get("memory_percent", 85)
    
    # 把内存大小转成 GB，方便看
    mem_total_gb = mem.total / 1024 / 1024 / 1024
    mem_used_gb = mem.used / 1024 / 1024 / 1024
    
    if mem_percent >= mem_threshold:
        results.append(_make_result(
            "内存使用率", "system", "warning",
            f"内存使用率过高：{mem_percent}%（已用 {mem_used_gb:.1f}GB / 共 {mem_total_gb:.1f}GB）",
            value=mem_percent
        ))
    else:
        results.append(_make_result(
            "内存使用率", "system", "healthy",
            f"内存使用率正常：{mem_percent}%（已用 {mem_used_gb:.1f}GB / 共 {mem_total_gb:.1f}GB）",
            value=mem_percent
        ))
    
    # --- 4.3 磁盘使用率 ---
    disk_path = thresholds.get("disk_path", "/")
    disk_threshold = thresholds.get("disk_percent", 90)
    
    try:
        disk = psutil.disk_usage(disk_path)
        disk_percent = disk.percent
        
        # 转成 GB
        disk_total_gb = disk.total / 1024 / 1024 / 1024
        disk_used_gb = disk.used / 1024 / 1024 / 1024
        
        if disk_percent >= disk_threshold:
            results.append(_make_result(
                "磁盘使用率", "system", "critical",
                f"磁盘使用率过高：{disk_percent}%（已用 {disk_used_gb:.1f}GB / 共 {disk_total_gb:.1f}GB）",
                value=disk_percent
            ))
        else:
            results.append(_make_result(
                "磁盘使用率", "system", "healthy",
                f"磁盘使用率正常：{disk_percent}%（已用 {disk_used_gb:.1f}GB / 共 {disk_total_gb:.1f}GB）",
                value=disk_percent
            ))
    except Exception as e:
        results.append(_make_result(
            "磁盘使用率", "system", "warning",
            f"磁盘检查异常：{str(e)}"
        ))
    
    return results


# ==================== 测试代码 ====================

if __name__ == "__main__":
    print("=" * 50)
    print("健康检查模块测试")
    print("=" * 50)
    
    # 测试端口检查
    print("\n1. 测试端口检查：")
    result = check_port("测试端口", "localhost", 80)
    print(f"   {result['name']}: {result['status']} - {result['message']}")
    
    # 测试系统资源
    print("\n2. 测试系统资源：")
    sys_results = check_system_resources({
        "cpu_percent": 80,
        "memory_percent": 85,
        "disk_percent": 90
    })
    for r in sys_results:
        print(f"   {r['name']}: {r['status']} - {r['message']}")
    
    print("\n测试完成！")