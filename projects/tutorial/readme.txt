Outline:
-Verifying ROS installation
-Creating a ROS Workspace
-Creating a Package
-Writing a Simple Publisher and Subscriber in C++
-Writing a Simple Service and Client in C++

1) Verify ROS instalation. A good way to check is to ensure that environment variables like ROS_ROOT and ROS_PACKAGE_PATH are set:
export | grep ROS
OUTPUT:
declare -x ROS_PACKAGE_PATH="/opt/ros/hydro/share:/opt/ros/hydro/stacks"
declare -x ROS_ROOT="/opt/ros/hydro/share/ros"

2) Create ROS workspace
a. Choose ROS version
source /opt/ros/hydro/setup.bash
NO OUTPUT 

b. create new workspace:
$ mkdir -p catkin_ws/src
$ cd catkin_ws/src
$ catkin_init_workspace
OUTPUT: Creating symlink "/home/viki/git/meneguzzi/projects/tutorial/catkin_ws/src/CMakeLists.txt" pointing to "/opt/ros/hydro/share/catkin/cmake/toplevel.cmake"

c. compile empty workspace (compile command should be within catkin_ws folder)
cd ..
catkin_make
OUTPUT:
####
#### Running command: "make -j2 -l2" in "<your path>"
####

d. source your new setup.*sh file (maybe we should include source our path every time we need to compile it)
source devel/setup.bash
NO OUTPUT

e. make sure ROS_PACKAGE_PATH environment variable includes the "src" directory you're in.
echo $ROS_PACKAGE_PATH
OUTPUT:
<your path>/src:/opt/ros/hydro/share:/opt/ros/hydro/stacks

3) Creating a Package
cd catkin_ws/src

a. create a package called "tutorial" which depends on std_msgs and roscpp
catkin_create_pkg tutorial std_msgs roscpp
OUTPUT:
Created file tutorial/package.xml
Created file tutorial/CMakeLists.txt
Created folder tutorial/include/tutorial
Created folder tutorial/src
Successfully created files in <your path>. Please adjust the values in package.xml.

b. change directories to your tutorial package that you created in your catkin workspace:
roscd tutorial

c. create a src directory (it may already exist) in the tutorial package directory (This directory will contain any source files for our tutorial package.)
mkdir src

4) Writing a Simple Publisher and Subscriber
a. create new talker.cpp and listener.cpp and copy to src folder (or download files from ros tutorials site)
https://raw.githubusercontent.com/ros/ros_tutorials/groovy-devel/roscpp_tutorials/talker/talker.cpp
https://raw.githubusercontent.com/ros/ros_tutorials/groovy-devel/roscpp_tutorials/listener/listener.cpp

b. to build the nodes, add the following lines at the end of your CMakeLists.txt (CMakeLists.txt inside turorial folder)
include_directories(include ${catkin_INCLUDE_DIRS})

add_executable(talker src/talker.cpp)
target_link_libraries(talker ${catkin_LIBRARIES})

add_executable(listener src/listener.cpp)
target_link_libraries(listener ${catkin_LIBRARIES})

c. in your catkin workspace run the following command to build the nodes
cd ../..
catkin_make
OUTPUT:
Linking CXX executable /home/viki/git/meneguzzi/projects/tutorial/catkin_ws/devel/lib/tutorial/listener
Linking CXX executable /home/viki/git/meneguzzi/projects/tutorial/catkin_ws/devel/lib/tutorial/talker
[100%] Built target talker
[100%] Built target listener

d. Nodes are ready to be used:
roscore

(inside catkin_ws folder)
source ./devel/setup.bash
rosrun tutorial talker

(inside catkin_ws folder)
source ./devel/setup.bash
rosrun tutorial listener

5) Writing a Simple Service and Client

a. change directories to your tutorial package that you created in your catkin workspace:
roscd tutorial

b. create a srv directory (This directory will contain SERVER definitions.)
mkdir srv

c. Instead of creating a new srv definition by hand, we will copy an existing one from another package
roscp rospy_tutorials AddTwoInts.srv srv/AddTwoInts.srv

d. modify the existing line within CMakeLists.txt
find_package(catkin REQUIRED COMPONENTS roscpp rospy std_msgs message_generation)

e. in your package.xml, add the following dependencies together with the others
  <build_depend>message_generation</build_depend>
(…)
  <run_depend>message_runtime</run_depend>

f. uncomment add_service_files section, and modify to:
add_service_files(
  FILES
  AddTwoInts.srv
)

g. uncomment Generate added messages and services section
## Generate added messages and services
generate_messages(
  DEPENDENCIES
  std_msgs
)

h. create new server.cpp and server.cpp and copy to src folder (or download files from ros tutorials site)
https://github.com/ros/catkin_tutorials/blob/master/create_package_srvclient/catkin_ws/src/beginner_tutorials/src/add_two_ints_server.cpp
https://github.com/ros/catkin_tutorials/blob/master/create_package_srvclient/catkin_ws/src/beginner_tutorials/src/add_two_ints_client.cpp
(if you download, dont forget to rename the files)

i. To build the nodes, add the following lines at the end of your CMakeLists.txt:
add_executable(add_two_ints_server src/server.cpp)
target_link_libraries(add_two_ints_server ${catkin_LIBRARIES})
add_dependencies(add_two_ints_server tutorial_gencpp)

add_executable(add_two_ints_client src/client.cpp)
target_link_libraries(add_two_ints_client ${catkin_LIBRARIES})
add_dependencies(add_two_ints_client tutorial_gencpp)

j. . in your catkin workspace run the following command to build the nodes
cd ../..
catkin_make
OUTPUT:
Linking CXX executable /home/viki/git/meneguzzi/projects/tutorial/catkin_ws/devel/lib/tutorial/add_two_ints_client
Linking CXX executable /home/viki/git/meneguzzi/projects/tutorial/catkin_ws/devel/lib/tutorial/add_two_ints_server
[100%] Built target add_two_ints_client
[100%] Built target add_two_ints_server


k. Nodes are ready to be used:
roscore

(inside catkin_ws folder)
source ./devel/setup.bash
rosrun tutorial add_two_ints_server

(inside catkin_ws folder)
source ./devel/setup.bash
rosrun tutorial add_two_ints_client 2 3
