/* Plans for cliff */
 
+!create_cliff : 
    true <-
        .print("Criando Artefato CLIFF.");
        makeArtifact("Cliff","jason.architecture.ArtCliff",[],Cliff).
        
-!create_cliff : 
    true <-
        .print("Falha ao criar Cliff.");
		.print("Tentando criar Cliff de novo.");
		!create_cliff.

+?toolCliff(CliffId) :
    true <-
        .print("Descobrindo Artefato Cliff.");
        lookupArtifact("Cliff",CliffId).
		

+tick_Cliff_LEFT : not moving
	<- +moving;
	.print("AGENT TICK CLIFF LEFT!!");
	rotate(-90);
	moveForward;
	-moving.

+tick_Cliff_CENTER : not moving
	<- +moving;
	.print("AGENT TICK CLIFF CENTER!!");
	rotate(180);
	moveForward;
	-moving.

+tick_Cliff_RIGHT : not moving
	<- +moving;
	.print("AGENT TICK CLIFF RIGHT!!");
	rotate(90);
	moveForward;
	-moving.
	
