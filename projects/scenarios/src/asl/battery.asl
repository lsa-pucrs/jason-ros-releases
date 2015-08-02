//+battery(X) :
//	X >= 90 <-
//	.print("Battery = ", X).
//	
//+battery(X) :
//	X < 20 <-
//	.print("Battery = ", X).//;
//	//-goPatrol;
//	//+goRecharge.

+goPatrol
	<-
	  !patrol.
	  
+diagnostics(Diag) :
	Diag = "Full Battery" <-
	.print(Diag);
	.print("mas oi 1");
	-diagnostics(Diag).
	  
+diagnostics(Diag) :
	Diag = "Medium Battery" <-
	.print(Diag);
	.print("mas oi 2");
	-diagnostics(Diag).
	  
+diagnostics(Diag) :
	Diag = "Low Battery" <-
	.print(Diag);
	.print("mas oi 3");
	-diagnostics(Diag);
	-goPatrol;
	+goRecharge.
	  
+!patrol :
	goPatrol <-
		.print("========================>Patrol...");
		//whereAmI; // No artefato, comeca com 0, vai somando 1 ate chegar 3 = gera percepcao atRoom(X)
		moveF;
		moveF;		
        rotate(90);
        !cleanRoom;
        !!patrol.
        
+!patrol :
	goRecharge <-
		.print("========================>Stop Patroling...");
		!recharge.

+!cleanRoom :
	goPatrol <-
		.print("========================>Clean Room...");
		-atRoom(Any);
        updateWhereAmI; //cheguei, tenho que fazer update de local
        ?atRoom(Y); //pega valor de atRoom e joga em Y
        .print("|||||||||||||||||||||||||||||||||||||||\\: ", Y);
        clean(Y); //no artefato, para cada sala, fazer algo
        .print("========================>Room is clean");
        .wait(1000).

+!cleanRoom :
	goRecharge <-
		.print("========================> Cancel cleaning").//;
		//!recharge.
	
+!recharge :
	goRecharge <-
		.print("========================>Recharge...");
		moveF;
		rotate(90);
		moveF;
		.print("========================>Recharge (Cheguei no centro)");
		operationRecharge;
		.print("========================>Recharge (acabei recarga)");
		!return.
		
+!return:
	goRecharge <-
		.print("========================>Return...");
		rotate(90);
		.wait(500);
		rotate(90);
		.print("========================>Return (fiz meia volta)");
		moveF;
		.print("========================>Return (fiz caminhada)");
		rotate(-90);
		.print("========================>Return (fiz 1/4 volta)");
		moveF;
		.print("========================>Return (fiz caminhada)");
		rotate(90);
		.wait(500);
		rotate(90);
		.print("========================>Return (fiz meia volta DOIS)");
		operationligar;
		.print("========================>Return (cheguei, liguei bateria)");
		-goRecharge;
		+goPatrol.

+!create_battery : 
    true <-
        .print("Creating Battery artifact. The agent name is unknown.");
        makeArtifact("Battery","jason.architecture.ArtSimBattery",[],Battery).
		   
-!create_battery : 
    true <-
        .print("Battery artifact creation failed.").
		
+?toolBattery(BatteryId) :
    true <- 
        .print("Discovering Battery artifact.");
        lookupArtifact("Battery",BatteryId).
	 
-?toolBattery(BatteryId) :
    true <-
        .print("Trying to discover Battery artifact again."); 
        .wait(10);
        ?toolBattery(BatteryId). 
		