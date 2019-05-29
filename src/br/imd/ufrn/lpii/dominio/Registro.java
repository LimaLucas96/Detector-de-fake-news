package br.imd.ufrn.lpii.dominio;

public abstract class Registro {
	
	protected String id;
	protected String conteudo;
	protected String link;
	protected String timestamp;
	

	public String getId() {
		return id;
	}
	public void setId(String id) { 
		this.id = id;
	}
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public abstract void setMensagemProcessada(String mensagemProcessada);
	
	public abstract String getMensagemProcessada();
	
}
