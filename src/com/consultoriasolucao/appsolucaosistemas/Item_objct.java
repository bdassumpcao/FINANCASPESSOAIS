package com.consultoriasolucao.appsolucaosistemas;

public class Item_objct {
	private String titulo;
	private int icone;
	public Item_objct(String title, int icon) {
		  this.titulo = title;
	      this.icone = icon;		    
	}	
    public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public int getIcone() {
		return icone;
	}
	public void setIcone(int icone) {
		this.icone = icone;
	}   
}
