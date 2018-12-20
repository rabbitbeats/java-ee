import requests
import json
import re


# 获取单个页面的图片
def get_view_img(restful_url):
    g_site = 'https://www.namibox.com'  # 纳米盒的域名
    g_cdn_upimg = 'https://f.namibox.com'  # 图片域名

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


get_view_img('/v/sclib/english/d/jct3content/003992/')
