#!/bin/bash

# export OPENALPR_LOCATION_DYLIB="`pwd`/openalpr-lib/macosx/libopenalpr.2.dylib"
# export OPENALPR_LOCATION_JNI="`pwd`/openalpr-lib/macosx/libopenalprjni.so"
# echo "OPENALPR_LOCATION_DYLIB = ${OPENALPR_LOCATION_DYLIB}"
# echo "OPENALPR_LOCATION_JNI = ${OPENALPR_LOCATION_JNI}"

mvn -Pmacosx clean package spring-boot:run
