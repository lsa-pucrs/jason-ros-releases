#!/bin/bash

# export | grep jvm
# export PATH=$PATH:/usr/lib/jvm/java-1.6.0-openjdk-i386
# export JDK_HOME=/usr/lib/jvm/java-1.6.0-openjdk-i386
# export JAVA_HOME=/usr/lib/jvm/java-1.6.0-openjdk-i386
# export | grep java-1.6.0

echo Launching ROS Core
gnome-terminal -x bash ./roscore.sh
sleep 3

echo Launching Gazebo
# gnome-terminal -x bash ./gazebo-turtle.sh
gnome-terminal -x bash ./gazebo-kobuki.sh

echo Loading Jedit. Please, wait..
cd Jason-1.4.0a
./bin/jason.sh &
sleep 3
