#!/usr/bin/env bash

source common.sh || source scripts/common.sh || echo "No common.sh script found..."

PROFILE="${PROFILE:-edgware}"
BOM_VERSION="${BOM_VERSION:-Edgware.BUILD-SNAPSHOT}"
SPRING_CLOUD_CONTRACT_VERSION="${SPRING_CLOUD_CONTRACT_VERSION:-1.2.4.BUILD-SNAPSHOT}"
ADDITIONAL_MAVEN_OPTS="${ADDITIONAL_MAVEN_OPTS:--Dspring-cloud.version=$BOM_VERSION -Dspring-cloud-contract.version=$SPRING_CLOUD_CONTRACT_VERSION}"
ROOT_FOLDER=${ROOT_FOLDER:-`pwd`}

set -e

cd $ROOT_FOLDER

echo -e "\nRunning the build with additional options [$ADDITIONAL_MAVEN_OPTS] and profile [$PROFILE]"

# Packages all apps in parallel using 6 cores
./mvnw clean package $ADDITIONAL_MAVEN_OPTS -P$PROFILE -U --batch-mode -Dmaven.test.redirectTestOutputToFile=true
