#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import sys
from timeit import Timer

# 单纯说递归还好，其实递归在很大程度上牺牲了空间换取了可读性。每次调用递归函数的时候都会创建一个函数栈，如果递归深度过大，则会造成溢出状况
def fib1(n):
    if (n < 0): return -1
    if (n == 0): return 0
    if (n == 1): return 1

    return fib1(n - 1) + fib1(n - 2)

Timer(fib1(10)).timeit()
# print(Fibonacci(40))


dic = {0: 0, 1: 1}


def fib(n):
    if n in dic:
        return dic[n]
    else:
        temp = fib(n - 1) + fib(n - 2)
        dic[n] = temp
        return temp


print(fib(40))

'''
while 循环使用 else 语句
'''
count = 0
while count < 5:
    print(count, " 小于 5")
    count = count + 1
else:
    print(count, " 大于或等于 5")

'''
for 语句
    for <variable> in <sequence>:
        <statements>
    else:
        <statements>
'''
data = [3, 6, 7]
for x in data:
    print(x)
else:
    print("end")

for x in range(3):
    print('range', x)


    # for i in range(5,9) : 使用range指定区间的值
    # for i in range(0, 10, 3) : 使range以指定数字开始并指定不同的增量(甚至可以是负数，有时这也叫做'步长'
# list(range(5))

# 使用内置 enumerate 函数进行遍历:
for i, j in enumerate(data):
    print("enumerate-->", i, j)


def maopap(list):
    for i in range(len(list) - 1):
        for j in range(len(list) - i - 1):
            if list[j + 1] < list[j]:
                temp = list[j + 1]
                list[j + 1] = list[j]
                list[j] = temp
    print(list)


maopap([2, 5, 1, 5, 7, 4, 8, 9, 1])

# ----------------------------------- 迭代器与生成器 -----------------------------------
'''
迭代器
    迭代是Python最强大的功能之一，是访问集合元素的一种方式。
    迭代器是一个可以记住遍历的位置的对象。
    迭代器对象从集合的第一个元素开始访问，直到所有的元素被访问完结束。迭代器只能往前不会后退。
    迭代器有两个基本的方法：iter() 和 next()。
    字符串，列表或元组对象都可用于创建迭代器：
'''
list = [1, 2, 3, 4]
it = iter(list)  # 创建迭代器对象
print(next(it))  # 输出迭代器的下一个元素

# 迭代器对象可以使用常规for语句进行遍历：这里的It是上面的迭代器
for x in it:
    print(x, end='')
print()

# 也可以使用 next() 函数, 遍历
print("next()----")
it = iter(list)
# 下面的代码如果打开了，这个代码块后面的代码就没法执行
# while True:
#     try:
#         print(next(it), end='')
#     except StopIteration:
#         sys.exit()

'''
生成器
    在 Python 中，使用了 yield 的函数被称为生成器（generator）。
    跟普通函数不同的是，生成器是一个返回迭代器的函数，只能用于迭代操作，更简单点理解生成器就是一个迭代器。
    在调用生成器运行的过程中，每次遇到 yield 时函数会暂停并保存当前所有的运行信息，返回 yield 的值, 并在下一次执行 next() 方法时从当前位置继续运行。
    调用一个生成器函数，返回的是一个迭代器对象。
    以下实例使用 yield 实现斐波那契数列：
'''

# ------------------------------- 函数 ---------------------
'''
'''


# 可写函数说明
def printinfo(name, age):
    """ 打印任何传入的字符串 """
    print("名字: ", name)
    print("年龄: ", age)
    return


# 调用printinfo函数， 不需要使用指定顺序
printinfo(age=50, name="runoob")

# 默认参数， 调用函数时，如果没有传递参数，则会使用默认参数， 【默认参数必须放在最后面】
# def printinfo( name, age = 35 ):

# 不定长参数：注意只有一个 *
# def functionname([formal_args,] *var_args_tuple ):
# def(**kwargs) 把N个关键字参数转化为字典:
def func(country,province,**kwargs):
    print(country,province,kwargs)

func("China","Sichuan", city = "Chengdu",  section = "JingJiang")
# China Sichuan {'city': 'Chengdu', 'section': 'JingJiang'}

'''
匿名函数
    python 使用 lambda 来创建匿名函数。
    所谓匿名，意即不再使用 def 语句这样标准的形式定义一个函数。
    lambda 只是一个表达式，函数体比 def 简单很多。
    lambda的主体是一个表达式，而不是一个代码块。仅仅能在lambda表达式中封装有限的逻辑进去。
    lambda 函数拥有自己的命名空间，且不能访问自己参数列表之外或全局命名空间里的参数。
    虽然lambda函数看起来只能写一行，却不等同于C或C++的内联函数，后者的目的是调用小函数时不占用栈内存从而增加运行效率。

语法
    lambda 函数的语法只包含一个语句，如下：
    lambda [arg1 [,arg2,.....argn]]:expression
'''
mySum = lambda arg1, arg2: arg1 + arg2
print("相加后的值为 : ", mySum(10, 20))

# lambda 匿名函数也是可以使用"关键字参数"进行参数传递
print("lambda参数传递-->", mySum(arg1=1, arg2=2))  # 3

# lambda 匿名函数也可以设定默认值
mySum = lambda arg1=1, arg2=2 : arg1 + arg2

'''
'''
# 函数返回 值
def sumCal(arg1, arg2):
    return arg1 + arg2


print(sumCal(1, 3))

# 全局变量 与 局部变更
total = 0  # 这是一个全局变量
def sum(arg1, arg2):
    total = arg1 + arg2 # total在这里是局部变量.
    print("函数内是局部变量 : ", total)
    return total


# 调用sum函数
sum(10, 20)
print("函数外是全局变量 : ", total)
# 函数内是局部变量 :  30
# 函数外是全局变量 :  0

'''
global 和 nonlocal关键字
    当内部作用域想修改外部作用域的变量时，就要用到global和nonlocal关键字了。
    如果要修改嵌套作用域（enclosing 作用域，外层非全局作用域）中的变量则需要 nonlocal 关键字了
'''
# global测试
num = 1
def test_local() :
    global num  # 需要使用 global 关键字声明
    print(num)
    num = 123
    print(num)

test_local()
# 1
# 123

# nonlocal测试
def outer():
    num = 10
    def inner():
        nonlocal num   # nonlocal关键字声明
        print(num)
        num = 100
        print(num)
    inner()
    print(num)
outer()
# 10
# 100
# 100

# 特殊情况！！！, 下面语句执行后会有错误，错误信息为局部作用域引用错误，因为 test 函数中的 a 使用的是局部，未定义，无法修改。
a = 10
def test():
    a = a + 1
    print(a)
test()