#!/bin/bash
java -ea -Dfile.encoding=UTF-8 -classpath \
./target/test-classes:\
./target/classes:\
./lib/* \
com.intellij.rt.execution.junit.JUnitStarter \
-ideVersion5 \
-junit5 \
tv.zodiac.dev.testAMS_newAPI_Performance,\
test0_Add\
\(java.lang.String,java.lang.String,java.lang.String,int,int,int,int,int\)


/opt/oracle-jdk-bin-1.8.0.172/bin/java -javaagent
:/home/kir/idea-IC-182.2574.2/lib/idea_rt.jar=35221
:/home/kir/idea-IC-182.2574.2/bin -Dfile.encoding=UTF-8 -classpath /opt/oracle-jdk-bin-1.8.0.172/jre/lib/charsets.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/deploy.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/ext/cldrdata.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/ext/dnsns.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/ext/jaccess.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/ext/localedata.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/ext/nashorn.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/ext/sunec.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/ext/sunjce_provider.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/ext/sunpkcs11.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/ext/zipfs.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/javaws.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/jce.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/jsse.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/management-agent.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/plugin.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/resources.jar
:/opt/oracle-jdk-bin-1.8.0.172/jre/lib/rt.jar
:/home/kir/IdeaProjects/json_HTTP_server/target/classes
:/home/kir/.m2/repository/org/junit/jupiter/junit-jupiter-api/5.2.0/junit-jupiter-api-5.2.0.jar
:/home/kir/.m2/repository/org/apiguardian/apiguardian-api/1.0.0/apiguardian-api-1.0.0.jar
:/home/kir/.m2/repository/org/opentest4j/opentest4j/1.1.0/opentest4j-1.1.0.jar
:/home/kir/.m2/repository/org/junit/platform/junit-platform-commons/1.2.0/junit-platform-commons-1.2.0.jar
:/home/kir/.m2/repository/org/apache/httpcomponents/httpclient/4.5.5/httpclient-4.5.5.jar
:/home/kir/.m2/repository/org/apache/httpcomponents/httpcore/4.4.9/httpcore-4.4.9.jar
:/home/kir/.m2/repository/commons-codec/commons-codec/1.10/commons-codec-1.10.jar
:/home/kir/.m2/repository/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar
:/home/kir/.m2/repository/junit/junit/4.10/junit-4.10.jar
:/home/kir/.m2/repository/org/hamcrest/hamcrest-core/1.1/hamcrest-core-1.1.jar
:/home/kir/.m2/repository/org/json/json/20180130/json-20180130.jar
:/home/kir/.m2/repository/commons-logging/commons-logging/1.2/commons-logging-1.2.jar json_server
