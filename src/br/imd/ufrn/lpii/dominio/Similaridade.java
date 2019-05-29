package br.imd.ufrn.lpii.dominio;

import java.util.Arrays;

public class Similaridade {
	private BancoDeDados bd;
	
	private static final double DEFAULT_SCALING_FACTOR = 0.1;
	
	
	public Similaridade(BancoDeDados bd) {
		this.bd = bd;
	}
	
	public boolean verificarSimilaridade(String noticia) {
	
		if(bd.buscaBancoDeDados(noticia)) {
			return true;
		}else {
			//System.out.println("pasou aqui");
			for (String hash : bd.keySet()) {
				//double porcentagem = algoritmoDeLevensthein(hash, noticia);
				double porcentagem = algoritmoDeLevensthein(bd.bdValor(hash).getMensagemProcessada(), noticia);
				//double porcentagem = algoritmoDeCosine(bd.bdValor(hash).getMensagemProcessada(), noticia);
				if( porcentagem >= 85) {
					System.out.println("com "+porcentagem+"%");
					return true;
				}
				
			}
			
			return false;
		}
	}
	
	public double algoritmoDeCosine(String hash, String noticia) {	
		
		return (produtoEscalar(hash, noticia) / (Math.sqrt(produtoEscalar(hash, hash))*
				Math.sqrt(produtoEscalar(noticia, noticia))))*100;	
	}
	
	private double algoritmoDeLevensthein(String hash, String noticia) {
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
	
	private void algoritmoDeTrigam(String hash, String noticia) {
      
	}

	private double algoritmoDeJaroWinkler(String hash, String noticia) {
		
		int[] mTemp = matches( hash , noticia );
		double m = mTemp[0];
	
		if(m == 0) {
			return 0;
		}
		
		double j = ((m/hash.length() + m/noticia.length() + (m - mTemp[1])/m))/3;
		
		double jw = j;
		
		if(j > 0.7) {
			jw = j + Math.min(DEFAULT_SCALING_FACTOR, 1 / mTemp[3]) * mTemp[2] * (1 - j);
		}
		
		return jw;
	}
	//Uso somente na Cosine
	private int produtoEscalar(String a, String b) {
		
		int produto = 0;
		
		for(int i = 0; i < b.length(); i ++) {
			//produto += Integer.parseInt(String.valueOf(a.charAt(i)),16) * Integer.parseInt(String.valueOf (b.charAt(i)) ,16);
			produto += a.charAt(i) * b.charAt(i);
		}
		System.out.println("O produto escalar é -------> "+produto);
		return produto;
		
	}
	//uso somente na Levens
	private int min(int a, int b, int c) { return Math.min(Math.min(a, b), c); }
	
	//Uso somente na Jaro-Winkler
	private int[] matches(String hash, String noticia) {
		String max, min;
		
		if(hash.length() > noticia.length()) {
			max = hash;
			min = noticia;
		}else {
			max = noticia;
			min = hash;
		}
		
		int range = Math.max(max.length() / (2 - 1), 0);
		int[] match_indexes = new int[min.length()];
		
		Arrays.fill(match_indexes, -1);
		boolean[] match_flags = new boolean[max.length()];
		
		int matches = 0;
		
		for(int i = 0; i < min.length(); i++) {
			char c1 =  min.charAt(i);
			
			for( int xj = Math.max(i - range, 0),
					xn = Math.min(i + range + 1, max.length());
					xj < xn;
					xj++) {
				if(!match_flags[xj] && c1 == max.charAt(xj)) {
					match_indexes[i] = xj;
					match_flags[xj] = true;
					matches++;
					break;
				}
			}
		}
		
		char[] ms1 = new char[matches];
		char[] ms2 = new char[matches];
		
		for(int i = 0, si = 0; i < min.length(); i++) {
			if(match_indexes[i] != -1) {
				ms1[si] = min.charAt(i);
				si++;
			}
		}
		for(int i = 0, si = 0; i < max.length(); i++) {
			if(match_flags[i]) {
				ms2[si] = max.charAt(i);
				si++;
			}
		}
		
		int transpositions = 0;
		
		for(int i = 0; i < ms1.length; i ++) {
			if(ms1[i] != ms2[i]) {
				transpositions ++;
			}
		}
		
		int prefix = 0;
		
		for(int i = 0; i < min.length(); i++) {
			if(hash.charAt(i) == noticia.charAt(i)) {
				prefix++;
			}else {
				break;
			}
		}
		
		return new int[] {matches, transpositions / 2, prefix, max.length()};
	}
}
