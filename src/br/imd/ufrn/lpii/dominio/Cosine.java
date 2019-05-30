package br.imd.ufrn.lpii.dominio;

import br.imd.ufrn.lpii.app.Similaridade;

public class Cosine extends Similaridade {

	public Cosine(BancoDeDados bd) {
		super(bd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double distancia(String hash, String noticia) {
		return (produtoEscalar(hash, noticia) / (Math.sqrt(produtoEscalar(hash, hash))*
				Math.sqrt(produtoEscalar(noticia, noticia))))*100;
	}
	
	private int produtoEscalar(String a, String b) {
		
		int produto = 0;
		
		for(int i = 0; i < b.length(); i ++) {
			//produto += Integer.parseInt(String.valueOf(a.charAt(i)),16) * Integer.parseInt(String.valueOf (b.charAt(i)) ,16);
			produto += a.charAt(i) * b.charAt(i);
		}
		System.out.println("O produto escalar é -------> "+produto);
		return produto;	
	}
}
