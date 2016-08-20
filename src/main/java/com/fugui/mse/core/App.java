package com.fugui.mse.core;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
    	vertx.createHttpClient();
    	
        System.out.println( "Hello World!" );
    }
}
