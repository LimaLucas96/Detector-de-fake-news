package br.imd.ufrn.lpii.modelo.abstratics;

import br.imd.ufrn.lpii.modelo.BancoDeDados;

public abstract class Similaridade {
	
	private BancoDeDados bd;


	public Similaridade(BancoDeDados bd) { 
		this.bd = bd; 
		
	}
	
	public double verificarSimilaridade(String noticia) {
		double maxPorcentagem = 0;
		for (String hash : bd.keySet()) {
				
			double porcentagem = distancia(bd.bdValor(hash).getMensagemProcessada(), noticia);
				
			if(porcentagem > maxPorcentagem) {
				maxPorcentagem = porcentagem;
			}
		}
		return maxPorcentagem;
	}	
	
	public abstract double distancia(String hash, String noticia);
}
