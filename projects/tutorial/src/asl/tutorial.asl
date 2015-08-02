/* Plans for tutorial */

+!create_tutorial(AgentName) : 
    true <-
        .print("Creating Tutorial artifact. We are using a agent name: ", AgentName);
        makeArtifact("Tutorial","jason.architecture.ArtTutorial",[AgentName],Tutorial).
        
-!create_tutorial(AgentName) : 
    true <-
        .print("Tutorial artifact creation failed.").

+!create_tutorial : 
    true <-
        .print("Creating Tutorial artifact. The agent name is unknown.");
        makeArtifact("Tutorial","jason.architecture.ArtTutorial",[],Tutorial).
		   
-!create_tutorial : 
    true <-
        .print("Tutorial artifact creation failed.").
		
+?toolTutorial(TutorialId) :
    true <- 
        .print("Discovering Tutorial artifact.");
        lookupArtifact("Tutorial",TutorialId).
	 
-?toolTutorial(TutorialId) :
    true <-
        .print("Trying to discover Tutorial artifact again."); 
        .wait(10);
        ?toolTutorial(TutorialId).        
    
+chatter(A)
    <- .println("a chatter from service was perceived");
    	println("Message: ", A);
        -+currentMsg(A).
        
+twoIntsSum(A)
    <- .println("a somatory from service was perceived");
    	println("Value: ", A);        
        -+currentSum(A).