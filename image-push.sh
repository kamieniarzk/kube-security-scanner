#!/bin/bash

./gradlew clean build && docker build --platform linux/amd64 -t kamieniarzk/kube-config-scanner . && docker push kamieniarzk/kube-config-scanner