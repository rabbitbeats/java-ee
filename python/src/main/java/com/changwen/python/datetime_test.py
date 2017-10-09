#!/usr/bin/python
# coding=utf-8
import datetime, os


def test1():
    start_time = datetime.datetime.now()
    sched_timer = start_time.strftime("%Y-%m-%d %H:%M:%S")
    flag = 0
    while True:
        now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        if now == sched_timer:
            print(1)
            #            print(sched_timer)
            flag = 1
        else:
            if flag == 1:
                sched_timer = (start_time + datetime.timedelta(seconds=5)).strftime("%Y-%m-%d %H:%M:%S")
                print(2)
                print(sched_timer)
                flag = 0


# test1()


def test2():
    # dt = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    # print(dt)
    sched_timer = datetime.datetime.now().strftime("%Y%m%d%H%M%S")
    print(sched_timer)
    print(datetime.datetime.now())
    print(datetime.timedelta(minutes=5))


#    sched_timer = sched_timer + datetime.timedelta(seconds=5)


test2()

def run_task(abs_file_path, sched_time):
    is_file_exist = os.path.exists(abs_file_path)  # 判断文件是否存在

    if is_file_exist == 1:
        dir_path = abs_file_path[0:abs_file_path.rfind('/', 1) + 1]

        #    file_name = abs_file_path.split("/")[-1]
        file_name = abs_file_path[abs_file_path.rindex("/") + 1:]  # 这种效率更高

        sched_time = sched_time.strftime("%Y%m%d%H%M%S")
        new_file_name = dir_path + sched_time + '_' + file_name
        os.system('mv ' + abs_file_path + ' ' + new_file_name)


# sched_timer = datetime.datetime(2017, 4, 5, 12, 1, 10)  # 这个时间 需要是未来时
# run_task('/Users/changwen/Documents/TCP3.png', sched_timer)


def test3(abs_file_path, sched_timer):
    print(sched_timer)
    flag = 0
    while True:
        now = datetime.datetime.now()
        if now == sched_timer:
            print(1)
            run_task(abs_file_path, sched_timer)
            flag = 1
        else:
            if flag == 1:
                sched_timer = sched_timer + datetime.timedelta(seconds=10)
                print(2)
                print(sched_timer)
                flag = 0


# sched_timer = datetime.datetime(2017, 4, 5, 12, 16, 10)  # 这个时间 需要是未来时
# test3('/Users/changwen/Documents/tcp3.png', sched_timer)
