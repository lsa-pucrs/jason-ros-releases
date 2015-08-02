/* Plans for base_scan */

+!create_base_scan(AgentName) : 
    true <-
        .print("Criando Base_scan");
        makeArtifact("Base_scan","jason.architecture.ArtBaseScan",[AgentName],Base_scan).//;
        //?toolBase_scan(C9);
        //println("Focando Base_scan .");
        //focus(C9);
        //print("get current9");
        //getCurrent9.
		
+!create_base_scan : 
    true <-
        .print("Criando Base_scan");
        makeArtifact("Base_scan","jason.architecture.ArtBaseScan",[],Base_scan).//;
//        ?toolBase_scan(C9);
//        println("Focando Base_scan .");
//        focus(C9);
//        print("get current9");
//        getCurrent9.
		
+?toolBase_scan(BaseScanId) :
    true <-
        .print("Descobrindo Base_scan .");
        lookupArtifact("Base_scan",BaseScanId).
	 
-?toolBase_scan(BaseScanId) :
    true <-
        .print("Descobrindo Base_scan (com wait)."); 
        .wait(10);
        .print("Acabei wait do Base_scan.");
        ?toolBase_scan(BaseScanId).
        
/*		
+tick_base_scan [artifact_name(BaseScanId,"Base_scan")]
    <- println("perceived a tick_base_scan").
    */
    
+tick_base_scan(W1,W2,W3,W4, W5) [artifact_name(BaseScanId,"Base_scan")]
    <-	
    	println("perceived a tick_base_scan");
    	println("Dado00: ", W1);
        println("Dado01: ", W2);
        println("Dado02: ", W3);
		println("Dado03: ", W4);
		println("Dado04: ", W5).
	
+base_scan(W1,W2,W3,W4,W5) :	 
	W5 < 2.5	
    <-
    	println("perceived a base_scan");
    	println("I will halt");
    	println("Dado00: ", W1);
        println("Dado01: ", W2);
        println("Dado02: ", W3);
		println("Dado03: ", W4);
		println("Dado04: ", W5);
		abortMoving.

+base_scan(W1,W2,W3,W4,W5)
    <-
        println("perceived a base_scan");
    	println("Dado00: ", W1);
        println("Dado01: ", W2);
        println("Dado02: ", W3);
		println("Dado03: ", W4);
		println("Dado04: ", W5).
        //-+odom(K,L).
        
+base_scan(X)
    <- println ("perceived a base_scan: ", X);
	   println ("repetindo: ", X).
	   
+scan(G,F,D,A,S) [A1,A2,A3,A4,A5,A6,A7]
     <-
     	.print("mah oi. 1");
        //println("primeiro: ", G);
        //println("segundo: ", F);
        //println("terceiro: ", D);
		//println("quarto: ", A);
		//println("quinto: ", S);
        //println("===============");
        -+scan(G,F,D,A,S).
	
+scan(Xj,Yj,Zj,Kj,Lj)
    <-
    	.print("mah oi. 2");
        ?toolBase_scan(BaseScanId);
        .print("Focando Scan (forever).");
        focus(BaseScanId);
		.print("Pegando current Scan (forever).");
        getCurrent4.