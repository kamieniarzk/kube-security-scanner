#!/bin/bash

# run with sudo
# for worker node - set K3S_URL to https://<master_ip>:6443 and K3S_TOKEN to contents of /var/lib/rancher/k3s/server/node-token of master

./ufw_config.sh
curl -sfL https://get.k3s.io | sh -
chmod a+r /etc/rancher/k3s/k3s.yaml

