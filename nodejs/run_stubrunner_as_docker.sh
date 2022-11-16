#!/bin/bash

export RUN_AS_SUDO="${RUN_AS_SUDO:-false}"
export DOCKER_CMD="${DOCKER_CMD:-docker}"
if [[ ${RUN_AS_SUDO} == "true" ]]; then
  DOCKER_CMD="sudo ${DOCKER_CMD}"
fi

# Provide the Spring Cloud Contract Docker version
SC_CONTRACT_DOCKER_VERSION="4.0.0-SNAPSHOT"
# Spring Cloud Contract Stub Runner properties
STUBRUNNER_PORT="8750"
# Stub coordinates 'groupId:artifactId:version:classifier:port'
STUBRUNNER_IDS="com.example:fraud-detection:0.0.1-SNAPSHOT:stubs:9876"
# Run the docker with Stub Runner Boot
${DOCKER_CMD} run  --rm -e "STUBRUNNER_IDS=${STUBRUNNER_IDS}" \
-e "STUBRUNNER_STUBS_MODE=LOCAL" \
-p "${STUBRUNNER_PORT}:${STUBRUNNER_PORT}" \
-p "9876:9876"  \
-v "${HOME}/.m2/:/home/scc/.m2:rw" \
springcloud/spring-cloud-contract-stub-runner:"${SC_CONTRACT_DOCKER_VERSION}"
