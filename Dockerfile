FROM openjdk:8-jdk

MAINTAINER 502192347@qq.com

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /hipi-trace

WORKDIR /hipi-trace

EXPOSE 8080

ADD ./hipi-admin/target/hipi-admin.jar ./

CMD sleep 5;java $JAVA_OPTS -jar hipi-admin.jar
