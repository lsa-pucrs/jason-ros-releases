// jasonAgent in project tutorial.mas2j
/*
In order to run this project, first you need to accomplish the tutorial from ROS site.
Alternativelly, you can execute the instructions of readme file. 
And dont forget to run the talker and service:

roscore

(inside catkin_ws folder)
source ./devel/setup.bash
rosrun tutorial add_two_ints_server

(inside catkin_ws folder)
source ./devel/setup.bash
rosrun tutorial talker
*/

{include("tutorial.asl")}

/* Initial beliefs and rules */

/* Initial goals */
!start.

/* Plans */
+!whoAmI(jasonAgent1) :
	true <-
		.println("I am agent 01");
		.println("End of WhoAmI - agent 01").
		
+!whoAmI(jasonAgent2) :
	true <-
		.println("I am agent 02");
		.println("End of WhoAmI - agent 02").

+!whoAmI(jasonAgent) :
	true <-
		.println("Starting Forever Alone agent");		
		
		.println("Creating Artifact");		
		!create_tutorial;
		.println("Tool Artifact");
		?toolTutorial(TutorialID);
		
		.println("Starting FOR");		
		for ( .range(I,1,4) ) {
			sum(10,I*2);
			.println("Wait...");
			.wait(4000);
			focus(TutorialID);
			.println("Wait...");
			.wait(5000);
			stopFocus(TutorialID);
			};
		
		.println("End WhoAmI").
		
+!start :
	true <-		
		.my_name(Name); 
        .println("Starting Agent: ", Name);		
		!whoAmI(Name);		
		.println("--------------- END OF !start ---------------").