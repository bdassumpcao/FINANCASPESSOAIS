package com.consultoriasolucao.appsolucaosistemas;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PagamentoFragment extends Fragment{
	private DatabaseHelper helper;
	
	EditText edtds_formaPagamento;
	
	public PagamentoFragment(){}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.activity_formadepagamento, container, false);
		this.edtds_formaPagamento = (EditText) rootView.findViewById(R.id.edtds_formapagamento);
	
//		getView().setOnKeyListener( new OnKeyListener()
//		{
//		    @Override
//		    public boolean onKey( View v, int keyCode, KeyEvent event )
//		    {
//		        if( keyCode == KeyEvent.KEYCODE_BACK )
//		        {
//		        	MostrarFragment();
//		            return true;
//		        }
//		        return false;
//		    }
//		} );
		
		
        return rootView;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// prepara acesso ao banco de dados
		helper = new DatabaseHelper(getActivity());		
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
		
		this.edtds_formaPagamento.setText("");
		MostrarFragment();
		
		}
		
	}
	
	public void MostrarFragment(){
		Fragment fragment = new ConsultaPagamentoFragment();
		getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
	}
	
//	  @Override
//	    public boolean onKeyDown(int keyCode, KeyEvent event) {
//	        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//				MostrarFragment();
//	            return true;
//	        }
//	        return super.onKeyDown(keyCode, event);
//	    }
	

	  
}
