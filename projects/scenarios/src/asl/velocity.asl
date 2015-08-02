/* Plans for velocity */
	   
+!create_velocity: 
    true <-
        .print("Criando Artefato de Movimentação SEM NOME AGENTE.");
        makeArtifact("Velocity","jason.architecture.ArtVelocity",[1],Velocity).
        
-!create_velocity : 
    true <-
        .print("Falha ao criar Velocity.");
		.print("Tentando criar Velocity de novo.");
		!create_velocity.
		
+?toolVelocity(VelocityId) :
    true <-
        .print("Descobrindo Artefato Velocity.");
        lookupArtifact("Velocity",VelocityId).
	 