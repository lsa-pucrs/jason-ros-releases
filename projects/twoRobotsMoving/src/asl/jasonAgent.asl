// jasonAgent in project jacaros.mas2j
{include("cmd_vel_and_odom.asl")}
{include("odom.asl")}
{include("odom_on_demand.asl")}

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!whoAmI(jasonAgent1) :
	true <-
		.print("I am agent 01");
		//two robots moving at same time
		!create_cmd_vel_odom(robot_0,X);
		?toolCmd_vel_Odom(robot_0, Cmd_velID1);
		focus(Cmd_velID1);
		getCurrentOdom;
		.print("================= Robot 1 will move");		
		moveForward(15);
		getCurrentOdom;						
		.print("End of WhoAmI - agent 01").
		
+!whoAmI(jasonAgent2) :
	true <-
		.print("I am agent 02");
		//two robots moving at same time
		.wait(6000);
		!create_cmd_vel_odom(robot_1,Y);
		?toolCmd_vel_Odom(robot_1, Cmd_velID2);
		focus(Cmd_velID2);
		getCurrentOdom;
		.print("================= Robot 2 will move");		
		moveForward(10);
		getCurrentOdom;					
		.print("End of WhoAmI - agent 02").

+!whoAmI(jasonAgent) :
	true <-
		.print("I am a Forever Alone agent");
		.print("End of WhoAmI").
		
+!start :
	true <-
		.my_name(Name); 
        .print("Starting Agent: ", Name);		
		!whoAmI(Name);		
		.print("--------------- end of !start ---------------").