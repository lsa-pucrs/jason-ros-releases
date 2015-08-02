// Fake Battery agent in project scenarios.mas2j
/* 
 * For this scenario we ROS. It will use our artifacts
 */
!start.

+!create_battery <-
	.print("Creating Battery artifact");
	makeArtifact("Battery","jason.architecture.ArtSimBattery",[],BatteryId).

-!create_battery <-
	.print("Battery artifact creation failed.").

+?toolBattery(BatteryId) <-
	.print("Discovering Battery artifact.");
	lookupArtifact("Battery",BatteryId).

-?toolBattery(BatteryId) <-
	.wait(10);
	?toolBattery(BatteryId).

+diagnostics(Diag) <-
	.print("Current Diagnostic: ",Diag);
	-diagnostics(Diag).

+!start <-
	.print("Starting...");
	!create_battery;
	?toolBattery(BatteryId);
	.print("Focusing artifact...");
	focus(BatteryId).