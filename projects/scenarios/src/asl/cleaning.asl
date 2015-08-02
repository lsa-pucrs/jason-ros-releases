+vacuum(Object, Room)
<-
	.print("XXXXXXXXXXXXXXXXXXX=================XXXXXXXXXXXXXXXXXXXXXXXXXXX");
	.print("At ", Room);
	.println(". vacuum ", Object);
	-vacuum(Object, Room).

+mop(Object, Room)
<-
	.print("XXXXXXXXXXXXXXXXXXX=================XXXXXXXXXXXXXXXXXXXXXXXXXXX");
	.print("At ", Room);
	.println(". mop ", Object);
	-mop(Object, Room).

+scrub(Object, Room)
<-
	.print("XXXXXXXXXXXXXXXXXXX=================XXXXXXXXXXXXXXXXXXXXXXXXXXX");
	.print("At ", Room);
	.println(". scrub ", Object);
	-scrub(Object, Room).

+!create_cleaner : 
    true <-
        .print("Creating Cleaner artifact. The agent name is unknown.");
        makeArtifact("Cleaner","jason.architecture.ArtCleaning",[],Cleaner).
		   
-!create_cleaner : 
    true <-
        .print("Cleaner artifact creation failed.").
		
+?toolCleaner(CleanerId) :
    true <- 
        .print("Discovering Cleaner artifact.");
        lookupArtifact("Cleaner",CleanerId).
	 
-?toolCleaner(CleanerId) :
    true <-
        .print("Trying to discover Cleaner artifact again."); 
        .wait(10);
        ?toolCleaner(CleanerId). 
		