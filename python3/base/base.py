#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
#!/usr/bin/python3 
    这句话仅仅在linux或unix系统下有作用，在 Windows 下可以不写第一行注释:因为在windows下文件名对文件的打开方式起了决定性作用
    
推荐使用 #!/usr/bin/env python3
    这种用法先在 env（环境变量）设置里查找 python 的安装路径，再调用对应路径下的解释器程序完成操作。
"""

import keyword

# 单行注释用#， 多行注释可以用多个 # 号，还有 ''' 和 """：
"""
行与缩进
    python最具特色的就是使用缩进来表示代码块，不需要使用大括号 {} 。
"""

"""
1、标识符
    第一个字符必须是字母表中字母或下划线 _ 。
    标识符的其他的部分由字母、数字和下划线组成。
    标识符对大小写敏感。
"""

"""
2、python保留字
    保留字即关键字，我们不能把它们用作任何标识符名称。Python 的标准库提供了一个 keyword 模块，可以输出当前版本的所有关键字：
"""
# print 默认输出是换行的，如果要实现不换行需要在变量末尾加上 end=""：
print(keyword.kwlist)
print("a--->", end="")

"""
4、字符串(String)
    python中单引号和双引号使用完全相同。
    使用三引号可以指定一个多行字符串。
    转义符 '\'
    反斜杠可以用来转义，使用r可以让反斜杠不发生转义。。 如 r"this is a line with \n" 则\n会显示，并不是换行。
    按字面意义级联字符串，如"this " "is " "string"会被自动转换为this is string。
    字符串可以用 + 运算符连接在一起，用 * 运算符重复。
    Python 中的字符串有两种索引方式，从左往右以 0 开始，从右往左以 -1 开始。
    Python中的字符串不能改变。
    Python 没有单独的字符类型，一个字符就是长度为 1 的字符串。
    字符串的截取的语法格式如下：变量[头下标:尾下标]
"""

# 等待用户输入
input("enter something: ")

# 同一行显示多条语句: Python可以在同一行中使用多条语句，语句之间使用分号(;)分割
import sys;

x = 'runoob';
sys.stdout.write(x + '\n')

"""
5、import 与 from...import
    在 python 用 import 或者 from...import 来导入相应的模块。
    将整个模块(somemodule)导入，格式为： import somemodule
    从某个模块中导入某个函数,格式为： from somemodule import somefunction
    从某个模块中导入多个函数,格式为： from somemodule import firstfunc, secondfunc, thirdfunc
    将某个模块中的全部函数导入，格式为： from somemodule import *
"""
# --------------------------------------------------------------

"""
6、基本数据类型
    Python 中的变量不需要声明。每个变量在使用前都必须赋值，变量赋值以后该变量才会被创建。
    在 Python 中，变量就是变量，它没有类型，我们所说的"类型"是变量所指的内存中对象的类型。
    等号（=）用来给变量赋值。
    python 是动态的，变量的类型由赋予它的值来决定
    
Python3 中有六个标准的数据类型：
    Number（数字）
    String（字符串）
    List（列表）
    Tuple（元组）
    Sets（集合）
    Dictionary（字典）
    
    string、list和tuple都属于 sequence（序列）
    不可变类型：整数、字符串、元组
"""
myInt = 1
myFloat = 1.0
myStr = "a"
print(myInt)  # 1
print(myFloat)  # 1.0
print(myStr)  # a

# 多个变量赋值
a = b = c = 1
d, e, f = 1, 2.0, 'a'

'''
6.1、数字(Number)类型
    python中数字有四种类型：整数、长整数、浮点数和复数。
    1).int (整数), 如 1, 只有一种整数类型 int，表示为长整型，没有 python2 中的 Long。
    2).float (浮点数), 如 1.23、3E-2
    3).bool (布尔),如 true。
    4).complex (复数), 如 1 + 2j、 1.1 + 2.2j
    数据类型是不允许改变的,这就意味着如果改变数字数据类型的值，将重新分配内存空间。
    
    内置的 type()/isinstance 函数可以用来查询变量所指的对象类型
'''
print(type(1), type(1.0))  # <class 'int'> <class 'float'>
print(isinstance(1, int))  # True

# del语句删除一些数字对象的引用
# del var
# del var_a, var_b

class A:
    pass


class B(A):
    pass


