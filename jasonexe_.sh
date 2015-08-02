#!/bin/bash

export | grep jvm
export PATH=$PATH:/usr/lib/jvm/java-1.6.0-openjdk-i386
export JDK_HOME=/usr/lib/jvm/java-1.6.0-openjdk-i386
export JAVA_HOME=/usr/lib/jvm/java-1.6.0-openjdk-i386
export | grep java-1.6.0

echo Loading Jedit. Please, wait..
cd ~/git/jacaros/Jason-1.4.0a
./bin/jason.sh &
sleep 3
