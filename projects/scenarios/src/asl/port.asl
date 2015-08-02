+!send_info : true
  <- makeArtifact("senderPort","jason.architecture.Port",[23000]);
     sendMsg("hello1","localhost:25000");
     sendMsg("hello2","localhost:25000").
     
+!receive_msgs : true
  <- makeArtifact("receiverPort","jason.architecture.Port",[25000],Id);
     receiveMsg(Msg,Sender);
     println("received ",Msg," from ",Sender);
     focus(Id);
     startReceiving.

+new_msg(Msg,Sender)
  <- println("received ",Msg," from ",Sender).
  
 +port(X)
    <- println ("perceived a perception(port): ", X);
	   println ("repeating: ", X).