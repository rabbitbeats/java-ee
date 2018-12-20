#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import pymysql
import mysql.connector

db = pymysql.connect("python_test", "localhost", "root", "root")
cursor = db.cursor()
cursor.execute("select version()")
data = cursor.fetchone()
print("Database version: %s" % data)
db.close()


# 注意把password设为你的root口令:
conn = mysql.connector.connect(user='root', password='root', database='python_test')

# cursor = conn.cursor()
# # 创建user表:
# cursor.execute('create table user (id varchar(20) primary key, name varchar(20))')
# # 插入一行记录，注意MySQL的占位符是%s:
# cursor.execute('insert into user (id, name) values (%s, %s)', ['1', 'Michael'])
# conn.commit()
# cursor.close()


# 运行查询:
cursor = conn.cursor()
cursor.execute('select * from user where id = %s', ('1',))
values = cursor.fetchall()
print(values)
# 关闭Cursor和Connection:
cursor.close()
conn.close()
