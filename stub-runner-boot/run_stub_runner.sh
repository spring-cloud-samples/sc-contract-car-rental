#!/bin/bash

set -o errexit

mkdir -p target
STUBRUNNER_VERSION="${STUBRUNNER_VERSION:-2.0.2.BUILD-SNAPSHOT}"
if [ ! -f "target/stub-runner.jar" ]; then
    case "${STUBRUNNER_VERSION}" in
      *RELEASE*)
        wget -O target/stub-runner.jar "https://search.maven.org/remotecontent?filepath=org/springframework/cloud/spring-cloud-contract-stub-runner-boot/${STUBRUNNER_VERSION}/spring-cloud-contract-stub-runner-boot-${STUBRUNNER_VERSION}.jar"
        ;;
      *SR*)
        wget -O target/stub-runner.jar "https://search.maven.org/remotecontent?filepath=org/springframework/cloud/spring-cloud-contract-stub-runner-boot/${STUBRUNNER_VERSION}/spring-cloud-contract-stub-runner-boot-${STUBRUNNER_VERSION}.jar"
        ;;
      *)
        wget -O target/stub-runner.jar "https://repo.spring.io/libs-snapshot/org/springframework/cloud/spring-cloud-contract-stub-runner-boot/${STUBRUNNER_VERSION}/spring-cloud-contract-stub-runner-boot-${STUBRUNNER_VERSION}.jar"
        ;;
    esac
else
    echo "Stub Runner already downloaded"
fi
java -jar target/stub-runner.jar --stubrunner.stubsMode="LOCAL" --stubrunner.ids="com.example:fraud-detection:+:9876" --server.port=8750
