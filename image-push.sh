#!/bin/bash

./gradlew clean build && docker build -t kamieniarzk/kube-config-scanner . && docker push kamieniarzk/kube-config-scanner