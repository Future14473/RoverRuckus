//package org.firstinspires.ftc.teamcode.RoverRuckus.old;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//@Autonomous(name = "Reset", group = "reset")
//@Disabled
//public class Reset extends LinearOpMode {
//	private OldRobot robot = new OldRobot();
//
//	@Override
//	public void runOpMode() {
//		robot.init(hardwareMap);
//		robot.drive.addLinearOpMode(this);
//		waitForStart();
//		telemetry.addLine("Doing Hook...");
//		telemetry.update();
//		robot.hooke.setPower(-1);
//		int pastPos;
//		do {
//			pastPos = robot.hooke.getCurrentPosition();
//			idle();
//			thenSleep(100);
//		} while (robot.hooke.getCurrentPosition() > pastPos &&
//		         opModeIsActive());
//		robot.hooke.setPower(0);
//		telemetry.addLine("Doing Rotation...");
//		telemetry.update();
//		robot.rotation.setPower(-0.2);
//		do {
//			pastPos = robot.arm.getCurrentPosition();
//			idle();
//			thenSleep(100);
//		} while (robot.rotation.getCurrentPosition() > pastPos + 20 &&
//		         opModeIsActive());
//		robot.arm.setPower(0);
//		telemetry.addLine("Done");
//		telemetry.update();
//	}
//}
