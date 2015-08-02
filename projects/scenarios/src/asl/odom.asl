/* Plans for odom */

+!create_odom(AgentName) : 
    true <-
        .print("Creating Odometry artifact. We are using a agent name: ", AgentName);
        makeArtifact("Odom","jason.architecture.ArtOdometry",[AgentName],Odom).
        
-!create_odom(AgentName) : 
    true <-
        .print("Odometry artifact creation failed.").

+!create_odom : 
    true <-
        .print("Creating Odometry artifact. The agent name is unknown.");
        makeArtifact("Odom","jason.architecture.ArtOdometry",[],Odom).
		   
-!create_odom : 
    true <-
        .print("Odometry artifact creation failed.").
		
+?toolOdom(OdomId) :
    true <- 
        .print("Discovering Odometry artifact.");
        lookupArtifact("Odom",OdomId).
	 
-?toolOdom(OdomId) :
    true <-
        .print("Trying to discover Odometry artifact again."); 
        .wait(10);
        ?toolOdom(OdomId).        
    
+odom(A,B,C)
    <- .print("a odometry property was perceived");
    	println("X: ", A);
        println("Y: ", B);
        println("Z: ", C);
        -+currentPose(A,B,C).