package com.fugui.mse.core;

public class HelloRequest {
	String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "Hello Request.name:[").append( name ).append( "]").append( name.length() );
		return sb.toString();
	}
}
