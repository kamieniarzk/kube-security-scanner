FROM openjdk:17

LABEL maintainer="kamieniarzk@gmail.com" version="1.0"

ARG KUBE_SCORE_VERSION=1.17.0
RUN curl -L "https://github.com/zegl/kube-score/releases/download/v${KUBE_SCORE_VERSION}/kube-score_${KUBE_SCORE_VERSION}_linux_amd64.tar.gz" | tar xz -C /usr/local/bin
RUN chmod +x /usr/local/bin/kube-score

ARG TRIVY_VERSION=0.46.0
RUN curl -L "https://github.com/aquasecurity/trivy/releases/download/v${TRIVY_VERSION}/trivy_${TRIVY_VERSION}_Linux-64bit.tar.gz" | tar xz -C /usr/local/bin
RUN chmod +x /usr/local/bin/trivy

COPY ./build/libs/out.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
