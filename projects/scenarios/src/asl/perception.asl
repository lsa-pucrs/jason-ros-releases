/* Plans for Perception from Marcio */

+!create_perception(AgentName) : 
    true <-
        .print("Creating Perception from Marcio artifact. We are using a agent name: ", AgentName);
        makeArtifact("Perception","jason.architecture.ArtPerception",[AgentName],Perception).

-!create_perception(AgentName) : 
    true <-
        .print("Perception from Marcio artifact creation failed.").
		
+!create_perception : 
    true <-
        .print("Creating Perception from Marcio artifact. The agent name is unknown.");
        makeArtifact("Perception","jason.architecture.ArtPerception",[],Perception).

-!create_perception : 
    true <-
        .print("Perception from Marcio artifact creation failed.").
		
+?toolPerception(PerceptionID) :
    true <-
        .print("Discovering Perception from Marcio artifact.");
        lookupArtifact("Perception",PerceptionID).
	 
-?toolPerception(PerceptionID) :
    true <-
        .print("Trying to discover Perception from Marcio artifact again."); 
        .wait(10);
        ?toolPerception(PerceptionID).
        
/*		
+tick_perception [artifact_name(PerceptionIDb,"Perception")]
    <- println("perceived a tick_perception").
*/
    
+tick_perception(W1,W2,W3,W4) [artifact_name(PerceptionIDb,"Perception")]
    <-	
    	println("perceived a tick_perception");
    	println("Objeto: ", W1);
        println("Dado01: ", W2);
        println("Dado02: ", W3);
		println("Dado02: ", W4).
	
+perception(W1,W2,W3,W4)
    <-
        println("Objeto: ", W1);
        println("Dado01: ", W2);
        println("Dado02: ", W3);
		println("Dado02: ", W4).
        //-+odom(K,L).
        
+perception(X)
    <- println ("perceived a perception: ", X);
	   println ("repetindo: ", X).