#!/bin/bash

set -x

curl -G --data-urlencode "sourceImage=$1" http://operator-pipeline-api:8080/imagePromotionTargets > promotions
echo "operator-pipeline-api service returned these image promotions:"
cat promotions
cat promotions | sed 's,\(.*\) \(.*\),oc image mirror --insecure=true --registry-config=/var/jenkins_home/promotion/auths.json \1 \2,' > promotions.sh
echo "executing promotion script: "
cat promotions.sh
bash promotions.sh
