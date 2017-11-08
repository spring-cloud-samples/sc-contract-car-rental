#!/usr/bin/env bash

mkdir -p target
cd target
git clone https://github.com/spring-cloud-samples/stub-runner-boot || REPO_CLONED="true"
cd stub-runner-boot
if [[ "${REPO_CLONED}" != "true" ]]; then
	./mvnw clean install
fi
java -jar target/stub-runner-boot-*.jar --stubrunner.workOffline="true" --stubrunner.ids="com.example:fraud-detection:+:9876"
