#!/usr/bin/env bash

set -o errexit

./scripts/build_all.sh

pushd nodejs
  ./run_tests.sh
popd
