/* Plans for slam_data ----------------------------------------------*/
	   
+!create_slam_data : 
    true <-
        .print("Creating slam_data artifact.");
		?bussola(Direcao);
        makeArtifact("Slam_data","jason.architecture.ArtSlamData",[Direcao],Slam_data);
        ?toolSlam_data(C5);
		focus(C5).
		   
-!create_slam_data : 
    true <-
        .print("slam_data artifact creation failed.").
		
+?toolSlam_data(ArtIdSlamData) :
    true <-
        .print("Discovering slam_data artifact.");
        lookupArtifact("Slam_data",ArtIdSlamData).
	 
-?toolSlam_data(ArtIdSlamData) :
    true <-
        .print("Discovering slam_data artifact (using wait)."); 
        .wait(10);
        .print("Wait for slam_data finished.");
        ?toolSlam_data(ArtIdSlamData).
		
/* ----------------------------------------------*/