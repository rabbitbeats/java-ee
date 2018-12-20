# from 指令：指明当前镜像继承的基镜像，编译当前镜像时会自动下载基镜像
FROM java:7

# maintainer指令：指明了当前镜像的作者
MAINTAINER liucw

# add指令：是从当前工作目录复制文件到镜像目录中去
ADD  default.jar app.jar

# expose指令：指明了镜像运行时的容器必需监听指定的端口
EXPOSE 8080

# entrypoint指令：可让容器像一个可执行程序一样运行，这样镜像运行时可以像软件一样接收参数执行。该指令是运行时(run)的动作
ENTRYPOINT ["java","-jar","/app.jar"]



# cmd指令：指明了启动镜像容器时的默认行为。一个Dockerfile里只能有一个CMD指令。cmd指令里设定的命令可以在运行镜像时使用参数覆盖。cmd是运行时(run)的动作。
# 如 CMD echo "this is a test"   可被docker run -d image_name echo "this is not a test"覆盖

# env指令：可用来设置环境变量 如ENV myName=lcw 或 ENV myName lcw

# run指令：可以在当前镜像上执行Linux命令并形成一个新的层。run是编译时(build)的动作
