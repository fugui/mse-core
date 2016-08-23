package com.fugui.mse.core;

public class Fuchsia {
	
	/**
	 * Single instance
	 */
	static Fuchsia fuchsia;
	public static Fuchsia fuchsia()
	{
		return fuchsia;
	}
	
	public void init()
	{
		
	}
	
	public void registerService( Class<?> cls, String idl )
	{
		
	}
	
	public <T> T getBean(Class<T> cls) throws InstantiationException, IllegalAccessException
	{
		return cls.newInstance();
	}

}
