package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import org.junit.Before;
import org.junit.Test;

public class MoreReflectTest {
	private TestFace testFace;
	
	@Before
	public void setUp() throws Exception {
		testFace = new TestFace() {
			@Override
			public void a() {
			
			}
		};
	}
	
	@Test
	public void getName() {
		System.out.println(MoreReflect.getInformativeName(testFace));
		System.out.println(MoreReflect.getInformativeName((TestFace) () -> {}));
	}
	
	interface TestFace {
		void a();
	}
}