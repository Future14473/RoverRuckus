//package org.firstinspires.ftc.teamcode.lib.navigation;
//
//import org.firstinspires.ftc.teamcode.RoverRuckus.MockRobot;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.firstinspires.ftc.teamcode.config.Constants.DEFAULT_MAX_ACCELERATION;
//import static org.firstinspires.ftc.teamcode.config.Constants.ENCODER_TICKS_PER_INCH;
//
//public class AutoMoveControllerTest {
//	private MockRobot          robot = new MockRobot();
//	private AutoMoveController autoMoveController;
//
//	@Before
//	public void setUp() throws Exception {
//		autoMoveController = new AutoMoveController(robot,
//		                                            ENCODER_TICKS_PER_INCH,
//		                                            DEFAULT_MAX_ACCELERATION);
//		//autoMoveController.setTranslationPIDCoefficients(new PIDCoefficients(0.1, 0, 0));
//		robot.mockSet.setNoise(0.2);
//		robot.mockSet.setLag(0.04);
//	}
//
//	@Test
//	public void straightLineTests() {
//		for (int i = 0; i < 2000; i++) {
//			straightLineTest();
//		}
//	}
//
//	public void straightLineTest() {
//		XY targ = new XY((Math.random() - 0.5) * 500,
//		                 (Math.random() - 0.5) * 500);
//		autoMoveController.setTargetLocation(targ);
//		//System.out.printf("TargetLoc: %s%n", targ);
//		int nearCount = 0;
//		int hitCount = 0;
//		XY pastLoc = XY.ZERO;
//		double error;
//		while (true) {
//			error = autoMoveController.getLocationError().magnitude();
//			autoMoveController.updateAndMove(0.8, 0.8, 0.030);
//			robot.mockSet.update(30);
//
//			XY curLoc = autoMoveController.getCurrentLocation();
////			System.out.printf("Cur Location: %s   ", curLoc)
////			          .printf("Diff Loc: %s%n",
////			                  curLoc.subtract(pastLoc));
//			if (error <= 2) {
//				if (hitCount++ > 5) break;
//			} else
//				hitCount = 0;
//			pastLoc = curLoc;
//			if (error < 4) nearCount++;
//			Assert.assertFalse(nearCount > 22); //one second
//		}
//		System.out.printf("Near count:%d%n", nearCount);
//	}
//
//	private void thenSleep(int millis) {
//		try {
//			Thread.thenSleep(millis);
//		} catch (InterruptedException ignored) {
//		}
//	}
//}