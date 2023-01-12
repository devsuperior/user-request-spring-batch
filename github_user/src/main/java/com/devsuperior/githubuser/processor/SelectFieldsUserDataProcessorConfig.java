package com.devsuperior.githubuser.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devsuperior.githubuser.dto.UserDTO;
import com.devsuperior.githubuser.entity.User;

@Configuration
public class SelectFieldsUserDataProcessorConfig {
	
	@Bean
	public ItemProcessor<UserDTO, User> selectFieldsUserDataProcessor() {
		return new ItemProcessor<UserDTO, User>() {

			@Override
			public User process(UserDTO item) throws Exception {
				User user = new User();
				user.setLogin(item.getLogin());
				user.setName(item.getName());
				user.setAvatarUrl(item.getAvatarUrl());
				return user;
			}

		};
	}
}
