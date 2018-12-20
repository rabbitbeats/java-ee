#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import os, shutil, glob, sys, re, math, random
from datetime import date, datetime

# 建议使用 "import os" 风格而非 "from os import *"。这样可以保证随操作系统不同而有所变化的 os.open() 不会覆盖内置函数 open()。

# ------------------------------------- 标准库概览 ---------------------------------

'''
1、操作系统接口
'''
# 返回当前的工作目录
print(os.getcwd())  # /Users/changwen/IdeaProjects/myProject/java-ee/python3/base
# print(os.system('mkdir today'))   # 执行系统命令 mkdir

'''针对日常的文件和目录管理任务，:mod:shutil 模块提供了一个易于使用的高级接口:'''
# shutil.copyfile('data.db', 'archive.db')  # 复制文件
# shutil.move('/build/executables', 'installdir')   # 删除文件


'''
2、文件通配符
    glob模块提供了一个函数用于从目录通配符搜索中生成文件列表:
'''
glob.glob('*.py')
print(sys.argv)

'''
3、字符串正则匹配
    re模块为高级字符串处理提供了正则表达式工具
'''

'''
4、数学
    math模块为浮点运算提供了对底层C函数库的访问
    random提供了生成随机数的工具。
'''
print(math.cos(math.pi / 4))
print(random.choice(['apple', 'pear', 'banana']))

'''
5、访问 互联网
    有几个模块用于访问互联网以及处理网络通信协议。其中最简单的两个是用于处理从 urls 接收的数据的 urllib.request和发送电子邮件的 smtplib
    邮件发送，本地有一个在运行的邮件服务器
'''
# from urllib.request import urlopen

'''
6、日期和时间
    datetime模块为日期和时间处理同时提供了简单和复杂的方法。
    支持日期和时间算法的同时，实现的重点放在更有效的处理和格式化输出。
    该模块还支持时区处理:
'''
nowDate = date.today()
print(nowDate)  # 2018-03-11
print(datetime.today())  # 2018-03-11 18:48:16.528266
my_birth = date(1993, 8, 3)
print(nowDate - my_birth)  # 8986 days, 0:00:00
