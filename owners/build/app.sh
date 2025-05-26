#!/bin/sh

if [ -f "/opt/app/extconfigs/application.${PROFILE}.yaml" ]; then
	mkdir config
	cp "/opt/app/extconfigs/application.${PROFILE}.yaml" "/opt/app/config/application.yaml"
fi

JAVA_OPTS=""

add_java_opt () {
	if [ "${2}" != "" ]; then
		JAVA_OPTS="${JAVA_OPTS} ${1}${2}"
	fi
}

add_java_opt_bool () {
	if [ "${2}" = "true" ]; then
		JAVA_OPTS="${JAVA_OPTS} ${1}"
	fi
}

add_java_opt "-XX:ActiveProcessorCount=" "${JVM_CPUCNT}"
add_java_opt "-Xms" "${JVM_MS}"
add_java_opt "-Xmx" "${JVM_MX}"
add_java_opt "-XX:MaxRAM=" "${JVM_MAXRAM}"
add_java_opt "-XX:InitialRAMPercentage=" "${JVM_INITRAMPERC}" 
add_java_opt "-XX:MaxRAMPercentage=" "${JVM_MAXRAMPERC}"
add_java_opt "-XX:MinRAMPercentage=" "${JVM_MINRAMPERC}"
add_java_opt "-XX:MaxRAMFraction=" "${JVM_MAXRAMFRAC}"
add_java_opt "-XX:MinRAMFraction=" "${JVM_MINRAMFRAC}"
add_java_opt_bool "-XX:+UseStringDeduplication" "${JVM_STRDEDUP}"
add_java_opt_bool "-XX:+UseParallelGC" "${JVM_PARALLELGC}"

echo "JAVA_OPTS ${JAVA_OPTS}"

java ${JAVA_OPTS} -jar /opt/app.jar