# 区别就是:
# type()不会认为子类是一种父类类型。
# isinstance()会认为子类是一种父类类型。
print(isinstance(A(), A), type(A()) == A)  # True   True
print(isinstance(B(), A), type(B()) == A)  # True    False
# 使用 isinstance() 代替对象类型的比较


# 注意：在 Python2 中是没有布尔型的，它用数字 0 表示 False，用 1 表示 True。
# 到 Python3 中，把 True 和 False 定义成关键字了，但它们的值还是 1 和 0，它们可以和数字相加。

# 计算要注意到的地方
# 在混合计算时，Python会把整型转换成为浮点数。
print(4 / 2)  # 2.0  除法（/）总是返回一个浮点数，要获取整数使用//操作符
print(4 // 2)  # 2
print(2 % 4)  # 2     取余得到的是整数
print(2 ** 4)  # 16    乘方

'''
6.2、String（字符串）
    字符串的截取的语法格式：变量[头下标:尾下标]  [ )
    Python中的字符串有两种索引方式，从左往右以0开始，从右往左以-1开始
'''
str = 'Runoob'
print(str[2:])  # noob 输出从第三个开始的后的所有字符
print(str * 2)  # 输出字符串两次

# 如果你不想让反斜杠发生转义，可以在字符串前面添加一个 r，表示原始字符串
print(r'Ru\noob')  # Ru\noob

# 字符串输出
print('这个人的名字是%s,已经有%d岁了！' % ('liucw', 23))

'''
6.3、List（列表）
    与Python字符串不一样的是，列表中的元素是可以改变的：
'''
list = ['abcd', 786, 2.23, 'runoob', 70.2]
print(list[1:3])  # [786, 2.23]  从第二个开始输出到第三个元素  [ )
# print(list * 2)  # 输出两次列表

a = [1, 2, 3, 4, 5, 6]
a[2:5] = []  # 将对应的元素值设置为 []
print(a)  # [1, 2, 6]

'''
6.4、Tuple（元组）
    元组（tuple）与列表类似，
    不同之处在于: 元组的元素不能修改（但它可以 包含 可变的对象，比如list列表）。元组写在小括号()里(列表写在[]里)
    可以把字符串看作一种特殊的元组。
'''
tup = (1, 2, 3, 4, 5, 6)
print(tup[1:5])  # (2,3,4,5)

# 额外的语法规则
tup1 = ()  # 空元组
tup2 = (20,)  # 一个元素，需要在元素后添加逗号


'''
6.5、Set（集合）
    集合（set）是一个 无序 不重复 元素的序列。
    基本功能是进行成员关系测试和删除重复元素。
    可以使用大括号 { } 或者 set() 函数创建集合，注意：创建一个空集合必须用 set() 而不是 { }，因为 { } 是用来创建一个空字典。
'''
student = {'Tom', 'Jim', 'Mary', 'Tom', 'Jack', 'Rose'}
# 输出集合，重复的元素被自动去掉
print(student)  # {'Jim', 'Mary', 'Rose', 'Tom', 'Jack'}

# set可以进行集合运算
a = set('abracadabra')
b = set('alacazam')
print(a - b)  # a和b的差集 {'d', 'b', 'r'}
print(a | b)  # a和b的并集 {'r', 'b', 'z', 'c', 'l', 'm', 'd', 'a'}
print(a & b)  # a和b的交集 {'c', 'a'}
print(a ^ b)  # a和b中不同时存在的元素 {'r', 'b', 'z', 'l', 'm', 'd'}

'''
6.6、Dictionary（字典）
    列表是有序的对象结合，字典是无序的对象集合。
    两者区别：字典当中的元素是通过键来存取的，而不是通过偏移存取。 类似Map
    字典是一种映射类型，字典用"{ }"标识，它是一个无序的键(key) : 值(value)对集合。
    键(key)必须使用不可变类型。
    在同一个字典中，键(key)必须是唯一的。
'''
tinydict = {}  # 创建空字典使用 {}
tinydict['one'] = "1-test"
tinydict[2] = 20
print(tinydict['one'], tinydict[2])  # 1-test 20
print(tinydict.keys())  # tinydict(['one', 2])
print(tinydict.values())  # tinydict(['1-test', 20])

# dict(d) 创建一个字典。d 必须是一个序列 (key,value)元组。  set(s) list(s) tuple(s) 类似
myDict = dict([('Runoob', 1), ('Google', 2), ('Taobao', 3)])
print(myDict)  # {'Runoob': 1, 'Google': 2, 'Taobao': 3}
