//package org.firstinspires.ftc.teamcode.RoverRuckus.testing;
//
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import org.firstinspires.ftc.teamcode.RoverRuckus.real.AbstractAuto;
//
//import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
//
//@TeleOp(group = "auto test")
//public class AutoTest extends AbstractAuto {
//	@Override
//	protected void putMarkerInDepot() {
//		drive.turn(-45, DEGREES, 10).move(-8, -12, 10).go()
//		     .then(this::openMarkerDoor)
//		     .goMove(-1, -36, 10, true)
//		     .then(this::flickMarkerOut);
//	}
//
//	@Override
//	protected void parkInCrater() {
//		drive.goMove(-1, 75, 10);
//	}
//
//	@Override
//	public void run() throws InterruptedException {
//		unHook();
//		knockOffGold();
//		putMarkerInDepot();
//		parkInCrater();
//
//		drive.waitUntilDone();
//		lookAndHook.waitUntilDone();
//	}
//}
