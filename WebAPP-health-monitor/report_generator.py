#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
报告生成模块
功能：把巡检结果整理成好看的报告
"""

import os
from datetime import datetime


def generate_report(results, repair_results, output_dir="logs/reports", format="markdown"):
    """
    生成健康报告
    
    参数：
        results: 健康检查结果列表
        repair_results: 自动修复结果列表
        output_dir: 报告输出目录
        format: 报告格式（markdown/txt）
    
    返回：
        报告文件路径
    """
    # 1. 创建输出目录
    os.makedirs(output_dir, exist_ok=True)
    
    # 2. 统计数据
    total = len(results)
    healthy_count = sum(1 for r in results if r["status"] == "healthy")
    warning_count = sum(1 for r in results if r["status"] == "warning")
    critical_count = sum(1 for r in results if r["status"] == "critical")
    
    # 3. 判断整体状态
    if critical_count > 0:
        overall_status = "严重异常"
        status_icon = "🔴"
    elif warning_count > 0:
        overall_status = "告警"
        status_icon = "🟡"
    else:
        overall_status = "健康"
        status_icon = "🟢"
    
    # 4. 生成报告内容
    report_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    report_date = datetime.now().strftime("%Y%m%d_%H%M%S")
    
    if format == "markdown":
        content = _generate_markdown_report(
            report_time, overall_status, status_icon,
            total, healthy_count, warning_count, critical_count,
            results, repair_results
        )
        filename = f"health_report_{report_date}.md"
    else:
        content = _generate_txt_report(
            report_time, overall_status,
            total, healthy_count, warning_count, critical_count,
            results, repair_results
        )
        filename = f"health_report_{report_date}.txt"
    
    # 5. 写入文件
    report_path = os.path.join(output_dir, filename)
    with open(report_path, "w", encoding="utf-8") as f:
        f.write(content)
    
    return report_path


def _generate_markdown_report(report_time, overall_status, status_icon,
                              total, healthy, warning, critical,
                              results, repair_results):
    """生成 Markdown 格式的报告（GitHub 上能直接看）"""
    
    lines = []
    
    # 标题
    lines.append(f"# 健康巡检报告")
    lines.append("")
    lines.append(f"**巡检时间**：{report_time}")
    lines.append("")
    lines.append(f"**整体状态**：{status_icon} {overall_status}")
    lines.append("")
    
    # 统计概览
    lines.append("## 📊 巡检概览")
    lines.append("")
    lines.append(f"| 指标 | 数量 |")
    lines.append(f"|------|------|")
    lines.append(f"| 总检查项 | {total} |")
    lines.append(f"| ✅ 正常 | {healthy} |")
    lines.append(f"| ⚠️ 告警 | {warning} |")
    lines.append(f"| ❌ 严重异常 | {critical} |")
    lines.append("")
    
    # 异常项汇总（如果有的话）
    abnormal_results = [r for r in results if r["status"] != "healthy"]
    if abnormal_results:
        lines.append("## ⚠️ 异常项汇总")
        lines.append("")
        lines.append("| 检查项 | 类型 | 状态 | 详情 |")
        lines.append("|--------|------|------|------|")
        for r in abnormal_results:
            status_emoji = "⚠️" if r["status"] == "warning" else "❌"
            lines.append(f"| {r['name']} | {r['type']} | {status_emoji} {r['status']} | {r['message']} |")
        lines.append("")
    
    # 详细检查结果
    lines.append("## 📋 详细检查结果")
    lines.append("")
    lines.append("| 检查项 | 类型 | 状态 | 详情 | 检查时间 |")
    lines.append("|--------|------|------|------|----------|")
    for r in results:
        if r["status"] == "healthy":
            status_emoji = "✅"
        elif r["status"] == "warning":
            status_emoji = "⚠️"
        else:
            status_emoji = "❌"
        lines.append(f"| {r['name']} | {r['type']} | {status_emoji} {r['status']} | {r['message']} | {r['check_time']} |")
    lines.append("")
    
    # 自动修复结果（如果有的话）
    if repair_results:
        lines.append("## 🔧 自动修复结果")
        lines.append("")
        lines.append("| 服务 | 状态 | 详情 | 重试次数 |")
        lines.append("|------|------|------|----------|")
        for r in repair_results:
            if r["status"] == "success":
                status_emoji = "✅"
            elif r["status"] == "failed":
                status_emoji = "❌"
            else:
                status_emoji = "⏭️"
            lines.append(f"| {r['name']} | {status_emoji} {r['status']} | {r['message']} | {r.get('retry_count', 0)} |")
        lines.append("")
    
    # 结尾
    lines.append("---")
    lines.append("*本报告由健康巡检系统自动生成*")
    
    return "\n".join(lines)


def _generate_txt_report(report_time, overall_status,
                         total, healthy, warning, critical,
                         results, repair_results):
    """生成纯文本格式的报告"""
    
    lines = []
    separator = "=" * 60
    
    lines.append(separator)
    lines.append("           健康巡检报告")
    lines.append(separator)
    lines.append(f"巡检时间：{report_time}")
    lines.append(f"整体状态：{overall_status}")
    lines.append("")
    
    # 统计概览
    lines.append(f"总检查项：{total}")
    lines.append(f"正常：{healthy}")
    lines.append(f"告警：{warning}")
    lines.append(f"严重异常：{critical}")
    lines.append("")
    
    # 异常项
    abnormal_results = [r for r in results if r["status"] != "healthy"]
    if abnormal_results:
        lines.append("-" * 40)
        lines.append("异常项汇总：")
        lines.append("-" * 40)
        for r in abnormal_results:
            lines.append(f"  [{r['status']}] {r['name']}: {r['message']}")
        lines.append("")
    
    # 详细结果
    lines.append("-" * 40)
    lines.append("详细检查结果：")
    lines.append("-" * 40)
    for r in results:
        lines.append(f"  [{r['status']}] {r['name']} ({r['type']}): {r['message']}")
    lines.append("")
    
    # 修复结果
    if repair_results:
        lines.append("-" * 40)
        lines.append("自动修复结果：")
        lines.append("-" * 40)
        for r in repair_results:
            lines.append(f"  [{r['status']}] {r['name']}: {r['message']}")
        lines.append("")
    
    lines.append(separator)
    lines.append("本报告由健康巡检系统自动生成")
    
    return "\n".join(lines)


# ==================== 测试代码 ====================

if __name__ == "__main__":
    print("报告生成模块测试")
    
    # 模拟一些检查结果
    test_results = [
        {"name": "超市管理系统", "type": "web", "status": "healthy", "message": "服务正常，响应时间 120ms", "check_time": "2024-01-01 12:00:00"},
        {"name": "Tomcat", "type": "port", "status": "healthy", "message": "端口 8080 正常监听", "check_time": "2024-01-01 12:00:00"},
        {"name": "MySQL", "type": "port", "status": "healthy", "message": "端口 3306 正常监听", "check_time": "2024-01-01 12:00:00"},
        {"name": "CPU使用率", "type": "system", "status": "warning", "message": "CPU 使用率 85%", "check_time": "2024-01-01 12:00:00"},
        {"name": "内存使用率", "type": "system", "status": "healthy", "message": "内存使用率 60%", "check_time": "2024-01-01 12:00:00"},
    ]
    
    test_repair = [
        {"name": "Tomcat", "type": "repair", "status": "success", "message": "自动修复成功", "retry_count": 1, "check_time": "2024-01-01 12:00:00"}
    ]
    
    # 生成 Markdown 报告
    report_path = generate_report(test_results, test_repair, format="markdown")
    print(f"\nMarkdown 报告已生成：{report_path}")
    
    # 生成 TXT 报告
    report_path2 = generate_report(test_results, test_repair, format="txt")
    print(f"TXT 报告已生成：{report_path2}")