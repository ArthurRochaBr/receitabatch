package br.com.arthur.sincronizacaobatch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import listner.JobListener;
import model.Conta;
import processor.ContaItemProcessor;



@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public FlatFileItemReader<Conta> reader(){
		return new FlatFileItemReaderBuilder<Conta>()
			   .name("contaItemReader")
			   .resource(new ClassPathResource("arquivo.csv"))
			   .delimited()
			   .names(new String[] {"agencia", "conta", "saldo", "status", "resultado"})
			   .fieldSetMapper(new BeanWrapperFieldSetMapper<Conta>() {{
				   setTargetType(Conta.class);
			   }})
			   .build();
	}
	
	@Bean
	public ContaItemProcessor processor() {
		return new ContaItemProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<Conta> writer(DataSource dataSource){
		return new JdbcBatchItemWriterBuilder<Conta>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO CONTA (agencia, conta, saldo, status, resultado) VALUES (:agencia, :conta, :saldo, :status, :resultado)")
				.dataSource(dataSource)
				.build();
	}
	
	@Bean
	public Job importContaJob(JobListener listener, Step step1) {
		return jobBuilderFactory.get("importContaJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end()
				.build();
	}
	
	@Bean
	public Step step1(JdbcBatchItemWriter<Conta> writer) {
		return stepBuilderFactory.get("step1")
				.<Conta, Conta> chunk(10)
				.reader(reader())
				.writer(writer)
				.build();
	}

}
