package org.firstinspires.ftc.teamcode.lib.util;

import org.junit.Before;
import org.junit.Test;

public class ReflectionsTest {
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
		System.out.println(Reflections.nameFor(testFace));
		System.out.println(Reflections.nameFor(new Inner()));
		//System.out.println(Reflections.nameFor((TestFace) () -> {}));
		
	}
	
	interface TestFace {
		void a();
	}
	
	private class Inner implements TestFace {
		
		@Override
		public void a() {
		
		}
	}
}