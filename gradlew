#!/bin/sh
# Gradle wrapper script
APP_NAME="Gradle"
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

die() { echo "$*" >&2; exit 1; }

if [ -n "$JAVA_HOME" ]; then
    if [ -x "$JAVA_HOME/jre/sh/java" ]; then
        JAVACMD=$JAVA_HOME/jre/sh/java
    else
        JAVACMD=$JAVA_HOME/bin/java
    fi
    if [ ! -x "$JAVACMD" ]; then
        die "JAVA_HOME set but java not found at $JAVACMD"
    fi
else
    JAVACMD=java
    command -v java >/dev/null 2>&1 || die "java not found"
fi

DIRNAME=$(cd "$(dirname "$0")" && pwd)
APP_HOME=$DIRNAME
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS \
    -classpath "$CLASSPATH" \
    org.gradle.wrapper.GradleWrapperMain "$@"
