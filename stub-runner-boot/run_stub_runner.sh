#!/usr/bin/env bash

mkdir -p target
cd target
git clone https://github.com/spring-cloud-samples/github-analytics-stub-runner-boot
cd github-analytics-stub-runner-boot
./mvnw clean install
java -jar target/github-analytics-*.jar --stubrunner.workOffline="true" --stubrunner.ids="com.example:fraud-detection"