#!/bin/bash

export OPENALPR_LOCATION_DYLIB="`pwd`/openalpr-lib/linux/libopenalpr.so"
export OPENALPR_LOCATION_JNI="`pwd`/openalpr-lib/linux/libopenalprjni.so"
echo "OPENALPR_LOCATION_DYLIB = ${OPENALPR_LOCATION_DYLIB}"
echo "OPENALPR_LOCATION_JNI = ${OPENALPR_LOCATION_JNI}"
mvn -Plinux spring-boot:run
