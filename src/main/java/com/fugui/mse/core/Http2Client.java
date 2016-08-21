package com.fugui.mse.core;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpVersion;

public class Http2Client {

	public static void main(String[] args) {

		Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
		HttpClient client = vertx.createHttpClient(
				new HttpClientOptions().setProtocolVersion(HttpVersion.HTTP_2).setHttp2ClearTextUpgrade(false));
		
		HttpClientRequest request = client.get( 8080, "localhost", "/service1", response-> {
			System.out.println("Received response for service1 " + response.statusCode() );
			response.bodyHandler( buffer -> {
				System.out.println( buffer.toString() );
			});
		});
		request.end();
		
		request = client.get( 8080, "127.0.0.1", "/service2", response-> {
			System.out.println("Received response for service2 " + response.statusCode() );
			response.bodyHandler( buffer -> {
				System.out.println( buffer.toString() );
			});
		});
		request.end();
	}

}
