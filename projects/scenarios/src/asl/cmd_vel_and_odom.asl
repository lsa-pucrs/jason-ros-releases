/* Plans for cmd_vel with odom control */

+!create_cmd_vel_odom(AgentName,Cmd_vel_Odom) : 
    true <-
        .print("Creating Cmd_vel artifact with Odom control. We are using a agent name: ", AgentName);
        .concat("Cmd_vel_Odom",AgentName,Result);
        //makeArtifact("Cmd_vel_Odom","jason.architecture.ArtCmdVelAndOdometry",[AgentName],Cmd_vel_Odom).
        .print("nome de tudo: ",Result);
        .wait(2000);
        makeArtifact(Result,"jason.architecture.ArtCmdVelAndOdometry",[AgentName],Cmd_vel_Odom).

+!create_cmd_vel_odom : 
    true <-
        .print("Creating Cmd_vel artifact with Odom control. The agent name is unknown.");
        makeArtifact("Cmd_vel_Odom","jason.architecture.ArtCmdVelAndOdometry",[],Cmd_vel_Odom).

-!create_cmd_vel_odom(AgentName) : 
    true <-
        .print("Cmd_vel with odom artifact creation failed.").
		   
-!create_cmd_vel_odom : 
    true <-
        .print("Cmd_vel with odom artifact creation failed.").
		
+?toolCmd_vel_Odom(AgentName, CmdVelOdomId) :
    true <- 
        .print("Discovering artifact cmd_vel_odom.");
        .concat("Cmd_vel_Odom",AgentName,Result);
        //lookupArtifact("Cmd_vel_Odom",CmdVelOdomId).
        .print("nome de tudo: ",Result);
        .wait(2000);
        lookupArtifact(Result,CmdVelOdomId).
	 
-?toolCmd_vel_Odom(AgentName, CmdVelOdomId) :
    true <-
        .print("Discovering artifact cmd_vel_odom (trying again)."); 
        .wait(10);
        ?toolCmd_vel_Odom(AgentName, CmdVelOdomId).
        
+tick_cmd_vel_odom
    <- println("perceived a tick_cmd_vel_odom").
    
+tick_cmd_vel_odom(Abc)
    <- println("perceived a tick_cmd_vel_odom 2a").
    
+tick_cmd_vel_odom(A,B,C)
    <- println("perceived a tick_cmd_vel_odom 2b").
	 
+tick_cmd_vel_odom [artifact_name(CmdVelOdomId,"Cmd_vel_Odom")]
    <- println("perceived a tick_cmd_vel_odom 3").
    
+odom_cmd_vel(A,B,C)
    <- .print("perceived a odom_cmd_vel 4");
    	println("X", A);
        println("Y", B);
        println("Z", C);
        -+currentPoseFromCmdVel(A,B,C).
  
+odom_cmd_vel(K,L)[A1,A2,A3,A4,A5,A6,A7]
    <-
        println("odom name: ", K);
        println("odom value: ", L);
        println("===============");
        -+odom_cmd_vel(K,L).
	
+odom_cmd_vel(Xi,Yi)
    <-
        ?toolCmd_vel_Odom(CmdVelOdomId);
        .print("Focando Odometro (forever).");
        focus(CmdVelOdomId);
        getCurrent2; //garante que vai sempre ficar recebendo info do sensor (isto é: fica loop infinito)
		.print("mais info sobre esse loop infinito, ver codigo do agente").