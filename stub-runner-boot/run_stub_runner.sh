#!/bin/bash

set -o errexit

mkdir -p target
STUBRUNNER_VERSION="${STUBRUNNER_VERSION:-2.1.0.BUILD-SNAPSHOT}"
LOCATION=""
case "${STUBRUNNER_VERSION}" in
  *RELEASE*)
    LOCATION="https://search.maven.org/remotecontent?filepath=org/springframework/cloud/spring-cloud-contract-stub-runner-boot/${STUBRUNNER_VERSION}/spring-cloud-contract-stub-runner-boot-${STUBRUNNER_VERSION}.jar"
    ;;
  *SR*)
    LOCATION="https://search.maven.org/remotecontent?filepath=org/springframework/cloud/spring-cloud-contract-stub-runner-boot/${STUBRUNNER_VERSION}/spring-cloud-contract-stub-runner-boot-${STUBRUNNER_VERSION}.jar"
    ;;
  *)
    LOCATION="https://repo.spring.io/libs-snapshot/org/springframework/cloud/spring-cloud-contract-stub-runner-boot/${STUBRUNNER_VERSION}/spring-cloud-contract-stub-runner-boot-${STUBRUNNER_VERSION}.jar"
    ;;
esac
echo "For version [${STUBRUNNER_VERSION}] Stub Runner JAR download location is [${LOCATION}]"
if [ ! -f "target/stub-runner.jar" ]; then
    wget -O target/stub-runner.jar "${LOCATION}"
else
    echo "Stub Runner already downloaded"
fi
java -jar target/stub-runner.jar --stubrunner.stubsMode="LOCAL" --stubrunner.ids="com.example:fraud-detection:+:9876" --server.port=8750
