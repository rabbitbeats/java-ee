#!/usr/bin/python3
# -*- coding: UTF-8 -*-
import requests
import datetime
import os


# 图片下载器
class ImgDownloader(object):
    # download_dir 图片下载存储目录
    def download(self, url, download_dir):
        if url is None:
            return None

        if download_dir is None:
            return ''

        if not os.path.exists(download_dir):
            os.makedirs(download_dir)

        # 把下载地址追加到txt里, a追加
        ir = requests.get(url, stream=True)
        if ir.status_code == 200:
            # 只写文件名的话，文件会下载到文件目录下，与该文件同级
            download_file_name = datetime.datetime.now().strftime("%Y%m%d%H%M%S")
            with open(download_dir + download_file_name + '.png', 'wb') as f:
                for chunk in ir:
                    f.write(chunk)

            return url
        else:
            return None
