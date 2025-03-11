FROM flink:1.20

RUN mkdir -p ./plugins/s3-fs-hadoop
RUN cp ./opt/flink-s3-fs-hadoop-1.20.1.jar ./plugins/s3-fs-hadoop/