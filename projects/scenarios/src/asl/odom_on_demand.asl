/* Plans for odom */

+!create_odom_on_demand(AgentName) : 
    true <-
        .print("Creating Odometry artifact. We are using a agent name: ", AgentName);
        makeArtifact("Odom_on_demand","jason.architecture.ArtOdometryOnDemand",[AgentName],Odom_on_demand).
        
-!create_odom_on_demand(AgentName) : 
    true <-
        .print("Odometry artifact creation failed.").

+!create_odom_on_demand : 
    true <-
        .print("Creating Odometry artifact. The agent name is unknown.");
        makeArtifact("Odom_on_demand","jason.architecture.ArtOdometryOnDemand",[],Odom_on_demand).
		   
-!create_odom_on_demand : 
    true <-
        .print("Odometry artifact creation failed.").
		
+?toolOdom_on_demand(OdomId) :
    true <- 
        .print("Discovering Odometry artifact.");
        lookupArtifact("Odom_on_demand",OdomId).
	 
-?toolOdom_on_demand(OdomId) :
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