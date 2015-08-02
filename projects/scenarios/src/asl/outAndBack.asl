!start.

+!create_cmd_vel
  <- .print("tentando"); 
  makeArtifact("Cmd_vel","jason.architecture.ArtCmdVel",[robot_1],CmdVelId);
  .print("create ok").

-!create_cmd_vel
  <- .print("cmd_vel artifact creation failed.").

+?toolCmd_vel(CmdVelId)
  <- lookupArtifact("Cmd_vel", CmdVelId).

-?toolCmd_vel(CmdVelId)
  <- .wait(10);
  ?toolCmd_vel(CmdVelId).

+!start
  <-
  .print("-----------------------inicio"); 
  !create_cmd_vel;
  .print("-----------------------create");
    ?toolCmd_vel(CmdVelId);
    .print("-----------------------tools");
    focus(CmdVelId);
    .print("-----------------------focus");
    for ( .range(I,1,2) )
      { //go out and return
      move(25); //period spend publishing
      rotate(180);
      }.