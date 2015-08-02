// Fake Battery agent in project scenarios.mas2j
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

/* Plans for Battery */
+!create_battery <-
	.print("Creating Battery artifact");
	makeArtifact("Battery","jason.architecture.ArtSimBattery",[],BatteryId).

-!create_battery <-
	.print("Battery artifact creation failed.").

+?toolBattery(BatteryId) <-
	.print("Discovering Battery artifact.");
	lookupArtifact("Battery",BatteryId).

-?toolBattery(BatteryId) <-
	.wait(10);
	?toolBattery(BatteryId).

/* Plans for Moviments */

+!create_cmd_vel
	<- .print("Creating Cmd_Vel artifact..."); 
	makeArtifact("Cmd_vel","jason.architecture.ArtCmdVelMux",[],CmdVelId).

-!create_cmd_vel
	 <- .print("cmd_vel artifact creation failed.").

+?toolCmd_vel(CmdVelId)
	<- .print("Discovering Cmd_vel artifact...");
	lookupArtifact("Cmd_vel", CmdVelId).

-?toolCmd_vel(CmdVelId)
	<- .wait(10);
	?toolCmd_vel(CmdVelId).

/* Diagnostics Reactions */
+diagnostics(Diag) : Diag = "Full Battery" <-
	.print("Current Diagnostic: ",Diag);
	-diagnostics(Diag).
	
+diagnostics(Diag) : Diag = "Medium Battery" <-
	.print("Current Diagnostic: ",Diag);
	-diagnostics(Diag).
	
+diagnostics(Diag) : Diag = "Low Battery" <-
	.print("Current Diagnostic: ",Diag);
	-diagnostics(Diag);	
	.print("Suspending Patrol plan");
	.suspend(patrol);
	!recharge.

/* Plans for Patrol */

+!patrol <-
	.print("patrolling");
	move(25); //moveForward(2);
	rotate(90);
	.print("patrolling (end)");
	!!patrol. // recursive call.

+!recharge <-
	.print("Moving to dock");
	move(25); //moveForward(1);
	rotate(90);
	move(25); //moveForward(1); //Now agent arrived in Recharge Base
	.print("Arrived in dock");
	operationRecharge;
	.print("Recharge Finished");
	!return. //Return to previous square corner

+!return <-
	.print("Getting out from dock");
	rotate(180); //Turned around
	move(25); //moveForward(1);
	rotate(-90);
	move(25); //moveForward(1); //original position
	.print("Original Position");
	rotate(180); //original orientation
	.print("Original Orientation");
	.print("Resuming patrol plan");
	.resume(patrol).

/* main */
+!start <-
	.print("Starting...");
	
	!create_cmd_vel;
	?toolCmd_vel(CmdVelMuxId);
	.print("Focusing cmd_vel_mux_odom artifact...");
	focus(CmdVelMuxId);	
	
	!create_battery;
	?toolBattery(BatteryId);
	.print("Focusing battery artifact...");
	focus(BatteryId);
	!patrol.