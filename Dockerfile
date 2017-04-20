FROM centos:7

WORKDIR /home

COPY . /home

RUN cd /usr/local
RUN yum install -y deltarpm
RUN yum install -y wget
RUN yum install -y bzip2
RUN wget https://ftp.mozilla.org/pub/firefox/releases/47.0.1/linux-x86_64/en-US/firefox-47.0.1.tar.bz2 
RUN tar -jxvf firefox-47.0.1.tar.bz2 -C /usr/local/
RUN rm -rf /usr/bin/firefox
RUN ln -s /usr/local/firefox/firefox /usr/bin/firefox
RUN wget http://mirror.centos.org/centos/7/os/x86_64/Packages/xorg-x11-server-Xvfb-1.17.2-22.el7.x86_64.rpm
RUN yum install -y xorg-x11-server-Xvfb-1.17.2-22.el7.x86_64.rpm
RUN yum install -y gtk3.x86_64
RUN Xvfb :1 -screen 0 1024x768x24 &
RUN rm -rf /.cache
 
 




CMD Xvfb :1 -screen 0 1024x768x24 &&  ln -s /usr/local/firefox/firefox /usr/bin/firefox
