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
	private int secondsToSleep = 5;
//	private HttpHeaders headers = null;
//	private HttpEntity<String> request = null;

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
	public Step step(ItemReader<User> reader, ItemWriter<User> writer, JobRepository jobRepository) {
		return new StepBuilder("step", jobRepository)
				.<User, User>chunk(10, transactionManager)
				.reader(reader)
				.processor(processor())
				.writer(writer)
				.build();
	}

	@Bean
	public ListItemReader<User> reader() throws InterruptedException {
		//List<User> users = fetchUserLoginByStarredRepo("devsuperior", "sds-dsmovie");
		return new ListItemReader<User>(fetchUserData());
	}

	public ItemProcessor<User, User> processor() {
		return new ItemProcessor<User, User>() {

			@Override
			public User process(User item) throws Exception {
//				if (item.getLogin().equals("yyy"))
//					throw new Exception("Exception in " + item.getId());
				return item;
			}

		};
	}

	@Bean
	public ItemWriter<User> userWriter(@Qualifier("appDS") DataSource dataSource) {
		//return items -> items.forEach(System.out::println);		
		return new JdbcBatchItemWriterBuilder<User>()
		        .dataSource(dataSource)
		        .sql(
		            "INSERT INTO user (id, login, name, location, avatar_url, followers, following, created_at) VALUES (:id, :login, :name, :location, :avatarUrl, :followers, :following, :createdAt)")
		        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
		        .build();
	}
	
	private List<User> fetchUserData() throws InterruptedException {
		
		String uri = BASE_URL + "/clients";
		
		System.out.println("Sleeping ... " + secondsToSleep * 1000);
		Thread.sleep(secondsToSleep * 1000);
		
		System.out.println("Fetching data ...");
		ResponseEntity<List<User>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<User>>() {
				});
		
		List<User> result = response.getBody();
		return result;		
	}
	
	/*
	private List<User> fetchUserLoginByStarredRepo(String owner, String repo) {

		String uri = BASE_URL + "/repos/" + owner + "/" + repo + "/stargazers";
		request = new HttpEntity<String>(getHttpHeaders());

		ResponseEntity<List<User>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<User>>() {
				});

		List<User> result = response.getBody();
		// result.forEach(user -> System.out.println(user.toString()));
		return result;
	}

	private User fetchUserData(User user) {

		String uri = BASE_URL + "/users/" + user.getLogin();
		request = new HttpEntity<String>(getHttpHeaders());

		ResponseEntity<User> response = restTemplate.exchange(uri, HttpMethod.GET, null,
				new ParameterizedTypeReference<User>() {
				});

		user = response.getBody();
		return user;
	}
	

	public HttpHeaders getHttpHeaders() {
		if (headers == null) {
			headers = new HttpHeaders();
			headers.add("Authorization", "Bearer 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");
		}
		return headers;
	}
	*/

	@BeforeChunk
	private void beforeChunk(ChunkContext context) {
		System.out.println("before Chunk");
	}

	@AfterChunk
	private void afterChunk(ChunkContext context) {
		System.out.println("after Chunk");
	}
}
