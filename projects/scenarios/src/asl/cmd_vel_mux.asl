/* Plans for cmd_vel_mux */

+!create_cmd_vel_mux(AgentName) : 
    true <-
        .print("Creating Cmd_vel_mux artifact. We are using an agent name: ", AgentName);
        makeArtifact("Cmd_vel_mux","jason.architecture.ArtCmdVelMux",[AgentName, 1],Cmd_vel_mux).
        
-!create_cmd_vel_mux(AgentName) :        
    true <-
        .print("cmd_vel_mux artifact creation failed.").      
	   
+!create_cmd_vel_mux : 
    true <-
        .print("Creating Cmd_vel_mux artifact. The agent name is unknown.");
        makeArtifact("Cmd_vel_mux","jason.architecture.ArtCmdVelMux",[1],Cmd_vel_mux).
        
-!create_cmd_vel_mux : 
    true <-
        .print("cmd_vel_mux artifact creation failed.").
		
+?toolCmd_vel_mux(CmdVelMuxId) :
    true <-
        .print("Discovering artifact cmd_vel_mux.");
        lookupArtifact("Cmd_vel_mux",CmdVelMuxId).
	 
-?toolCmd_vel_mux(CmdVelMuxId) :
    true <-
        .print("Discovering artifact cmd_vel_mux (using wait)."); 
        .wait(10);
        .print("Wait for cmd_vel_mux finished.");
        ?toolCmd_vel_mux(CmdVelMuxId).