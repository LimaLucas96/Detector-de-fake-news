package br.imd.ufrn.lpii.modelo;

import java.util.HashMap;
import java.util.Set;

public class BancoDeDados {
	private HashMap<String, Registro> registros;
	
	public BancoDeDados() {
		registros = new HashMap<String, Registro>();
	}
	
	public void Adicionar(String hash, Registro resg) {
		registros.put(hash, resg);
	}
	public void print() {
		for (String key : registros.keySet()) {
			System.out.println("#############################");
			System.out.println("Chave = "+key);
		}
	}
	
	public Set<String> keySet() {
		return registros.keySet();
	}
	public Registro bdValor(String key) {
		return registros.get(key);
	}
	public boolean buscaBancoDeDados(String hash) { return registros.containsKey(hash); }
	
	public int size() { return registros.size(); }
}
