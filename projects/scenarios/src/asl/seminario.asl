/* Initial beliefs and rules */
pos2d(home,0,0).
pos2d(me,0,0).
//pos2d(goal,4,-4). //moving this to whoAmI. Now each agent can have a different goal.

bussola("north").
//bussola("east").

//pos2d(obstacle,1,1).
//pos2d(obstacle,0,-3).
//pos2d(obstacle,1,-3).
//pos2d(obstacle,-1,-3).
//pos2d(obstacle,-11,0).
//pos2d(obstacle,-12,0).

/*
pos2d(goal,X,Y)
pos3d(goal,X,Y,Z)
pos2d(me,X,Y)
pos3d(me,X,Y,Z)
pos2d(obstacle,X,Y)
pos3d(obstacle,X,Y,Z)
pos2d(home,X,Y)
pos3d(home,X,Y,Z)
pos2d(beacon,X,Y)
pos3d(beacon,X,Y,Z)
pos2d(agent,X,Y) //agent = objects that move in the map.  We have a identity problem here. (we are aware of the limitations).
pos3d(agent,X,Y,Z)
*/

/*
+pos2d(Aa,Ab,Ac) :
	true <- 
	.print("A: ", Aa);
	.print("B: ", Ab);
	.print("C: ", Ac).
*/


/* begin */
+!start_seminario : 
    true <-
        .my_name(Name); 
        .print("Starting Agent: ", Name);
        +pos2d(goal,4,-4); //initial belief
		
		.print("Creating artifact /cmd_vel_mux");
		!create_cmd_vel_mux;
		
		!addBeliefsToSlam;		
		.print("Beliefs were added to Slam");
		
		?toolSlam_data(ABC_ArtIdSlamData);
		focus(ABC_ArtIdSlamData);
		.print("SLAM artifact focused");
		?pos2d(home,HomeX,HomeY); //we should protect this, when this fails
		?pos2d(goal,GoalX,GoalY); //we should protect this, when this fails
		.print("Got current Beliefs");
		//printSlamData;
		
		!moveToGoal;
		
		//?toolSlam_data(ABC_ArtIdSlamData);
		focus(ABC_ArtIdSlamData);
		printSlamData;		
		
		.print("End of Agent").
        
		/* TESTES...
		.print("Criando artefato SLAM");
		!create_slam_data;
		
		.print("Populando artefato SLAM");
		updateSlamData(0,0,"home");
		updateSlamData(0,1,"zero_um");
		updateSlamData(0,2,"zero_dois");
		updateSlamData(1,0,"um_zero");
		updateSlamData(1,1,"um_um");
		updateSlamData(1,2,"um_dois");
		
		//updateSlamData(0,0,"home");
		updateSlamData(0,-1,"zero_menosUm");
		updateSlamData(0,-2,"zero_menosDois");
		updateSlamData(-1,0,"menosUm_zero");
		updateSlamData(-1,-1,"menosUm_menosUm");
		updateSlamData(-1,-2,"menosUm_menosDois");
		
		updateSlamData(-1,1,"menosUm_um");
		updateSlamData(-1,2,"menosUm_dois");
		updateSlamData(1,-1,"um_menosUm");
		updateSlamData(1,-2,"um_menosDois");
		
		.print("Imprimindo artefato SLAM");
		printSlamData;
		
		.print("Pegando Visao ao redor");
		getVisionRange(0,0,2);

		
		//.print("Criando artefato do /cmd_vel");
		//!create_cmd_vel;
		//?toolCmd_vel(TestID);
		//focus(TestID);
		
		//moveRotate(270);
		//moveRotate(90);
		//!create_scan;
		//moveForward;
		*/
		
