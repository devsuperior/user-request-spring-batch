package com.devsuperior.githubuser.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.devsuperior.githubuser.dto.UserDTO;
import com.devsuperior.githubuser.entity.User;

@Configuration
public class FetchUserDataAndStoreDBStepConfig {

	@Autowired
	@Qualifier("transactionManagerApp")
	private PlatformTransactionManager transactionManager;

	@Bean
	public Step fetchUserDataAndStoreDBStep(ItemReader<UserDTO> fetchUserDataReader,
			ItemProcessor<UserDTO, User> selectFieldsUserDataProcessor, ItemWriter<User> insertUserDataDBWriter,
			JobRepository jobRepository) {
		return new StepBuilder("step", jobRepository)
				.<UserDTO, User>chunk(10, transactionManager)
				.reader(fetchUserDataReader)
				.processor(selectFieldsUserDataProcessor)
				.writer(insertUserDataDBWriter)
				.build();
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
