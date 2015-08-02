/* Plans for bumper */
   
+!create_bumper : 
    true <-
        .print("Criando Artefato BUMPER.");
        makeArtifact("Bumper","jason.architecture.ArtBumper",[],Bumper).
        
-!create_bumper : 
    true <-
        .print("Falha ao criar Bumper.");
		.print("Tentando criar Bumper de novo.");
		!create_bumper.
		
+?toolBumper(BumperId) :
    true <-
        .print("Descobrindo Artefato Bumper.");
        lookupArtifact("Bumper",BumperId).
		
		
+tick_Bumper_LEFT : not moving
	<- +moving;
	.print("AGENT TICK BUMPER LEFT!!");
	rotate(-90);
	moveForward;
	.print("Fim Moving!!");
	-moving.

	
+tick_Bumper_CENTER : not moving
	<- +moving;
	.print("AGENT TICK BUMPER CENTER!!");
	rotate(180);
	moveForward;
	moveForward;
	-moving.

+tick_Bumper_RIGHT : not moving
	<- +moving;
	.print("AGENT TICK BUMPER RIGHT!!");
	rotate(90);
	moveForward;
	.print("Fim Moving!!");
	-moving.
