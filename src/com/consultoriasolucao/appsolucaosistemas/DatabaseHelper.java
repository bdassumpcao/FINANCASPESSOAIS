package com.consultoriasolucao.appsolucaosistemas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String BANCO_DADOS = "FINANCASSOLUCAO";
	private static int VERSAO = 18;
	
	public DatabaseHelper(Context context) 
	{
	  super(context, BANCO_DADOS, null, VERSAO);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{		
		db.execSQL("CREATE TABLE licenca (_id INTEGER PRIMARY KEY, dslicenca TEXT, ds_usuario TEXT);");
		db.execSQL("CREATE TABLE financas (_id INTEGER PRIMARY KEY, ds_historico TEXT, vl_despesa DOUBLE, "
				+ "vl_receita DOUBLE, ds_tipo TEXT, dt_lancamento DATE, cd_categoria INTEGER, "
				+ "cd_pagamento INTEGER,ds_situacao TEXT, dt_vencimento DATE); ");
		db.execSQL("CREATE TABLE categoria (_id INTEGER PRIMARY KEY, ds_categoria TEXT);");		
		db.execSQL("CREATE TABLE pagamento (_id INTEGER PRIMARY KEY, ds_pagamento TEXT);");		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		
	    
		 if (oldVersion <17)
		{
			
		}
	    
		
	}
	
}
