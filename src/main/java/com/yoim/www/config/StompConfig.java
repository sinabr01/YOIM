package com.yoim.www.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry reg) {
		reg.enableSimpleBroker("/topic"); // 구독 주소
		reg.setApplicationDestinationPrefixes("/app"); // 발행 주소
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry reg) {
		reg.addEndpoint("/ws").setAllowedOrigins("*").withSockJS(); // dev용
	}
}