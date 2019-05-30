package br.imd.ufrn.lpii.dominio;

import br.imd.ufrn.lpii.app.Similaridade;

public class Levensthein extends Similaridade {

	public Levensthein(BancoDeDados bd) {
		super(bd);
		// TODO Auto-generated constructor stub
	}

	public double distancia(String hash, String noticia) {
		
		int tam = 0;
		if(hash.length() > noticia.length() ) {
			tam = hash.length();
		}else {
			tam = noticia.length();
		}
		
		int v0[] = new int [tam + 1];
		int v1[] = new int [tam + 1];
		
		int temp[];
		
		for(int i = 0; i < tam; i ++) {
			v0[i]=i;
		}
		for(int i = 0; i < tam - 1; i ++) {
			v1[i] = i+1;
			
			int minv1=v1[0];
			
			for(int j = 0; j < tam - 1; j++) {
				int cost = 1;
				if((i < hash.length()) && (j < noticia.length()) && ((hash.charAt(i) == noticia.charAt(j)))) {
					cost = 0;
				}
				
				v1[j+1] = min(v1[j]+1,
						v0[j+1]+1,
						v0[j]+cost);
				
				minv1 = Math.min(minv1, v1[j+1]);
			}
			 temp = v0;
			 v0=v1;
			 v1 = temp;
		}
		
		return 100 - ((v0[tam - 1]*100)/tam-1);
		
	}

	private int min(int a, int b, int c) { return Math.min(Math.min(a, b), c); }
	
}
