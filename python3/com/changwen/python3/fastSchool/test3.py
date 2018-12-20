import requests
import json
import re


# r = requests.get(
#     url='https://www.namibox.com/v/sclib/english/read')  # 带参数的GET请求
# print(r.text)
# # base_url = "https://ra.namibox.com/tina/static/d/tape1a/002002_%E8%8B%B1%E8%AF%AD%EF%BC%88%E6%96%B0%E8%B5%B7%E7%82%B9%EF%BC%89/bookshow/"
# base_url = 'https://ra.namibox.com'   # 纳米盒的域名
# text = r.text
# regex = re.compile('https://f.namibox.com/content/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]')  # 匹配url
# print(regex.findall(text))
# page_img_list = regex.findall(text)
# for page_img in page_img_list:
#     new_page_img = page_img
#     print(new_page_img)

# <a href="/v/sclib/english/read/d/jct3content/001017" target="jct3content"  >


# 获取页面里的url列表
def get_rest_url(rest_url):
    g_site = 'https://www.namibox.com'  # 纳米盒的域名
    g_cdn_upimg = 'https://f.namibox.com'  # 图片域名

    view_url = g_site + rest_url
    r = requests.get(url=view_url)  # 带参数的GET请求
    # print(r.text)
    text = r.text
    # <a href="/v/sclib/english/d/jct3content/004012" target="jct3content"  >
    regex = re.compile(rest_url + '/[-A-Za-z0-9&/]*')  # 不匹配分类的
    print(regex.findall(text))
    page_img_list = set(regex.findall(text))  # 去重
    for page_img in page_img_list:
        new_page_img = page_img
        print(new_page_img)


# https://www.namibox.com/v/sclib/english
get_rest_url('/v/sclib/123')
