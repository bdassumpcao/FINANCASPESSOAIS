package com.consultoriasolucao.appsolucaosistemas;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;


@SuppressLint("NewApi")
public class FiltroRelatFinanceiroFragment extends Fragment {

	
	private Button btdtini;
	private Button btdtfim;
	private Button btn_buscarRelat;
	private int ano, mes, dia;
	static boolean flagdataini;
	private DatabaseHelper db;
	private List<String> nomesCategoria = new ArrayList<String>();
	private List<String> nomesPagamento = new ArrayList<String>();
	private Spinner categoria;	
	private Spinner pagamento;
	private Spinner situacao;
	private Spinner ds_tipo;
	private String filtro;
	
	public FiltroRelatFinanceiroFragment() {}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.activity_filtrorelatfinanceiro, container, false);
        
		Calendar calendar = Calendar.getInstance();
		ano = calendar.get(Calendar.YEAR);
		mes = calendar.get(Calendar.MONTH);
		dia = calendar.get(Calendar.DAY_OF_MONTH);	
		
		btn_buscarRelat = (Button) rootView.findViewById(R.id.btn_buscarRelat);
        btdtini = (Button) rootView.findViewById(R.id.btdataini);
        btdtfim = (Button) rootView.findViewById(R.id.btdatafim);
		btdtini.setText("01/" + (mes+1) + "/" + ano);
		btdtfim.setText(dia + "/" + (mes+1) + "/" + ano);
		situacao = (Spinner) rootView.findViewById(R.id.ds_situacaosel);
		ds_tipo = (Spinner) rootView.findViewById(R.id.ds_tiporeceitadespesa);
		
		categoria = (Spinner) rootView.findViewById(R.id.categoriasel);
		pagamento = (Spinner) rootView.findViewById(R.id.pagamentosel);
		
		btn_buscarRelat.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				filtro =btdtini.getText().toString()+"|"+btdtfim.getText().toString()+"|"+categoria.getSelectedItem().toString()+"|"+pagamento.getSelectedItem().toString()+"|"+situacao.getSelectedItem().toString()+"|"+ds_tipo.getSelectedItem().toString()+"|";
				
				Intent intent = new Intent(getActivity(), RelatorioFinanceiroFragment.class);
				Log.i("financas", filtro);
				intent.putExtra(RelatorioFinanceiroFragment.EXTRA_NOME_USUARIO, filtro);
				getActivity().setIntent(intent);
				Fragment fragment = new RelatorioFinanceiroFragment();
				getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
				
			}
		});
		
		btdtini.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatePicker();
				flagdataini = true;
			}
		});
		
		btdtfim.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatePicker();
				flagdataini = false;
			}
		});
		
		carregarSpinnerCategoria();
		carregarSpinnerPagamento();
		carregarSpinnerTipo();
		carregarSpinnerSituacao();
		
		
        return rootView;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new DatabaseHelper(getActivity());
		
	
	

	}
	
	public void carregarSpinnerCategoria()
	{
		nomesCategoria.clear();
		SQLiteDatabase dbexe = db.getReadableDatabase();
		Cursor cursor = dbexe.rawQuery("SELECT _id, ds_categoria FROM categoria order by ds_categoria",	null);
		nomesCategoria.add("TODOS");
		while (cursor.moveToNext()) 
		{
		  nomesCategoria.add(cursor.getString(1));
		}
		
	    cursor.close();
		
		
		//Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, nomesCategoria);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		categoria.setAdapter(spinnerArrayAdapter);
	}
	
	public void carregarSpinnerPagamento()
	{
		nomesPagamento.clear();
		SQLiteDatabase dbexe2 = db.getReadableDatabase();
		Cursor cursor = dbexe2.rawQuery("SELECT _id, ds_pagamento FROM pagamento order by ds_pagamento",	null);
		nomesPagamento.add("TODOS");
		while (cursor.moveToNext()) 
		{
		  nomesPagamento.add(cursor.getString(1));
		}
		
	    cursor.close();
		
		
		//Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
		ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, nomesPagamento);
		ArrayAdapter<String> spinnerArrayAdapter2 = arrayAdapter2;
		spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
		pagamento.setAdapter(spinnerArrayAdapter2);
	}
	
	public void carregarSpinnerTipo(){
		String[] tipos = {"TODOS", "RECEITA", "DESPESA"};
		
		//Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, tipos);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		ds_tipo.setAdapter(spinnerArrayAdapter);
		
	}
	
	public void carregarSpinnerSituacao(){
		String[] situacoes = {"TODOS", "PAGOS", "A VENCER"};
		
		//Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, situacoes);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		situacao.setAdapter(spinnerArrayAdapter);
		
	}
	
		
	
	private void showDatePicker() {
	        DatePickerFragment date = new DatePickerFragment();
	        /**
	        * Set Up Current Date Into dialog
	        */
	        Calendar calender = Calendar.getInstance();
	        Bundle args = new Bundle();
	        args.putInt("year", calender.get(Calendar.YEAR));
	        args.putInt("month", calender.get(Calendar.MONTH));
	        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
	        date.setArguments(args);
	        /**
	        * Set Call back to capture selected date
	        */
	        date.setCallBack(ondate);
	        date.show(getFragmentManager(), "Date Picker");
	}
	
	OnDateSetListener ondate = new OnDateSetListener() {

	        public void onDateSet(DatePicker view, int year, int monthOfYear,
	            int dayOfMonth) {

	    		ano = year;
	    		mes = monthOfYear;
	    		dia = dayOfMonth;
	    		
	    		if (flagdataini) //para não ter que criar outro método 
	    		{
	    		btdtini.setText(dia + "/" + (mes+1) + "/" + ano);
	    		} else		btdtfim.setText(dia + "/" + (mes+1) + "/" + ano);
	        }
	    };
	   
//	private OnDateSetListener listener = new OnDateSetListener() {
//		@Override
//		public void onDateSet(DatePicker view,
//		int year, int monthOfYear, int dayOfMonth) {
//		ano = year;
//		mes = monthOfYear;
//		dia = dayOfMonth;
//		
//		if (flagdataini) //para não ter que criar outro método 
//		{
//		btdtini.setText(dia + "/" + (mes+1) + "/" + ano);
//		} else		btdtfim.setText(dia + "/" + (mes+1) + "/" + ano);
//		}
//		};
		
//	@Override
//	protected Dialog onCreateDialog(int id) {
//	if(R.id.btdataini == id){
//	return new DatePickerDialog(this, listener, ano, mes, dia);
//	}
//	if(R.id.btdatafim == id){
//		return new DatePickerDialog(this, listener, ano, mes, dia);
//		}
//	return null;
//	}
	

	public void buscarRelat(View view)
	{
		filtro =btdtini.getText().toString()+"|"+btdtfim.getText().toString()+"|"+categoria.getSelectedItem().toString()+"|"+pagamento.getSelectedItem().toString()+"|"+situacao.getSelectedItem().toString()+"|"+ds_tipo.getSelectedItem().toString()+"|";
		
		Intent intent = new Intent(getActivity(), RelatorioFinanceiro.class);
		intent.putExtra(RelatorioFinanceiro.EXTRA_NOME_USUARIO, filtro);
		getActivity().setIntent(intent);
//		startActivity(intent);
	}
	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.solucao_sistemas, menu);
//		return true;
//	}

}
