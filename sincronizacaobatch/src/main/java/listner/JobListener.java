package listner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import model.Conta;

@Component
public class JobListener extends JobExecutionListenerSupport {
	private static final Logger LOG = LoggerFactory.getLogger(JobListener.class);

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JobListener(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			LOG.info("FIM DO JOB DE CONTAS:");

			jdbcTemplate.query("SELECT AGENCIA, CONTA, SALDO, STATUS, RESULTADO FROM CONTA",
					(rs, row) -> new Conta(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5)))
			.forEach(conta -> LOG.info("Registro < " + conta + " >"));
			
		}
		
		
	}
}
