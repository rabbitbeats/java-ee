#!/usr/bin/env python3
# coding:UTF-8


import unittest
import os


class test_thread(unittest.TestCase):
    # Unix/Linux操作系统提供了一个fork()系统调用，它非常特殊。普通的函数调用，调用一次，返回一次，但是fork()调用一次，返回两次，
    # 因为操作系统自动把当前进程（称为父进程）复制了一份（称为子进程），然后，分别在父进程和子进程内返回。
    # 【子进程永远返回0】，而父进程返回子进程的ID。这样做的理由是，一个父进程可以fork出很多子进程，
    # 所以，父进程要记下每个子进程的ID，而子进程只需要调用getppid()就可以拿到父进程的ID。
    def test_fork(self):
        print('Process (%s) start...' % os.getpid())  # 父进程
        pid = os.fork()  # 子进程
        print('pid (%s) ' % pid)

        if pid == 0:
            print('I am child process (%s) and my parent is %s' % (os.getpid(), os.getppid()))
        else:
            print('I (%s) just created a child process (%s).' % (os.getpid(), pid))

            # Process (10145) start...
            # pid (10146)
            # I (10145) just created a child process (10146).
            # pid (0)
            # I am child process (10146) and my parent is 10145


if __name__ == 'main':
    unittest.main()
