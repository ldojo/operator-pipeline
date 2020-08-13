#!/bin/bash
set -x

#TO_CATALOG_EAR_URL
#FROM_CATALOG_EAR_URL
#EAR_API_URL
#EAR_DOCKER_REPO
#EAR_CURL_CREDS
#CATALOG_UTILS_API_URL

echo "fetching repos of Operator Images in Artifactory"
curl -u ${EAR_CURL_CREDS} "${EAR_API_URL}docker/${EAR_DOCKER_REPO}/v2/_catalog" | jq .repositories > repositories.json

cat repositories.json

echo "fetch the FROM tar.gz package manifest catalog"
curl -u ${EAR_CURL_CREDS}  "${FROM_CATALOG_EAR_URL}" > from-catalog.tar.gz

echo "invoking api to prune FROM tar.gz catalog based on the image repos"
curl -X POST ${CATALOG_UTILS_API_URL}  -H "accept: */*" -H "Content-Type: multipart/form-data" -F "package-manifest-file=@from-catalog.tar.gz;type=application/gzip" -F "repositories-file=@repositories.json;type=application/json" > result.tar.gz

echo "uploading the result TO tar.gz catalog"
curl -u ${EAR_CURL_CREDS} -T result.tar.gz ${TO_CATALOG_EAR_URL}

echo done
