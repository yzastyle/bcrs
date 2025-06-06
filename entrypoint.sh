#!/usr/bin/env sh
echo "Running a Java Application with docker..."
exec /usr/bin/java -XX:+UseContainerSupport -Xmx256m -Xss512k -XX:MetaspaceSize=100m -jar /bank-card/app.jar
