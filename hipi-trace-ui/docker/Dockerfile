FROM nginx

COPY ./dist /data/ui

RUN rm /etc/nginx/conf.d/default.conf

ADD ./docker/hipi-trace-ui.conf /etc/nginx/conf.d/default.conf

RUN /bin/bash -c 'echo init ok'
