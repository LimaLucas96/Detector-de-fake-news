package br.imd.ufrn.lpii.model;

import java.util.ArrayList;

import br.imd.ufrn.lpii.model.abstratics.Similaridade;

public class Trigram extends Similaridade {
	
	
	public Trigram(BancoDeDados bd) {
		super(bd);
	}
	
	@Override
	public double distancia(String hash, String noticia)
    {
        //Array List para guardar as Strings tri	
        ArrayList<String> palavratriA = new ArrayList<>(); 	
        ArrayList<String> palavratriB = new ArrayList<>(); 	
      //  ArrayList<String> matches = new ArrayList<>(); 	
        
        //Percorre A guardando a substring do indice i até i+3 no array list
    	for (int i = 0; i < hash.length(); i++) 
    	{
    	    if(i + 3 <= hash.length())
			palavratriA.add(hash.substring(i, i+3));
    	    
    	    else
    	    	continue;
		}
    	
        //Percorre B guardando a substring do indice j até j+3 no array list
    	for (int i = 0; i < noticia.length(); i++) 
    	{
    	    if(i + 3 <= noticia.length())
			palavratriB.add(noticia.substring(i, i+3));
    	    
    	    else
    	    	continue;
		}
    	
    	//Acertos
    	double cont = 0;
    	
    	for (int i = 0; i < palavratriA.size(); i++) 
    	{
    		for (int j = 0; j < palavratriB.size(); j++) 
    		{
				if(palavratriA.get(i).equals(palavratriB.get(j)))
				{
					cont++;
					break;
				}
				else 
					continue;
			}
			
		}
    	
    
    	double unique = (palavratriA.size() - cont) + (palavratriB.size() - cont) + cont;
    	
    	
    	
    	return 100 * (cont / unique);
    }
}
