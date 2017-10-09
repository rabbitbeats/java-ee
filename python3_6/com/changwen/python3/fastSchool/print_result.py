#!/usr/bin/python
# coding=utf-8


import time, datetime
import os


def print_result():
    today = datetime.date.today()  # 获得今天的日期
    yesterday = (today - datetime.timedelta(days=1)).strftime('%Y%m%d')  # 用今天日期减掉时间差，参数为1天，获得昨天的日期
    print(yesterday)

    root_command = "less logs/history/api." + yesterday + ".log.gz"

    #  用户已经登录后从小程序入口进入的【人数】命令
    get_applet_cmd = root_command + "|grep homeworkInfo_3_0 |grep \"type=1\" " \
                                    "|grep \"\\\"statusCode\\\":200\" " \
                                    "|  awk -F \"|\" \'{ print $4, $7 }\' |sort |uniq -c |wc -l"
    print(get_applet_cmd)

    #  用户已经登录后从群里查看作业的【人数】】命令
    get_wx_cmd = root_command + "|grep homeworkInfo_3_0 |grep \"type=2\" " \
                                "|grep \"\\\"statusCode\\\":200\" " \
                                "|awk -F \"|\" \'{ print $4, $7 }\' |sort |uniq -c |wc -l"

    # 交作业的人数
    get_saveresult_cmd = root_command + "| grep saveResult " \
                                        "| grep \"\\\"statusCode\\\":200\"" \
                                        "| awk -F\"|\" \'{ print $4, $7 }\' |sort |uniq -c |wc -l"

    # 登陆成功人数
    get_success_login_cmd = root_command + "| grep userLogin | grep \"\\\"statusCode\\\":200\" " \
                                           "| awk -F \"|\" \'{ print $1 $4, $7 }\' |sort |uniq -c |wc -l"

    #
    cmd = "ls -a | wc -l"
    print(os.popen(cmd).read())

    print((today - datetime.timedelta(days=1)).strftime('%Y-%m-%d') + ", 不算重复的")
    print("用户已经登录后从小程序入口进入的【人数】有 " + os.popen(get_applet_cmd).read())
    print("用户已经登录后从群里查看作业的【人数】有 " + os.popen(get_wx_cmd).read())
    print("用户交过作业的【人数】有 " + os.popen(get_saveresult_cmd).read())
    print("登陆成功人数 " + os.popen(get_success_login_cmd).read())
    print("如果用户退出再登录，算2人")


print_result()
