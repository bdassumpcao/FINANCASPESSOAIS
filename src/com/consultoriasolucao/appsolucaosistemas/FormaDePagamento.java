package com.consultoriasolucao.appsolucaosistemas;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FormaDePagamento extends Activity{
	private DatabaseHelper helper;
	
	EditText edtds_formaPagamento;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formadepagamento);
		
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().hide();
		
		this.edtds_formaPagamento = (EditText)findViewById(R.id.edtds_formapagamento);
		// prepara acesso ao banco de dados
		helper = new DatabaseHelper(this);		
	}
	
	public void inserirFormaDePagamento(View view)
	{
		Boolean flagvalida = true;

		if (edtds_formaPagamento.getText().toString().equals("")) {
			edtds_formaPagamento.setError("Entre com a descrição");
			edtds_formaPagamento.requestFocus();
			flagvalida = false;
		}
		
		if (flagvalida)
		{
			
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("ds_pagamento", this.edtds_formaPagamento.getText().toString());
		long resultado = db.insert("pagamento", null, values);
		Toast.makeText(this, "Registro salvo com sucesso!",
				Toast.LENGTH_SHORT).show();
		
		this.edtds_formaPagamento.setText("");
		}
		
	}
}
