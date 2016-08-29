package com.fugui.mse.core;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchemaLoader;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpVersion;

public class Http2Client {

	public static void main(String[] args) {

/*
:=<BR>
:=<BR>

Body received: 19<BR>
Data:0 14 14<BR>
Hello Request.name:[I Love China]12
 * 
 * 		
 */
		
		Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
		HttpClient client = vertx.createHttpClient(
				new HttpClientOptions().setProtocolVersion(HttpVersion.HTTP_2).setHttp2ClearTextUpgrade(false));
		
		HttpClientRequest request = client.post( 8080, "localhost", "/helloworld.Greeter/SayHello", response-> {
			System.out.println("Received response for service1 " + response.statusCode() );
			response.bodyHandler( buffer -> {
				System.out.println( buffer.toString() );
				
				vertx.close();
			});
		});
 
		request.putHeader( ":method"  , "POST" );
		request.putHeader( ":scheme", "http");
		request.putHeader( ":path", "/helloworld.Greeter/SayHello");
		request.putHeader( ":authority", "localhost");
		request.putHeader( "content-type", "application/grpc");
		request.putHeader( "user-agent", "grpc-go/1.0");
		request.putHeader( "te", "trailers");
		
		Buffer buffer = Buffer.buffer();
		buffer.appendByte( (byte)0 );
		
		ObjectMapper mapper = new ProtobufMapper();
		try {
			String proto = "message HelloRequest {\n optional string name = 1; \n }\n" ;
		
			ProtobufSchema schema = ProtobufSchemaLoader.std.parse( proto );
			
			HelloRequest hr = new HelloRequest();
			hr.setName( "I Love China" );
			
			byte[] data = mapper.writer(schema).writeValueAsBytes(hr);

			buffer.appendUnsignedInt( data.length );
			buffer.appendBytes( data );
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.write( buffer );		
		request.end();
	
	}

}
