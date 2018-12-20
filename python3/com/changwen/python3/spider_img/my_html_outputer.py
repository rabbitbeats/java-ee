#!/usr/bin/python
# -*- coding: UTF-8 -*-


class ImgOutputer(object):
    def __init__(self):
        self.datas = []

    def collect_data(self, data):
        if data is None:
            return
        self.datas.append(data)

    def output_html(self):
        fout = open('output.html', 'w')
        fout.write("<html>")
        fout.write("<body>")
        fout.write("<table>")

        # print("ImgOutputer", self.datas)
        for data in self.datas:
            fout.write("<tr>")
            fout.write("<td ><a href=\"%s\">%s</a></td>" % (data, data))
            fout.write("</tr>")

        fout.write("</table>")
        fout.write("</body>")
        fout.write("</html>")
        fout.close()
