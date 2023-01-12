package com.devsuperior.githubuser.reader;

import java.util.List;

import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.devsuperior.githubuser.dto.UserDTO;

@Configuration
public class FetchUserDataReaderConfig {

	private final String BASE_URL = "http://localhost:8081";
	private RestTemplate restTemplate = new RestTemplate();

	@Bean
	public ListItemReader<UserDTO> fetchUserDataReader() {
		return new ListItemReader<UserDTO>(fetchUserData());
	}

	private List<UserDTO> fetchUserData() {

		String uri = BASE_URL + "/clients";

		System.out.println("Fetching data ...");
		ResponseEntity<List<UserDTO>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<UserDTO>>() {
				});

		List<UserDTO> result = response.getBody();
		return result;
	}
}
