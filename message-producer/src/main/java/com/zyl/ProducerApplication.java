package com.zyl;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(SimpleMessageService.class)
public class ProducerApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(ProducerApplication.class)
				.web(WebApplicationType.SERVLET)
				.run(args);
	}


//	@StreamListener(Sink.INPUT)
//	public void handle(Person in){
//		System.out.println("Received:" + in);
//	}
//
//	public static class Person{
//		private String name;
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//
//		@Override
//		public String toString() {
//			return this.name;
//		}
//	}


}