+!addBeliefsToSlam:
	true <-
		.print("Adding Initial beliefs into SLAM hashtable: ");
		!create_slam_data;
		
		// I should not insert where am I (where the current agent is) into hashtable // it should be a map for static objects (so, I will comment next lines)	
		//?pos2d(me,CurrentX,CurrentY);
		//updateSlamData(CurrentX,CurrentY,"me");
		
		?pos2d(home,HomeX,HomeY); //we should protect this, when this fails //revisit
		updateSlamData(HomeX,HomeY,"home"); 
		
		?pos2d(goal,GoalX,GoalY); //we should protect this, when this fails //revisit
		updateSlamData(GoalX,GoalY,"goal");
		
		//clearSlamData;		
		printSlamData.	
		
+!moveToGoal:
	true <-
		?pos2d(me,CurrentX,CurrentY); //we should protect this, when this fails
		?pos2d(goal,GoalX,GoalY); //we should protect this, when this fails
		.println(" --==--> Starting movement");
		//.println("****************************"); //.println(CurrentY); //.println(GoalY);
		!tryingY(CurrentY, GoalY);
		
		//check if arrived in goal
		!doWeFinished;
		//.wait(500).
		
		.println("End of movement").

+!reachedY:
	not route("reached") <- 
		.println("I have not come to the correct Y").
		
+!reachedX:
	not route("reached") <- 
		.println("I have not come to the correct X").
		
+!tryingY(CurrY, GoalY):
	true <-	
		.println("Trying to move inside Y axis");
		// taking Y axis route
		.println("###### Before getRoute");
		getRoute(CurrY, GoalY, "Y"); //Wait until get belief
		.println("###### after getRoute");
	
		// lets check if we arrived at the correct Y position	
		!reachedY;
	
		// Are route and compass the same?
		?route(Route);
		?bussola(Bussola); 	
		.println("Route = ", Route);
		.println("Compass = ", Bussola);
		!rotaIgualBussola(Route, Bussola);
	
		// Verify if there is obstacle in front of agent
		!scanObstacle; //physical check //revisit //TODO
		!obstaculoFrente; //if there is a obstacle, this plan will fail and the next plan will start
		
		// Move in the direction that the compass swing
		//!stepToGoal(Bussola);
		!stepToGoal;
		
		.println("Fim Trying Y").
	
-!tryingY(CurrY, GoalY):
	true <-
		.println("Trying to move inside Y axis - ERROR!!!!!");
		-route("reached");
		.println("reached route removed in order to try axis X");
		
		?bussola(ValorBussola);
		.println("Value to be set on route (reset) = ", ValorBussola);		
		?toolSlam_data(ArtId);
		focus(ArtId);
		updateProperties(ValorBussola,0);
		
		?pos2d(me,CurrentX,CurrentY); //we should protect this, when this fails
		?pos2d(goal,GoalX,GoalY); //we should protect this, when this fails
		
		.println("Sending current X to tryingX", CurrentX);
		.println("Sending goal X to tryingX", GoalX);
		
		!tryingX(CurrentX, GoalX).
	
+!tryingX(CurrX, GoalX):
	true <-
		.println("Trying to move inside X axis");		
		// taking X axis route
		.println("###### Before getRoute");
		getRoute(CurrX, GoalX, "X"); //Wait until get belief
		.println("###### after getRoute");
		
		// lets check if we arrived at the correct X position	
		!reachedX;
		
		// Are route and compass the same?
		?route(Route); 	
		?bussola(Bussola); 	
		.println("Route = ", Route);
		.println("Compass = ", Bussola);
		!rotaIgualBussola(Route, Bussola);
	
		// Verify if there is obstacle in front of agent
		!scanObstacle; //physical check //revisit //TODO
		!obstaculoFrente; //if there is a obstacle, this plan will fail and the next plan will start
	
		// Move in the direction that the compass swing
		//!stepToGoal(Bussola);
		!stepToGoal;
	
		.println("Fim Trying X").
	
-!tryingX(CurrX, GoalX):
	true <-
		.println("Trying to move inside X axis - ERROR!!!!!");
		-route("reached");
		.println("reached route removed in order to try axis Y");
		
		?bussola(ValorBussola);
		.println("Value to be set on route (reset) = ", ValorBussola);		
		?toolSlam_data(ArtId);
		focus(ArtId);
		updateProperties(ValorBussola,0);
		
		?pos2d(me,CurrentX,CurrentY); //we should protect this, when this fails
		?pos2d(goal,GoalX,GoalY); //we should protect this, when this fails
		
		.println("Sending current Y to tryingY", CurrentY);
		.println("Sending goal Y to tryingY", GoalY);
		
		!tryingY(CurrentY, GoalY).
		
