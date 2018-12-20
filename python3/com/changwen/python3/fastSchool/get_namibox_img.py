import requests
import json
import re

g_cdn = 'https://r.namibox.com'
g_cdn_audio = 'https://ra.namibox.com'
g_userwork_cdn = 'https://u.namibox.com'
g_cdn_upimg = 'https://f.namibox.com'  # 图片域名
g_site = 'https://www.namibox.com'  # 纳米盒的域名
g_client_ip = '202.106.10.34'


# 获取纳米盒官网首页所有的跳转页面
def get_href_list_home():
    r = requests.get(url=g_site)  # GET请求
    # print(r.text)
    text = r.text
    # <a href="/v/referencebook">  https://c.namibox.com 不匹配含有http,https的
    regex = re.compile('<a href=[\'\"](?!\w+:)([^\'\" ]+).+>')
    # print(regex.findall(text)[0])
    page_img_list = set(regex.findall(text))  # 去重
    for page_img in page_img_list:
        new_page_img = page_img
        print("1-->" + new_page_img)
        # get_rest_url(new_page_img)


# 获取页面里的url列表
def get_rest_url(rest_url):
    view_url = g_site + rest_url
    r = requests.get(url=view_url)  # 带参数的GET请求
    # print(r.text)
    text = r.text
    # <a href="/v/sclib/english/d/jct3content/004012" target="jct3content"  >
    regex = re.compile(rest_url + '/[-A-Za-z0-9&/]+\d+')  # 不匹配分类的
    # print(regex.findall(text))
    page_img_list = set(regex.findall(text))  # 去重
    for page_img in page_img_list:
        new_page_img = page_img
        # print(new_page_img)
        get_view_img(new_page_img)


# 获取单个页面的图片
def get_view_img(restful_url):
    view_url = g_site + restful_url
    r = requests.get(url=view_url)  # 带参数的GET请求
    # print(r.text)
    text = r.text
    regex = re.compile(g_cdn_upimg + '/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]')  # 匹配url
    # print(regex.findall(text))
    page_img_list = set(regex.findall(text))  # 去重
    for page_img in page_img_list:
        new_page_img = page_img
        print(new_page_img)


get_href_list_home()
# https://www.namibox.com/v/sclib/english
# get_rest_url('/v/sclib/english')
