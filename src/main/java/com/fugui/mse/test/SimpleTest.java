package com.fugui.mse.test;

import com.fugui.mse.core.Fuchsia;

public class SimpleTest {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		Fuchsia fuchsia = Fuchsia.fuchsia();
		
		fuchsia.init();
		
		fuchsia.registerService( SimpleService.class, "" );
		
		SimpleService ss = fuchsia.getBean( SimpleService.class );
		
		System.out.println( ss.sayHello( "Fuchsia") );
	}

}
