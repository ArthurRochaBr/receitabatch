package processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import model.Conta;
import service.ReceitaService;

public class ContaItemProcessor implements ItemProcessor<Conta, Conta> {

	private static final Logger LOG = LoggerFactory.getLogger(ContaItemProcessor.class);
	
	@Override
	public Conta process(Conta item) throws Exception {
		ReceitaService receitaService = new ReceitaService();
		boolean resultado = receitaService.atualizarConta(item.getAgencia(), item.getConta().replace("-", ""), item.getSaldo(), item.getStatus());
		
		Conta conta = new Conta(item.getAgencia(), item.getConta(), item.getSaldo(), item.getStatus(), resultado ? "1" : "0");
		
		LOG.info("Validando ("+item+") para ("+conta+")");
		
		return conta;
	}

}
