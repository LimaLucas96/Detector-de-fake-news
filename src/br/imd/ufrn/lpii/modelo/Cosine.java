package br.imd.ufrn.lpii.modelo;

import br.imd.ufrn.lpii.app.Similaridade;

public class Cosine extends Similaridade {

	public Cosine(BancoDeDados bd) {
		super(bd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double distancia(String hash, String noticia) {
		return (produtoEscalar(hash, noticia) / (Math.sqrt(produtoEscalar(hash, hash))*
				Math.sqrt(produtoEscalar(noticia, noticia)))*100);
	}
	
	private int produtoEscalar(String a, String b) {
		
		int max = 0;
		
		if(a.length() > b.length()) {
			max = a.length();
		}else {
			max = b.length();
		}
		
		int produto = 0;
		
		for(int i = 0; i < max; i ++) {
			//produto += Integer.parseInt(String.valueOf(a.charAt(i)),16) * Integer.parseInt(String.valueOf (b.charAt(i)) ,16);
			if(i < a.length() && i < b.length() ) {
				produto += a.charAt(i) * b.charAt(i);
			}
			
			
		}
		
		return produto;	
	}
}
