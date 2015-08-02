/* Plans for cmd_vel_mux with odom control */

+!create_cmd_vel_mux_odom(AgentName, Cmd_vel_mux_odom) : 
    true <-
        .print("Creating Cmd_vel_mux_odom artifact. We are using an agent name: ", AgentName);
        .concat("Cmd_vel_mux_odom", AgentName, Result);
        makeArtifact(Result,"jason.architecture.ArtCmdVelMuxAndOdometry",[AgentName],Cmd_vel_mux_odom).
        
-!create_cmd_vel_mux_odom(AgentName, Cmd_vel_mux_odom) :        
    true <-
        .print("cmd_vel_mux_odom artifact creation failed.").
        
+?toolCmd_vel_mux_odom(AgentName, CmdVelMuxId) :
    true <-
        .print("Discovering artifact cmd_vel_mux_odom.");
        .concat("Cmd_vel_mux_odom", AgentName, Result);
        lookupArtifact(Result, CmdVelMuxId).
	 
-?toolCmd_vel_mux_odom(AgentName, CmdVelMuxId) :
    true <-
        .print("Trying to discovery cmd_vel_mux_odom again."); 
        .wait(10);
        ?toolCmd_vel_mux_odom(CmdVelMuxId).      
	   
+!create_cmd_vel_mux_odom : 
    true <-
        .print("Creating Cmd_vel_mux_odom artifact. The agent name is unknown.");
        makeArtifact("Cmd_vel_mux_odom","jason.architecture.ArtCmdVelMuxAndOdometry",[],Cmd_vel_mux_odom).
        
-!create_cmd_vel_mux_odom : 
    true <-
        .print("cmd_vel_mux_odom artifact creation failed.").
		
+?toolCmd_vel_mux_odom(CmdVelMuxId) :
    true <-
        .print("Discovering artifact cmd_vel_mux_odom.");
        lookupArtifact("Cmd_vel_mux_odom",CmdVelMuxId).
	 
-?toolCmd_vel_mux_odom(CmdVelMuxId) :
    true <-
        .print("Trying to discovery cmd_vel_mux_odom again."); 
        .wait(10);
        ?toolCmd_vel_mux_odom(CmdVelMuxId).
        
+odom_cmd_vel_mux(A,B,C)
    <- .print("perceived a odom_cmd_vel_mux");
    	println("X", A);
        println("Y", B);
        println("Z", C);
        -+currentPoseFromCmdVel(A,B,C).