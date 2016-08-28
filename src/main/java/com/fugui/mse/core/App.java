package com.fugui.mse.core;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchemaLoader;

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
			for( Map.Entry<String,String> entry : headers )
			{
				sb.append( entry.getKey() ).append( ":=" ).append( entry.getValue() ).append("<BR>\r\n");
			}

			request.bodyHandler( buffer -> {
				sb.append( "Body received: ").append( buffer.length() ).append( "<BR>\r\n" );

				byte [] data = buffer.getBytes( 5 , buffer.length() );
				sb.append( "Data:" ).append( buffer.getByte(0) ).append( " ")
					.append( buffer.getUnsignedInt(1)) .append(" ")
					.append( data.length ).append( "<BR>\r\n" );
				
				ObjectMapper mapper = new ProtobufMapper();
				try {
					String proto = "message HelloRequest {\n optional string name = 1; \n }\n" ;
				
					ProtobufSchema schema = ProtobufSchemaLoader.std.parse( proto );
					
					HelloRequest empl = mapper.readerFor(HelloRequest.class)
							   .with(schema)
							   .readValue(data);
					
					sb.append( empl );

					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
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
