package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.Constants;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.RobotMoveController;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.PrintedRobot;

import static java.util.concurrent.TimeUnit.SECONDS;

@TeleOp(group = "test")
public class RobotMoveControllerTest extends OurLinearOpMode {
	private static final XY TO_ADD = new XY(0, 12);
	
	private final Button              a = new Button(() -> gamepad1.a);
	private       RobotMoveController moveController;
	
	@Override
	protected void initialize() throws InterruptedException {
		PrintedRobot robot = new PrintedRobot(hardwareMap);
		moveController = new RobotMoveController(robot, Constants.ENCODER_TICKS_PER_INCH,
		                                         2);
		robot.initIMU();
		waitUntil(robot::imuInitted, 3, SECONDS);
	}
	
	@Override
	protected void run() throws InterruptedException {
		moveController.reset();
		while (opModeIsActive()) {
			if (a.pressed()) {
				moveController.addToTargetLocation(TO_ADD);//half a yard up.
			}
			moveController.updateAndMove(0.5, 0.5);
		}
	}
}
