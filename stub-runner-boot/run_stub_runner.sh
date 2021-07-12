#!/bin/bash

set -o errexit

function contractVersion() {
    local minor="${1}"
    # curl https://repo.spring.io/libs-snapshot-local/org/springframework/cloud/spring-cloud-starter-contract-verifier/maven-metadata.xml | sed -ne '/<latest>/s#\s*<[^>]*>\s*##gp') | xargs
    curl --silent https://repo.spring.io/libs-snapshot-local/org/springframework/cloud/spring-cloud-starter-contract-verifier/maven-metadata.xml | grep "<version>${minor}." | tail -1 | sed -ne '/<version>/s#\s*<[^>]*>\s*##gp' | xargs
}

[[ -z "${STUBRUNNER_VERSION}" ]] && STUBRUNNER_VERSION="$( contractVersion 3.1 )"

mkdir -p target
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
