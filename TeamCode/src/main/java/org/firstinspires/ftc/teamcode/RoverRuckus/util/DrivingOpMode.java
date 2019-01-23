package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class DrivingOpMode extends LinearOpMode {
	protected Robot2 Robot;
	protected DriveHandler2 driveHandler;
	@Override
	public final void runOpMode() throws InterruptedException {
		try {
			telemetry.addLine("Initializing, Please wait");
			telemetry.addLine("DON'T PRESS START YET");
			telemetry.update();
			initialize();
			telemetry.addLine("Initialize done.");
			telemetry.update();
			waitForStart();
			telemetry.clearAll();
			run();
			finish();
		} finally {
		
		}
	}
	public abstract void initialize() throws InterruptedException;
	
	public abstract void run() throws InterruptedException;
	
	public abstract void finish() throws InterruptedException;
	
	
}
