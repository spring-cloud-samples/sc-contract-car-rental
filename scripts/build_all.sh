#!/usr/bin/env bash

# port is $1
function kill_app_with_port() {
    kill -9 $(lsof -t -i:$1) && echo "Killed an app running on port [$1]" || echo "No app running on port [$1]"
}

kill_app_with_port 6543 || echo "Failed to kill app at port 6543"
kill_app_with_port 6544 || echo "Failed to kill app at port 6544"

function contractVersion() {
    local minor="${1}"
    # curl https://repo.spring.io/libs-snapshot-local/org/springframework/cloud/spring-cloud-starter-contract-verifier/maven-metadata.xml | sed -ne '/<latest>/s#\s*<[^>]*>\s*##gp') | xargs
    curl --silent https://repo.spring.io/libs-snapshot-local/org/springframework/cloud/spring-cloud-starter-contract-verifier/maven-metadata.xml | grep "<version>${minor}." | tail -1 | sed -ne '/<version>/s#\s*<[^>]*>\s*##gp' | xargs
}

BOM_VERSION="${BOM_VERSION:-}"
SPRING_CLOUD_CONTRACT_VERSION="${SPRING_CLOUD_CONTRACT_VERSION:-}"
[[ -z "${BOM_VERSION}" ]] && BOM_VERSION="Hoxton.BUILD-SNAPSHOT"
[[ -z "${SPRING_CLOUD_CONTRACT_VERSION}" ]] && SPRING_CLOUD_CONTRACT_VERSION="$( contractVersion 2.2 )"
ADDITIONAL_MAVEN_OPTS="${ADDITIONAL_MAVEN_OPTS:--Dspring-cloud.version=$BOM_VERSION -Dspring-cloud-contract.version=$SPRING_CLOUD_CONTRACT_VERSION}"
ROOT_FOLDER=${ROOT_FOLDER:-`pwd`}

set -e

cd $ROOT_FOLDER

echo -e "\nRunning the build with additional options [$ADDITIONAL_MAVEN_OPTS]"

./mvnw clean install $ADDITIONAL_MAVEN_OPTS -U --batch-mode -Dmaven.test.redirectTestOutputToFile=true