+!doWeFinished:
	pos2d(me,Xpos,Ypos) & pos2d(goal,Xpos,Ypos) <-
		.println("!!! We finished !!!").
		
+!doWeFinished:
	pos2d(me,Xpos,Ypos) & not pos2d(goal,Xpos,Ypos) <-
		.println("!!! We do not finished yet !!!");
		!moveToGoal.

+!stepToGoal:
	true <-
		.println("I am physically moving myself");
		
		?toolSlam_data(ArtId_Slam);
		focus(ArtId_Slam);
		?pos2d(me,CurrentX,CurrentY);
		updateSlamData(CurrentX,CurrentY,"free"); //adding current position as FREE

		?toolCmd_vel_mux(C3);
		focus(C3);
		moveForward;
		!updateCurrentPosition;
		.println("One step toward the goal was taken").

+!spinToGoal:
	true <-
		.println("I'm physically turning myself");
		?graus(Graus);
		.println("Degrees = ", Graus);
		?toolCmd_vel_mux(C3);
		focus(C3);
		rotate(Graus);
		.println("One step toward the goal was taken (rotation)").

/*
+!updateCurrentPosition :
	true <-
		.println("Atualizando posicao atual");
		?pos2d(me,Xpos,Ypos);
		?bussola(Bussola);
		.println("Bussola: ", Bussola);
		.println("X atual:", Xpos);
		.println("Y atual:", Ypos);
		?toolSlam_data(C5);
		.println("passou 01");
		focus(C5);
		.println("passou 02");
		updatePos(Bussola, Xpos, Ypos);
		.println("passou 03");
		.println("Novo X:" , NewX);
		.println("Novo Y:" , NewY);
		.println("").
*/

+!updateCurrentPosition :
	bussola("north") <-
		.println("Updating current position, in case of compass = North");
		?pos2d(me,Xpos,Ypos);
		?bussola(Bussola);
		.println("current X: ", Xpos);
		.println("current Y: ", Ypos);
		
		.println("New X: ", Xpos);
		.println("New Y: ", Ypos+1);
		-pos2d(me,Xpos,Ypos);
		+pos2d(me,Xpos,Ypos+1);
		
		?pos2d(me,Xpos2,Ypos2);
		.println("X: ", Xpos2);
		.println("Y: ", Ypos2);
		.println("").
		
+!updateCurrentPosition :
	bussola("east") <-
		.println("Updating current position, in case of compass = East");
		?pos2d(me,Xpos,Ypos);
		?bussola(Bussola);
		.println("current X: ", Xpos);
		.println("current Y: ", Ypos);
		
		.println("New X: ", Xpos+1);
		.println("New Y: ", Ypos);
		-pos2d(me,Xpos,Ypos);
		+pos2d(me,Xpos+1,Ypos);
		
		?pos2d(me,Xpos2,Ypos2);
		.println("X: ", Xpos2);
		.println("Y: ", Ypos2);
		.println("").
		
+!updateCurrentPosition :
	bussola("south") <-
		.println("Updating current position, in case of compass = South");
		?pos2d(me,Xpos,Ypos);
		?bussola(Bussola);
		.println("current X: ", Xpos);
		.println("current Y: ", Ypos);
		
		.println("New X: ", Xpos);
		.println("New Y: ", Ypos-1);
		-pos2d(me,Xpos,Ypos);
		+pos2d(me,Xpos,Ypos-1);
		
		?pos2d(me,Xpos2,Ypos2);
		.println("X: ", Xpos2);
		.println("Y: ", Ypos2);
		.println("").
		
