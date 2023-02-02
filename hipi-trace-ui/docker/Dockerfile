FROM nginx

COPY ./h5 /data/trace-h5

COPY ./dist /data/trace-ui

RUN rm /etc/nginx/conf.d/default.conf

ADD ./hipi-trace-ui.conf /etc/nginx/conf.d/default.conf

RUN /bin/bash -c 'echo init ok'
