#!/bin/bash
java -Dfile.encoding=UTF-8 -classpath \
./target/classes:\
./libs/* \
json_server