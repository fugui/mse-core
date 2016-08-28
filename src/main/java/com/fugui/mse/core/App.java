package com.fugui.mse.core;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;

import java.util.Map;
import io.vertx.core.MultiMap;

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
			sb.append( request.version() ).append( " ").append(request.method())
				.append( request.uri() ).append("<BR>\r\n");

			MultiMap headers = request.headers();
			for( Map.Entry entry : headers )
			{
				sb.append( entry.getKey() ).append( ":=" ).append( entry.getValue() ).append("<BR>\r\n");
			}

			request.bodyHandler( buffer -> {
				sb.append( "Body received: ").append( buffer.length() );
				System.out.println( sb );

				request.response().end(sb.toString());
			});
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
