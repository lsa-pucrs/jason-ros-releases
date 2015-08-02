/* Plans for cmd_vel */

+!create_cmd_vel(AgentName) : 
    true <-
        .print("Criando Artefato de Movimentação COM NOME AGENTE.");
        makeArtifact("Cmd_vel","jason.architecture.ArtCmdVel",[AgentName],Cmd_vel).

-!create_cmd_vel(AgentName) :        
    true <-
        .print("Falha ao criar cmd_vel.").        
	   
+!create_cmd_vel : 
    true <-
        .print("Criando Artefato de Movimentação SEM NOME AGENTE.");
        makeArtifact("Cmd_vel","jason.architecture.ArtCmdVel",[],Cmd_vel).        
        
-!create_cmd_vel : 
    true <-
        .print("Falha ao criar cmd_vel.").
		
+?toolCmd_vel(CounterId3) :
    true <-
        .print("Descobrindo Artefato cmd_vel.");
        lookupArtifact("Cmd_vel",CounterId3).
	 
-?toolCmd_vel(CounterId3) :
    true <-
        .print("Descobrindo Artefato cmd_vel (com wait)."); 
        .wait(10);
        .print("Acabei wait.");
        ?toolCmd_vel(CounterId3).