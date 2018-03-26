#!/bin/bash

set -o errexit

mkdir -p target
STUBRUNNER_VERSION="${STUBRUNNER_VERSION:-1.2.3.RELEASE}"
if [ ! -f "target/stub-runner.jar" ]; then
    wget -O target/stub-runner.jar "https://search.maven.org/remote_content?g=org.springframework.cloud&a=spring-cloud-contract-stub-runner-boot&v=${STUBRUNNER_VERSION}"
else
    echo "Stub Runner already downloaded"
fi
java -jar target/stub-runner.jar --stubrunner.workOffline="true" --stubrunner.ids="com.example:fraud-detection:+:9876"
