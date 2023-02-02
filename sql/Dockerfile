FROM mysql:8.0.20

MAINTAINER hipi-trace(502192347@qq.com)

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./1schema.sql /docker-entrypoint-initdb.d

COPY ./2hipi_trace.sql /docker-entrypoint-initdb.d

COPY ./3quartz.sql /docker-entrypoint-initdb.d
