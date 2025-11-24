#!/bin/bash
SCRIPT_DIR="$( dirname -- "${BASH_SOURCE[0]}")"
pushd $SCRIPT_DIR > /dev/null
ant -buildfile build-local.xml
popd
