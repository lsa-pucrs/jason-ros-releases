// Agent jasonAgent in project jacaros.mas2j
{include("seminario.asl")}
{include("slam_data.asl")}
{include("port.asl")}
{include("cmd_vel_mux.asl")}
{include("cmd_vel_mux_odom.asl")}
{include("cmd_vel.asl")}
{include("perception.asl")}
{include("odom.asl")}
{include("odom_on_demand.asl")}
{include("cmd_vel_and_odom.asl")}
{include("base_scan.asl")}
{include("battery.asl")}
{include("cleaning.asl")}
{include("tutorial.asl")}

/* Initial beliefs and rules */

/* Initial goals */

!testes.

/* Plans */

+!whoAmI(jasonAgent1) :
	true <-
		.print("sou o Agente 01");		
		.print("Criando artefato 01");		
		!create_cmd_vel_mux_odom;
		.print("Tool Artefato");
		?toolCmd_vel_mux_odom(Cmd_velID);
		.print("Foco");
		focus(Cmd_velID);		
		.print("Robo vai mover");
		moveForward(1);
		/*
		for ( .range(I,1,2) ) {
			moveForward(6);
			.wait(2000);
			rotate(90);
			.wait(2000);
			moveForward(4);
			.wait(2000);
			rotate(90);
			.wait(2000);
			};
		 */
		.print("fim WhoAmI - Agente 01").
		
+!whoAmI(jasonAgent2) :
	true <-
		.print("sou o Agente 02");
		+pos2d(goal,1,1);

		/* // dois robos funcionando
		.wait(6000);
		!create_cmd_vel_odom(robot_1,Y);
		?toolCmd_vel_Odom(robot_1, Cmd_velID2);
		focus(Cmd_velID2);
		getCurrentOdom;
		.print("================= Robo 2 vai mover");		
		moveForward(10);
		getCurrentOdom;// fim dos testes com cmd_vel_odom
		*/
				
		.print("fim WhoAmI - Agente 02").

+!whoAmI(jasonAgent) :
	true <-
		.print("sou o Agente UNICO");
		//pontos 1081 - 0 a 1080?
		
		//chamar laser
		//chamar cmd_vel_mux
		
		+pos2d(goal,4,-4);
		//!create_slam_data;
		//!create_perception;
		
		.print("================= 01");		
		!create_cmd_vel_mux_odom;
		.print("================= 02");
		?toolCmd_vel_mux_odom(Cmd_velID);
		.print("================= 03");
		focus(Cmd_velID);
		.print("================= 04");
		//getCurrentOdom;
		.print("================= Robo vai mover");		
		moveF(32*5); //magic numer is 31
		.wait(2000);
		rotateD(15); //magic is 13
		.wait(2000);
		moveF(34*3.5);
		.wait(2000);
		rotateD(15); //magic is 13
		.wait(2000);
		moveF(31*5); //32*5
		.wait(2000);
		rotateD(15);
		moveF(34*3);
		
		/*
		// inicio dos testes com base_scan
		!create_base_scan(robot_1);
		?toolBase_scan(Base_scanID);
		focus(Base_scanID);// fim dos testes com base_scan
		 */
		
		/*
		// inicio dos testes com cmd_vel_odom
//		!create_cmd_vel;
//		!create_cmd_vel(jasonAgent012);
		!create_cmd_vel_odom(robot_1);
		?toolCmd_vel_Odom(Cmd_velID);
		.wait(2000);
		focus(Cmd_velID);
		getCurrentOdom;		
		moveForward(5);
//		moveRotate(-90);
		.print("WAIT");
		.wait(2500);
		focus(Cmd_velID);
		getCurrentOdom;
		.print("MOVE");				
		moveForward(5);
		getCurrentOdom;// fim dos testes com cmd_vel_odom
		*/
		

		/* so uma inicializacao simples do cmd_vel 
		.wait(3000);
		!create_cmd_vel(robot_1);
		?toolCmd_vel(Cmd_velID);
		focus(Cmd_velID);
		*/
		
		/* //inicio dos testes com odom 
		.wait(3000);
		!create_cmd_vel(robot_1);
		?toolCmd_vel(Cmd_velID);
		focus(Cmd_velID);
		
		.print("agente >> vou criar odom");
		//!create_odom;
		!create_odom(robot_1);
		.print("agente >> vou TOOl odom");
		?toolOdom(OdomID);
		.print("agente >> vou FOCUS odom");
		focus(OdomID);
		moveForward;
		getCurrentOdom;
		//rotate(-90);rotate(-90);rotate(-90);
		moveForward;
		getCurrentOdom; //fim dos testes com odom
		*/


		/*
		// inicio dos testes getCurrentOdom
		.print("nao foquei ainda");
		.wait(1800);
		.print("vou focar");
		focus(OdomID);
		.print("agente >> vou getCurrentOdom");
		getCurrentOdom;
		//.wait(2000);
		.print("vou desfocar");
		//unfocus(OdomID); -> unfocus is not working
		stopFocus(OdomID);
		// fim dos testes getCurrentOdom
		 */
		
		/* inicializacao do primeiro perception do marcio
		//!create_perception("robot1");
		!create_perception;
		?toolPerception(PerceptionID);
		focus(PerceptionID);
		*/
		
		/* inicializacao simples do scan
		!create_scan;
		.wait(5000);
		?tool_scan(ScanID);
		.wait(5000);
		*/
		
		/*
		// inicio dos testes Odom ON DEMAND
		!create_cmd_vel(robot_1);
		?toolCmd_vel(Cmd_velID);
		focus(Cmd_velID);
		
		.print("agente >> vou criar odom");
		//!create_odom_on_demand;
		!create_odom_on_demand(robot_1);
		.print("agente >> vou TOOl odom");
		?toolOdom_on_demand(OdomID);
		.print("agente >> nao foquei ainda");
		.wait(2000);
		.print("agente >> vou focar");
		focus(OdomID);
		.print("agente >> vou getCurrentOdom");
		getCurrentOdom;
		
		moveForward;
		
		.print("agente >> vou getCurrentOdom NOVAMENTE");
		getCurrentOdom;
		*/		
		
		.print("fim WhoAmI").

+!start :
true <-
		.print("chamou !start");
		//!create_perception;
		!create_cmd_vel_mux;
		?toolCmd_vel_mux(Cmd_vel_muxID);
		focus(Cmd_vel_muxID);
		moveForward;
		rotate(-90);
		.print("fim !start").
		
+!testes :
	true <-
		.print("TESTES TESTES TESTES TESTES TESTES TESTES");  
		.my_name(Name); 
        .print("Iniciando Agente: ", Name);
		
		!whoAmI(Name);
		
		//!create_perception;
		
		.print("--------------- fim !testes ---------------").
	
