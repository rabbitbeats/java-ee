#!/usr/bin/python
# -*- coding: UTF-8 -*-
import re
import requests


# 解析器
class HtmlParser(object):
    def parse(self, g_site, img_sites, restful_url, filters, restful_url_filters):
        if g_site is None or img_sites is None or restful_url is None:
            return

        new_restful_urls = self._get_view_url(g_site, img_sites, restful_url, restful_url_filters)
        new_img_urls = self._get_img_url(g_site, img_sites, restful_url, filters)
        return new_restful_urls, new_img_urls

    def _get_view_url(self, g_site, img_sites, restful_url, restful_url_filters):
        view_url = g_site + restful_url
        r = requests.get(url=view_url)  # 带参数的GET请求

        # print(r.text)
        text = r.text
        regex = re.compile('<a href=[\'\"](?!\w+:)([^\'\" ]+).+>')
        restful_url_list = set(regex.findall(text))  # 去重
        if len(restful_url_list) > 0:
            for page_img in restful_url_list:
                # 关键字过虑
                count = 0
                for filter_key in restful_url_filters:
                    if page_img.find(filter_key) != -1:  # 如果含有关键字
                        count += 1
                        break
                if count == 0:
                    print("_get_view_url-->", page_img)
                    restful_url_list.add(page_img)
        # print(restful_url_list)
        return restful_url_list

    def _get_img_url(self, g_site, img_sites, restful_url, filters):
        new_img_urls = set()

        view_url = g_site + restful_url
        ir = requests.get(url=view_url)  # 带参数的页面GET请求
        if ir.status_code == 200:
            text = ir.text

            for img_site in img_sites:
                regex = re.compile(img_site + '/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]')  # 匹配url
                # print(regex.findall(text))
                page_img_list = set(regex.findall(text))  # 去重
                if len(page_img_list) > 0:
                    for page_img in page_img_list:
                        if page_img.find(".jpg") == -1:
                            continue
                        else:
                            print("parse--------------------->", page_img)

                        # 关键字过虑
                        count = 0
                        for filter_key in filters:
                            if page_img.find(filter_key) != -1:  # 如果含有关键字
                                count += 1
                                break
                        if count == 0:
                            new_img_urls.add(page_img)

            # print(new_img_urls)
            return new_img_urls
        else:
            return None
