// Out-And-Back agent in project scenarios.mas2j
/* 
 * For this scenario we need the following:
 * 1. start ROS: 
 * 		$ roscore
 * 
 * 2. Start Gazebo (empty environment with turtlebot): 
 * 		$ roslaunch turtlebot_gazebo turtlebot_empty_world.launch
 * 
 * If the project fails when creating artifact, try to run it again.
 */
!start.

+!create_cmd_vel
	<- .print("Creating artifact..."); 
	makeArtifact("Cmd_vel","jason.architecture.ArtCmdVelMux",[],CmdVelId).

-!create_cmd_vel
	 <- .print("cmd_vel artifact creation failed.").

+?toolCmd_vel(CmdVelId)
	<- .print("Discovering artifact...");
	lookupArtifact("Cmd_vel", CmdVelId).

-?toolCmd_vel(CmdVelId)
	<- .wait(10);
	?toolCmd_vel(CmdVelId).

 +!start
 	<-.print("Starting...");
 	!create_cmd_vel;
 	?toolCmd_vel(CmdVelId);
 	.print("Focusing artifact...");
 	focus(CmdVelId);
 	for ( .range(I,1,2) ) { //go out and return
 		move(25); //period spend publishing
 		rotate(180);
 		}.