package br.imd.ufrn.lpii.app;

import br.imd.ufrn.lpii.dominio.BancoDeDados;

public abstract class Similaridade {
	
	private BancoDeDados bd;
	
	public Similaridade(BancoDeDados bd) { this.bd = bd; }
	
	public boolean verificarSimilaridade(String noticia) {
		
		if(bd.buscaBancoDeDados(noticia)) {
			return true;
		}else {
			//System.out.println("pasou aqui");
			for (String hash : bd.keySet()) {
				
				double porcentagem = distancia(bd.bdValor(hash).getMensagemProcessada(), noticia);
				
				if( porcentagem >= 85) {
					System.out.println("com "+porcentagem+"%");
					return true;
				}
				
			}
			
			return false;
		}
	}	
	
	public abstract double distancia(String hash, String noticia);
}
