#!/bin/bash

./gradlew clean build && docker build --platform linux/amd64 -t kamieniarzk/kube-security-scanner . && docker push kamieniarzk/kube-security-scanner
