#!/bin/bash

set -o errexit

WAIT_TIME="${WAIT_TIME:-15}"

function clean() {
  docker stop $(docker ps | grep spring-cloud-contract | awk '{print $1}') && echo "Killed a running container" || echo "Nothing running"
  pkill -9 -f stubrunner && echo "Killed stub runner jar" || echo "Failed to kill stub runner jar"
}

# Stop any running images
clean

# Run Stub Runner
echo "Running tests against Stub Runner Docker Image"
nohup ./run_stubrunner_as_docker.sh &
echo "Waiting for the image to start for [${WAIT_TIME}] seconds"
sleep ${WAIT_TIME}

# Execute tests
TEST_PASSED="false"
echo "Working around certificate issues" && npm config set strict-ssl false
yes | npm install || echo "Failed to install packages"
node app && TEST_PASSED="true"

if [[ "${TEST_PASSED}" == "false" ]]; then
  echo "Test has failed!"
  clean
  exit 1
fi

echo "Tests passed with Docker image!"

clean

# Execute tests
echo "Running tests against a Stub Runner fat jar"
TEST_PASSED="false"
nohup ./run_stubrunner_as_process.sh &
echo "Waiting for the image to start for [${WAIT_TIME}] seconds"
sleep ${WAIT_TIME}
node app && TEST_PASSED="true"

if [[ "${TEST_PASSED}" == "false" ]]; then
  echo "Test has failed!"
  clean
  exit 1
fi

echo "Tests passed with running Stub Runner as a standalone JAR!"

clean
