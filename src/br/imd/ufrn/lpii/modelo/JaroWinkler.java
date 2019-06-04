package br.imd.ufrn.lpii.modelo;

import java.util.Arrays;

import br.imd.ufrn.lpii.modelo.abstratics.Similaridade;

public class JaroWinkler extends Similaridade {

	private static final double DEFAULT_SCALING_FACTOR = 0.1;

	public JaroWinkler(BancoDeDados bd) {
		super(bd);
	}

	@Override
	public double distancia(String hash, String noticia) {

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
		
		return jw * 100;
	}
	
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
