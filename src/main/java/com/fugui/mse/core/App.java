package com.fugui.mse.core;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));

		// HttpServerOptions options = new HttpServerOptions();
		// options.setAlpnVersions(alpnVersions)

		HttpServer server = vertx.createHttpServer();
		server.requestHandler(request -> {
			StringBuilder sb = new StringBuilder();
			sb.append("Request Version:").append(request.version()).append("<BR>\r")
				.append("Request Method:").append(request.method());

			System.out.println( sb );
			request.response().end(sb.toString());

		}).connectionHandler( conn -> {
			System.out.println( "Received connection:" );
			conn.closeHandler( c -> { System.out.println("Connection Closed."); });
		})
		.listen(8080, res -> {
			if (res.succeeded())
				System.out.println("Server listion at " + server.actualPort());
		});
	}
}
