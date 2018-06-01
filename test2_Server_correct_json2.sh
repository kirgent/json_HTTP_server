#!/bin/bash
java -ea -Dfile.encoding=UTF-8 -classpath \
./target/test-classes:\
./target/classes:\
./libs/* \
com.intellij.rt.execution.junit.JUnitStarter \
-ideVersion5 \
-junit5 \
testServer_internal,test2_Server_correct_json2
