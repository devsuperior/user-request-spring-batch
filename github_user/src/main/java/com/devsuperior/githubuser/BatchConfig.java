package com.devsuperior.githubuser;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BatchConfig {

	private final String BASE_URL = "http://localhost:8081";
	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	@Qualifier("transactionManagerApp")
	private PlatformTransactionManager transactionManager;

	@Bean
	public Job job(Step step, JobRepository jobRepository) {
		return new JobBuilder("job", jobRepository)
				.start(step)
				.incrementer(new RunIdIncrementer())
				.build();
	}

	@Bean
	public Step step(ItemReader<UserDTO> reader, ItemWriter<User> writer, JobRepository jobRepository) {
		return new StepBuilder("step", jobRepository)
				.<UserDTO, User>chunk(10, transactionManager)
				.reader(reader)
				.processor(processor())
				.writer(writer)
				.build();
	}

	@Bean
	public ListItemReader<UserDTO> reader() {
		return new ListItemReader<UserDTO>(fetchUserData());
	}

	public ItemProcessor<UserDTO, User> processor() {
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

	@Bean
	public ItemWriter<User> userWriter(@Qualifier("appDS") DataSource dataSource) {		
		return new JdbcBatchItemWriterBuilder<User>()
		        .dataSource(dataSource)
		        .sql(
		            "INSERT INTO user (login, name, avatar_url) VALUES (:login, :name, :avatarUrl)")
		        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
		        .build();
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

	@BeforeChunk
	private void beforeChunk(ChunkContext context) {
		System.out.println("before Chunk");
	}

	@AfterChunk
	private void afterChunk(ChunkContext context) {
		System.out.println("after Chunk");
	}
}
