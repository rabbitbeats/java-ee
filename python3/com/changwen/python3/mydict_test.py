#!/usr/bin/python
# coding=utf-8

import unittest

from mydict import myclass


class mytest(unittest.TestCase):
    ##初始化工作
    def setUp(self):
        self.tclass = myclass()
        ##实例化了被测试模块中的类

    # 退出清理工作
    def tearDown(self):
        pass

    # 具体的测试用例，一定要以test开头
    def testsum(self):
        self.assertEqual(self.tclass.sum(1, 2), 3)


if __name__ == '__main__':
    unittest.main()
