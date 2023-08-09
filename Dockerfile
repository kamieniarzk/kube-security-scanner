FROM openjdk:17

LABEL maintainer="kamieniarzk@gmail.com" version="1.0"

ARG KUBE_SCORE_VERSION=1.17.0
RUN curl -L "https://github.com/zegl/kube-score/releases/download/v${KUBE_SCORE_VERSION}/kube-score_${KUBE_SCORE_VERSION}_linux_amd64.tar.gz" | tar xz -C /usr/local/bin

RUN chmod +x /usr/local/bin/kube-score

ENTRYPOINT ["java","-jar", "build/libs/app.jar"]