+!updateCurrentPosition :
	bussola("west") <-
		.println("Updating current position, in case of compass = West");
		?pos2d(me,Xpos,Ypos);
		?bussola(Bussola);
		.println("current X: ", Xpos);
		.println("current Y: ", Ypos);
		
		.println("New X: " , Xpos-1);
		.println("New Y: " , Ypos);
		-pos2d(me,Xpos,Ypos);
		+pos2d(me,Xpos-1,Ypos);
		
		?pos2d(me,Xpos2,Ypos2);
		.println("X: ", Xpos2);
		.println("Y: ", Ypos2);
		.println("").
		
+!updateCurrentPosition :
	bussola(Bussola) <-
		.println("Updating current position", Bussola);
		.println("").

+!obstaculoFrente:
	bussola("north") & pos2d(me,Xpos,Ypos) & not pos2d(obstacle,Xpos,Ypos+1) <-
		.println("There is no obstacle in front of me",Xpos,Ypos+1).
		
+!obstaculoFrente:
	bussola("south") & pos2d(me,Xpos,Ypos) & not pos2d(obstacle,Xpos,Ypos-1) <-
		.println("There is no obstacle in front of me",Xpos,Ypos-1).
		
+!obstaculoFrente:
	bussola("east") & pos2d(me,Xpos,Ypos) & not pos2d(obstacle,Xpos+1,Ypos) <-
		.println("There is no obstacle in front of me",Xpos+1,Ypos).
		
+!obstaculoFrente:
	bussola("west") & pos2d(me,Xpos,Ypos) & not pos2d(obstacle,Xpos-1,Ypos) <-
		.println("There is no obstacle in front of me",Xpos-1,Ypos).
		
+!scanObstacle:
	true <-
		.println("scan obstacle").
	
+!rotaIgualBussola(Rota, Bussola):
	true <-
		.println("Check if route = compass");
		.println("Route = ", Rota);
		.println("Compass = ", Bussola);
		?bussola(Rota);
		.println("Yes, I have a route to the current compass").

-!rotaIgualBussola(Rota, Bussola):
	true <-
		.println("Route != Compass. We need to make them EQUAL");
		.println("current Compass:", Bussola);
		-bussola(Bussola);				
		+bussola(Rota); 
		!spinToGoal; //Do this while the robot spin //revisit
		?bussola(Bussola2);
		.println("new compass:", Bussola2);
		?route(RotaX);
		?bussola(BussolaX);
		.println("Final Route = ", RotaX);
		.println("Final Compass= ", BussolaX);
		!rotaIgualBussola(RotaX, BussolaX);
		.println("NOW I have a route to the current compass").
		
+route(Route) :
	true <-
		println("The agent received a belief about a given rote. In this case, ROUTE = ", Route).
		/*-+rota(Route);
		-route(Route).*/

+degrees(Degrees):
	true <-
		println("The agent received a belief about a given degree. In this case, DEGREE = ", Degrees);
		-+graus(Degrees);
		-degrees(Degrees).		

/*
+rota("reached"):
	true <-
		//se chegou em X, entao tentar chegar em Y (e vice-versa) 
		.println("ROTA RETORNOU reached");
		-rota("reached").
*/

/*
tentar chegar em Y
	se acabou homeX=goalX e homeY=goalY, entao parar.
	se plano Y falhar por qualquer motivo (errro), entar chegar em X
	
tentar chegar em X
	se acabou homeX=goalX e homeY=goalY, entao parar.
	se plano X falhar por qualquer motivo (errro), entar chegar em Y
*/		

+tick_route [artifact_name(ArtIdSlamData2,"Slam_data")]
    <- println("perceived a tick_route").
		
+teste(Teste):
	true <-
		println("Recebeu o conhecimento de um TESTE. No caso, TESTE = ", Teste);
		//-+testeABC(Teste);
		+testeABC(Teste);
		-teste(Teste).
		
+teste(Y1,Y2,Y3)
  <- println("observed value A: ",Y1);
     println("observed value B: ",Y2);
     println("observed value C: ",Y3);
	 +pos2dB(Y1, Y2, Y3).
	 
+tick_oi [artifact_name(Id,"C5")]
  <- println("perceived a tick").