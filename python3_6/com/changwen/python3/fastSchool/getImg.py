import requests
import json
import re

r = requests.get(
    url='https://www.namibox.com/audio/bookclick?book=d/tape1a/002002_%E8%8B%B1%E8%AF%AD%EF%BC%88%E6%96%B0%E8%B5%B7%E7%82%B9%EF%BC%89&audio=000002_Starter_Starter.mp3')  # 带参数的GET请求
# print(r)
# r.text.find_all('a', imglist=re.compile("/view/\d+\.htm"))
# var imglist         = ["page_002.jpg","page_003.jpg","page_004.jpg","page_005.jpg","page_999.jpg"];
base_url = "https://ra.namibox.com/tina/static/d/tape1a/002002_%E8%8B%B1%E8%AF%AD%EF%BC%88%E6%96%B0%E8%B5%B7%E7%82%B9%EF%BC%89/bookshow/"
text = r.text  # "</span>[大护法之黑花生<br/>"
regex = re.compile('page_\d+')
page_img_list = regex.findall(text)
for page_img in page_img_list:
    new_page_img = base_url + page_img + ".jpg"
    print(new_page_img)
