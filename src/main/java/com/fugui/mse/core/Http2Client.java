package com.fugui.mse.core;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchemaLoader;

import io.vertx.core.MultiMap;
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
		
		Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
		HttpClient client = vertx.createHttpClient(
				new HttpClientOptions().setProtocolVersion(HttpVersion.HTTP_2).setHttp2ClearTextUpgrade(false));
		
		System.out.println( "Started client. ");
		HttpClientRequest request = client.post( 50051, "localhost", "/helloworld.Greeter/SayHello", response-> {
			System.out.println("Received response for service1 " + response.statusCode() );
			response.bodyHandler( buffer -> {
				StringBuilder sb = new StringBuilder();
				MultiMap headers = response.headers();
				for( Map.Entry<String,String> entry : headers )
				{
					sb.append( entry.getKey() ).append( ":=" ).append( entry.getValue() ).append("<BR>\r\n");
				}
				
				sb.append( "Body received: ").append( buffer.length() ).append( "<BR>\r\n" );

				byte [] data = buffer.getBytes( 5 , buffer.length() );
				sb.append( "Data:" ).append( buffer.getByte(0) ).append( " ")
					.append( buffer.getUnsignedInt(1)) .append(" ")
					.append( data.length ).append( "<BR>\r\n" )
					.append( buffer.toString());
				
				System.out.println( sb.toString() );
				vertx.close();
			});
		});
 
		request.setChunked(true);
		request.putHeader( ":method"  , "POST" );
		request.putHeader( ":scheme", "http");
		request.putHeader( ":path", "/helloworld.Greeter/SayHello");
		request.putHeader( ":authority", "localhost");
		request.putHeader( "content-type", "application/grpc");
		request.putHeader( "user-agent", "grpc-mse/1.0");
		request.putHeader( "te", "trailers");
		
		ObjectMapper mapper = new ProtobufMapper();
		try {
			String proto = "message HelloRequest {\n optional string name = 1; \n }\n" ;
		
			ProtobufSchema schema = ProtobufSchemaLoader.std.parse( proto );
			
			HelloRequest hr = new HelloRequest();
			hr.setName( "I Love China" );
			
			byte[] data = mapper.writer(schema).writeValueAsBytes(hr);

			// request.putHeader("content-length", String.valueOf( 5+data.length )  );
			
			Buffer buffer = Buffer.buffer();
			buffer.appendByte( (byte)0 );
			buffer.appendUnsignedInt( data.length );
			buffer.appendBytes( data );
			request.write( buffer );	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		request.end();
	
	}

}
