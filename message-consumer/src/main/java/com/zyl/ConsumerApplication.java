package com.zyl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;

@SpringBootApplication
@EnableBinding(SimpleMessageReceiver.class)
public class ConsumerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ConsumerApplication.class)
				.web(WebApplicationType.SERVLET)
				.run(args);
	}

	@Autowired
	private SimpleMessageReceiver simpleMessageReceiver;


	@StreamListener("rabbitmq2018")
	public void onMessage(byte[] data) {
		System.out.println("StreamListener onMessage(byte[]): " + new String(data));
	}

	@StreamListener("rabbitmq2018") // Spring Cloud Stream 注解驱动
	public void onMessage2(String data2) {
		System.out.println("StreamListener onMessage2(String) : " + data2);
	}

	@ServiceActivator(inputChannel = "rabbitmq2018") // Spring Integration 注解驱动
	public void onServiceActivator(String data) {
		System.out.println("onServiceActivator(String) : " + data);
	}


	@StreamListener("mns2018")
	public void onMessage3(byte[] data) {
		System.out.println("MNS StreamListener onMessage3(byte[]): " + new String(data));
	}

	@StreamListener("mns2018") // Spring Cloud Stream 注解驱动
	public void onMessage4(String data2) {
		System.out.println("MNS StreamListener onMessage4(String) : " + data2);
	}

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
