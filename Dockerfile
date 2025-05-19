FROM openjdk:21-slim

RUN apt update && apt install -y libxext6 libxrender1 libxtst6 libxi6 libfreetype6 fontconfig

COPY src /src
RUN cd /src && javac *.java