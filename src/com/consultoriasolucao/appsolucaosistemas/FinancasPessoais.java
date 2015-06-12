package com.consultoriasolucao.appsolucaosistemas;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


public class FinancasPessoais extends Activity {

	private DatabaseHelper helper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_financaspessoais);

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().hide();
		
		//procedimento para verificar se as categorias padrões foram lançadas
		helper = new DatabaseHelper(this);	
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT _id, ds_categoria FROM categoria",	null);
		
		if (cursor.getCount() == 0) //caso ainda nao tenha nehuma categoria cadastrada então cadastra 
		{
			SQLiteDatabase dbexec = helper.getWritableDatabase();
			dbexec.execSQL("INSERT INTO categoria (ds_categoria) values ('BELEZA ESTÉTICA'); ");
			dbexec.execSQL("INSERT INTO categoria (ds_categoria) values ('COMBUSTÍVEL TRANSPORTE'); ");
			dbexec.execSQL("INSERT INTO categoria (ds_categoria) values ('EDUCAÇÃO'); ");
			dbexec.execSQL("INSERT INTO categoria (ds_categoria) values ('GERAL'); ");
			dbexec.execSQL("INSERT INTO categoria (ds_categoria) values ('LAZER VIAJENS ENTRETENIMENTO'); ");
			dbexec.execSQL("INSERT INTO categoria (ds_categoria) values ('SAÚDE'); ");
			dbexec.execSQL("INSERT INTO categoria (ds_categoria) values ('SUPERMERCADO'); ");
			dbexec.execSQL("INSERT INTO categoria (ds_categoria) values ('VESTUÁRIO'); ");
			dbexec.execSQL("INSERT INTO categoria (ds_categoria) values ('ALIMENTAÇÃO'); ");
			dbexec.execSQL("INSERT INTO categoria (ds_categoria) values ('DESPESAS DOMÉSTICAS'); ");
			dbexec.execSQL("INSERT INTO categoria (ds_categoria) values ('DESPESAS VEÍCULOS'); ");
			dbexec.execSQL("INSERT INTO categoria (ds_categoria) values ('IMPOSTOS'); ");
			dbexec.close();
			
		}
		cursor.close();
		
		
		
		
	}
	
	public void listCategoria(View view)
	{
		startActivity(new Intent(this, ConsultaCagetoria.class));
	}

	public void cadCategoria(View view)
	{
		startActivity(new Intent(this, Categoria.class));
	}
	
	public void relatorioFinanceiro(View view)
	{
		startActivity(new Intent(this,FiltroRelatFinanceiro.class));
	}
	
	public void QuemSomos(View view){
		startActivity(new Intent(this, QuemSomos.class));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.solucao_sistemas, menu);
		return true;
	}

}
