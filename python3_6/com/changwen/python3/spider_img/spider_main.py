#!/usr/bin/python
# -*- coding: UTF-8 -*-
import my_img_downloader
import my_html_parser
import my_html_outputer
import my_url_manager


# 获取 g_site 网页下所有的 g_site开头的的url,如 https://www.namibox.com/v/menu/1a
class SpiderImgMain(object):
    # 初始各个对象， 其中UrlManager、HtmlDownloader、HtmlParser、HtmlOutputer四个对象需要之后创建
    def __init__(self):
        self.urls = my_url_manager.UrlManager()  # URL管理器
        self.downloader = my_img_downloader.ImgDownloader()  # 下载器
        self.parser = my_html_parser.HtmlParser()  # 解析器
        self.outputer = my_html_outputer.ImgOutputer()  # 输出器

    def craw(self, g_site, img_sites, restful_url, filters, restful_url_filters):
        count = 1
        self.urls.add_new_url(restful_url)

        # 只要添加的url里有新的url
        while self.urls.has_new_url():
            try:
                new_url = self.urls.get_new_url()
                # print('craw %d : %s' % (count, new_url))
                print('craw  %s' % new_url)
                if new_url.find("/") == -1:
                    continue
                print('执行 craw %d : %s' % (count, new_url))

                # 调用解析器解析下载的这个页面
                new_restful_urls, new_img_urls = self.parser.parse(g_site, img_sites, new_url, filters,
                                                                   restful_url_filters)

                # 将解析出的url添加到url管理器， 将数据添加到输出器里
                self.urls.add_new_urls(new_restful_urls)

                # 启动下载器，将获取到的url下载下来
                if len(new_img_urls) > 0:
                    for new_img_url in new_img_urls:
                        self.downloader.download(new_img_url, 'download/')
                        # self.downloader.download(new_img_url)

                        # print(new_img_url)
                        count += 1
                        # 收集下载图片url
                        self.outputer.collect_data(new_img_url)

                if count > 30:
                    print("count----->")
                    self.outputer.output_html()
                    break

                    # count += 1

            except:
                print('craw failed')

            self.outputer.output_html()


if __name__ == "__main__":
    # 图片域名
    g_cdn = 'https://r.namibox.com'
    g_cdn_audio = 'https://ra.namibox.com'
    g_userwork_cdn = 'https://u.namibox.com'
    g_cdn_upimg = 'https://f.namibox.com'

    g_site = 'https://www.namibox.com'  # 纳米盒的域名
    restful_url = '/audio/bookclick?book=d/tape1a/002002_%E8%8B%B1%E8%AF%AD%EF%BC%88%E6%96%B0%E8%B5%B7%E7%82%B9%EF%BC%89&audio=000002_Starter_Starter.mp3'  # my_img_downloader
    img_sites = [g_cdn_audio]
    filters = ['gif', 'icon', 'title']
    restful_url_filters = ['zidian', 'chengyu']
    obj_spider = SpiderImgMain()
    obj_spider.craw(g_site, img_sites, restful_url, filters, restful_url_filters)  # 启动爬虫
