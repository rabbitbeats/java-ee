#!/usr/bin/python
# coding=utf-8

# 或者来中文编码
# -*- coding: UTF-8 -*-

__author__ = 'lcw'

import unittest

from functools import reduce
import sys


# https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143217133614028a244ea855b40a586b551c616d3b2c9000
def test_base():
    print(sys.path)
    print("\"你好\" ，世界")

    print(100 + 200)
    print('hello world')


def log(func):
    def wrapper(*args, **kw):
        print("call %s():" % func.__name__)
        return func(*args, **kw)

    return wrapper


@log
def now():
    print("2017-08-08")


now()
test_base()
