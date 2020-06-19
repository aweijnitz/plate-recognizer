#!/bin/bash

export OPENALPR_LOCATION_DYLIB="`pwd`/openalpr-lib/macosx/libopenalpr.2.dylib"
export OPENALPR_LOCATION_JNI="`pwd`/openalpr-lib/macosx/libopenalprjni.so"

mvn clean package spring-boot:run
