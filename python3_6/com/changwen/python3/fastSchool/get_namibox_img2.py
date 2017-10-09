#!/usr/bin/env python3
# -*- coding: UTF-8 -*-


import requests
import re

g_cdn = 'https://r.namibox.com'
g_cdn_audio = 'https://ra.namibox.com'
g_userwork_cdn = 'https://u.namibox.com'
g_cdn_upimg = 'https://f.namibox.com'  # 图片域名

g_site = 'https://www.namibox.com'  # 纳米盒的域名

RESULT_IMG_URL = []  #


# 获取 g_site 网页下所有的 g_site开头的的url,如 https://www.namibox.com/v/menu/1a
def get_rest_url(rest_url):
    global RESULT_IMG_URL
    view_url = g_site + rest_url
    r = requests.get(url=view_url)  # 带参数的GET请求

    # print(r.text)
    text = r.text
    # <a href="/v/sclib/english/d/jct3content/004012" target="jct3content"  >
    regex = re.compile('<a href=[\'\"](?!\w+:)([^\'\" ]+).+>')
    # print(regex.findall(text))
    page_img_list = set(regex.findall(text))  # 去重
    # print(len(page_img_list))
    if (len(page_img_list) > 0):
        for page_img in page_img_list:
            # print("2-->" + page_img)
            if (page_img in RESULT_IMG_URL):
                continue
            else:
                RESULT_IMG_URL.append(page_img)

            print("4-->" + page_img)
            get_view_img(page_img)
            with open("douban.txt","a") as f:
                f.write(g_site + page_img)
                f.write('\n')
            new_page_img = page_img
            # get_rest_url(new_page_img)


def download():
    url = 'https://ra.namibox.com/tina/static/v/menu/1a/title.png'
    ir = requests.get(url, stream=True)
    with open("douban.txt","w") as f:
        f.write(url)

    if ir.status_code == 200:
        # '/Users/changwen/IdeaProjects/myProject/java-ee/python3_6/com/changwen/python3/fastSchool'
        # 'hello.png' 会下载到文件目录下，与该文件同级
        with open('hello.png', 'wb') as f:
            for chunk in ir:
                f.write(chunk)


def get_view_img(restful_url):
    view_url = g_site + restful_url
    r = requests.get(url=view_url)  # 带参数的GET请求
    # print(r.text)
    text = r.text
    regex = re.compile(g_cdn_audio + '/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]')  # 匹配url
    # print(regex.findall(text))
    page_img_list = set(regex.findall(text))  # 去重
    for page_img in page_img_list:
        new_page_img = page_img
        print(new_page_img)


get_rest_url('/')
# download()