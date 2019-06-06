package br.imd.ufrn.lpii.modelo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import br.imd.ufrn.lpii.modelo.abstratics.Similaridade;

public class Cosine extends Similaridade {
	
	private static final Pattern SPACE_REG = Pattern.compile("\\s+");
	
	private static final int K = 5;
	
	public Cosine(BancoDeDados bd) {
		super(bd);
	}

//	@Override
//	public double distancia(String hash, String noticia) {
//		return(produtoEscalar(hash, noticia) / (Math.sqrt(produtoEscalar(hash, hash))*
//				Math.sqrt(produtoEscalar(noticia, noticia))))*100;
//	}
	
	private int produtoEscalar(String a, String b) {
		
		int max = 0, min = 0;
		
		if(a.length() != b.length() ) {
			return 0;
		}
		if(a.length() > b.length()) {
			max = a.length();
			min = b.length();
		}else {
			max = b.length();
			min = a.length();
		}
		
		int produto = 0;
		
		for(int i = 0; i < max; i ++) {
			if( i < a.length() && i < b.length()) {
				produto += (a.charAt(i)) 
						* (b.charAt(i));
			}else if(i < a.length() && i > b.length()) {
				produto -= a.charAt(i);
			}else if( i < b.length()) {
				produto -= b.charAt(i);
			}
		}
		
		return produto;	
	}
	
	
	//teste
    public final double distancia(final String s1, final String s2) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 1;
        }

        if (s1.length() < K || s2.length() < K) {
            return 0;
        }

        Map<String, Integer> profile1 = getProfile(s1);
        Map<String, Integer> profile2 = getProfile(s2);

        return (dotProduct(profile1, profile2)
                / (norm(profile1) * norm(profile2)))*100;
    }

    /**
     * Compute the norm L2 : sqrt(Sum_i( v_i²)).
     *
     * @param profile
     * @return L2 norm
     */
    private static double norm(final Map<String, Integer> profile) {
        double agg = 0;

        for (Map.Entry<String, Integer> entry : profile.entrySet()) {
            agg += 1.0 * entry.getValue() * entry.getValue();
        }

        return Math.sqrt(agg);
    }

    private static double dotProduct(
            final Map<String, Integer> profile1,
            final Map<String, Integer> profile2) {

        // Loop over the smallest map
        Map<String, Integer> small_profile = profile2;
        Map<String, Integer> large_profile = profile1;
        if (profile1.size() < profile2.size()) {
            small_profile = profile1;
            large_profile = profile2;
        }

        double agg = 0;
        for (Map.Entry<String, Integer> entry : small_profile.entrySet()) {
            Integer i = large_profile.get(entry.getKey());
            if (i == null) {
                continue;
            }
            agg += 1.0 * entry.getValue() * i;
        }

        return agg;
    }
    
    
    public final Map<String, Integer> getProfile(final String string) {
        HashMap<String, Integer> shingles = new HashMap<String, Integer>();

        String string_no_space = SPACE_REG.matcher(string).replaceAll(" ");
        for (int i = 0; i < (string_no_space.length() - K + 1); i++) {
            String shingle = string_no_space.substring(i, i + K);
            Integer old = shingles.get(shingle);
            if (old != null) {
                shingles.put(shingle, old + 1);
            } else {
                shingles.put(shingle, 1);
            }
        }

        return Collections.unmodifiableMap(shingles);
    }

}
