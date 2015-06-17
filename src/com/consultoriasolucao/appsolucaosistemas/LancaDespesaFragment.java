package com.consultoriasolucao.appsolucaosistemas;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class LancaDespesaFragment extends Fragment {

	public static final String EXTRA_CD_LANCAMENTO = "AppSolucaoSistemas.EXTRA_CD_LANCAMENTO";
	
	private EditText edtvalor;
	private EditText edthistorico;
	private DatabaseHelper db;
	private Uri outputFileUri;
	private int ano, mes, dia;
	private Button dataGasto;
	private Button dataVencimento;
	private Button btn_cadCategoria;
	private Button btn_cadPagamento;
	private Button btn_inserirDespesa;
	private Spinner categoria;
	private Spinner pagamento;
	private List<String> nomesCategoria = new ArrayList<String>();
	private List<String> nomesPagamento = new ArrayList<String>();
	private RadioGroup radioGroup;
	private RadioGroup rgreceitadespesa;
	private String datasel;
	private boolean flagvalida;
	private Date dt_lancamento, dt_vencimento;
    private String cd_lancamento;
    private SimpleDateFormat dateFormat;
    private RadioButton rgsituacaopago;
    private RadioButton rgsituacaoapagar;
    private RadioButton rgreceita;
    private RadioButton rgdespesa;
    private TableLayout tl_situacao;
    private TableLayout tl_vencimento;
    private String straux;
    private int cat_position;
    private int pag_position;
    ArrayAdapter<String> arrayAdapterCategoria;
    ArrayAdapter<String> arrayAdapterPagamento;

    LancaDespesaFragment(){};
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.activity_lancardespesa, container, false);	

		this.edtvalor = (EditText) rootView.findViewById(R.id.edtvalor);
		this.edthistorico = (EditText) rootView.findViewById(R.id.edthistorico);
		this.radioGroup = (RadioGroup) rootView.findViewById(R.id.rgsituacao);
		this.rgreceitadespesa = (RadioGroup) rootView.findViewById(R.id.rgreceitadespesa);
		this.tl_situacao = (TableLayout) rootView.findViewById(R.id.tl_situacao);
		this.tl_vencimento = (TableLayout) rootView.findViewById(R.id.tl_vencimento);
		this.rgreceita = (RadioButton) rootView.findViewById(R.id.rgreceita);
		this.rgdespesa = (RadioButton) rootView.findViewById(R.id.rgdespesa);
		this.rgsituacaopago = (RadioButton) rootView.findViewById(R.id.rgsituacaopgo);
		this.rgsituacaoapagar = (RadioButton) rootView.findViewById(R.id.rgsituacaoavencer);
		this.categoria = (Spinner) rootView.findViewById(R.id.categoria);
		this.pagamento = (Spinner) rootView.findViewById(R.id.pagamento);
		this.btn_cadCategoria = (Button) rootView.findViewById(R.id.btn_cadCategoria);
		this.btn_cadPagamento = (Button) rootView.findViewById(R.id.btn_cadPagamento);
		this.btn_inserirDespesa = (Button) rootView.findViewById(R.id.btn_inserirDespesa);
		
		Calendar calendar = Calendar.getInstance();
		ano = calendar.get(Calendar.YEAR);
		mes = calendar.get(Calendar.MONTH);
		dia = calendar.get(Calendar.DAY_OF_MONTH);
		dataGasto = (Button) rootView.findViewById(R.id.btdata);
		dataGasto.setText(dia + "/" + (mes + 1) + "/" + ano);
		dataVencimento = (Button) rootView.findViewById(R.id.btdtvencimento);
		dataVencimento.setText(dia + "/" + (mes + 1) + "/" + ano);
		
		
		
        dataGasto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatePicker();
				datasel = "DataLancamento";
			}
		});
        
        dataVencimento.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatePicker();
				datasel = "DataVencimento";
			}
		});
        
        btn_cadCategoria.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment fragment = new CategoriaFragment();
				getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
			}
		});
        
        btn_cadPagamento.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment fragment = new PagamentoFragment();
				getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
			}
		});
        
        btn_inserirDespesa.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InserirDespesa();
			}
		});
        
		carregaSpinnerCategoria();
		carregaSpinnerPagamento();
		
		db = new DatabaseHelper(getActivity());

		SQLiteDatabase dbexe = db.getReadableDatabase();
		

		
		cd_lancamento=""; //codigo do lancamento e a flag para verificar se é edição ou inserção
		Intent intent = getActivity().getIntent();
		if (intent.hasExtra(EXTRA_CD_LANCAMENTO)) {//caso seja edição então carregando os campos

			cd_lancamento = intent.getStringExtra(EXTRA_CD_LANCAMENTO);
			Cursor cursor = dbexe.rawQuery(
					"SELECT a._id, a.ds_historico, coalesce(a.vl_despesa,0), coalesce(a.vl_receita,0), a.dt_lancamento,a.dt_vencimento, a.cd_categoria, a.cd_pagamento, a.ds_situacao,  a.ds_tipo,b.ds_categoria, c.ds_pagamento from financas a join categoria b on (a.cd_categoria=b._id) "
					+ "join pagamento c on (a.cd_pagamento=c._id) where a._id="+cd_lancamento, null);
			while (cursor.moveToNext()) {
				edthistorico.setText(cursor.getString(1));				
				Double val =Double.valueOf(cursor.getString(2).toString()).doubleValue()+Double.valueOf(cursor.getString(3).toString()).doubleValue();
				edtvalor.setText(val+"");
				long dataChegada = cursor.getLong(4);
				Date dataChegadaDate = new Date(dataChegada);
				dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String periodo = dateFormat.format(dataChegadaDate);
				dataGasto.setText(periodo);
				
				dataChegada = cursor.getLong(5);
				periodo = dateFormat.format(dataChegadaDate);
				dataVencimento.setText(periodo);
				

				if (cursor.getString(8).equals("P")) //caso a situação seja pago
				{
					rgsituacaopago.setChecked(true);	
					rgsituacaoapagar.setChecked(false);
				} else 
				{
					rgsituacaopago.setChecked(false);	
					rgsituacaoapagar.setChecked(true);	
					tl_vencimento.setVisibility(View.VISIBLE);
				}
				
				
				if (cursor.getString(9).equals("D")) //caso o tipo seja despesa
				{
					rgreceita.setChecked(false);	
					rgdespesa.setChecked(true);
					rgsituacaopago.setText("Pago");
					rgsituacaoapagar.setText("A Pagar");
					rgsituacaoapagar.setBackgroundResource(R.drawable.rbtn_selector2);
					rgsituacaoapagar.setTextColor(R.drawable.rbtn_textcolor_selector2);
					rgsituacaopago.setBackgroundResource(R.drawable.rbtn_selector2);
					rgsituacaopago.setTextColor(R.drawable.rbtn_textcolor_selector2);
				} else 
				{
					rgreceita.setChecked(true);	
					rgdespesa.setChecked(false);
					rgsituacaopago.setText("Recebido");
					rgsituacaoapagar.setText("A Receber");
					rgsituacaoapagar.setBackgroundResource(R.drawable.rbtn_selector);
					rgsituacaoapagar.setTextColor(R.drawable.rbtn_textcolor_selector);
					rgsituacaopago.setBackgroundResource(R.drawable.rbtn_selector);
					rgsituacaopago.setTextColor(R.drawable.rbtn_textcolor_selector);
				}
				
				//verificando qual item do sppiner Categoria foi selecinado	
				straux = cursor.getString(10);			
				final int spinnerPosition = arrayAdapterCategoria.getPosition(straux);
				cat_position = spinnerPosition;
				new Handler().postDelayed(new Runnable() {        
				    public void run() {
				    	categoria.setSelection(spinnerPosition, true);
				    }
				  }, 100);

				
				//verificando qual item do sppiner Formas de Pagamento foi selecinado	
				straux = cursor.getString(11);					
				final int spinnerPosition2 = arrayAdapterPagamento.getPosition(straux);
				pag_position = spinnerPosition2;
				new Handler().postDelayed(new Runnable() {        
				    public void run() {
				    	pagamento.setSelection(spinnerPosition2, true);
				    }
				  }, 100);
				 
			}
			cursor.close();
		}
        return rootView;
    }
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		

	}
	
	public void selecionarTipo(View view){
		if(rgdespesa.isChecked()){
			rgsituacaopago.setText("Pago");
			rgsituacaoapagar.setText("A Pagar");
			rgsituacaoapagar.setBackgroundResource(R.drawable.rbtn_selector2);
			rgsituacaoapagar.setTextColor(R.drawable.rbtn_textcolor_selector2);
			rgsituacaopago.setBackgroundResource(R.drawable.rbtn_selector2);
			rgsituacaopago.setTextColor(R.drawable.rbtn_textcolor_selector2);
//			tl_situacao.setVisibility(View.VISIBLE);
		}
		else if(rgreceita.isChecked()){
			rgsituacaopago.setText("Recebido");
			rgsituacaoapagar.setText("A Receber");
			rgsituacaoapagar.setBackgroundResource(R.drawable.rbtn_selector);
			rgsituacaoapagar.setTextColor(R.drawable.rbtn_textcolor_selector);
			rgsituacaopago.setBackgroundResource(R.drawable.rbtn_selector);
			rgsituacaopago.setTextColor(R.drawable.rbtn_textcolor_selector);
//			tl_situacao.setVisibility(View.VISIBLE);
		}
	}
	
	public void selecionarSituacao(View view){
		if(rgsituacaoapagar.isChecked()){			
			tl_vencimento.setVisibility(View.VISIBLE);			
		}
		else if(rgsituacaopago.isChecked()){
			tl_vencimento.setVisibility(View.INVISIBLE);
		}
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
	
	private OnDateSetListener ondate = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			ano = year;
			mes = monthOfYear;
			dia = dayOfMonth;

			if (datasel == "DataLancamento") {
				dataGasto.setText(dia + "/" + (mes + 1) + "/" + ano);
			} else
				dataVencimento.setText(dia + "/" + (mes + 1) + "/" + ano);
		}
	};


	public void InserirDespesa() {
		flagvalida = true;

		
		if (edthistorico.getText().toString().equals("") ) {
			edthistorico.setError("Entre com a Descrição!");
			edthistorico.requestFocus();
			flagvalida = false;

		}
		
		else if (edtvalor.getText().toString().equals("")) {
			edtvalor.setError("Entre com o Valor!");
			edtvalor.requestFocus();
			flagvalida = false;

		}
		
		else if (categoria.getSelectedItem().toString().equals("SELECIONE")){
			categoria.requestFocus();
			Toast.makeText(getActivity(), "Entre com a Categoria!",
					Toast.LENGTH_LONG).show();
			flagvalida = false;
		}
		
		else if (pagamento.getSelectedItem().toString().equals("SELECIONE")){
			pagamento.requestFocus();
			Toast.makeText(getActivity(), "Entre com a Forma de Pagamento!",
					Toast.LENGTH_LONG).show();
			flagvalida = false;
		}
		
		else if ((!rgdespesa.isChecked()) & (!rgreceita.isChecked())){
			rgreceita.setError("Entre com o Tipo do Lançamento!");
			rgreceita.requestFocus();
			flagvalida = false;
		}
		
		
		else if ((!rgsituacaoapagar.isChecked()) & (!rgsituacaopago.isChecked())){
			rgsituacaoapagar.setError("Entre com a Situação do Título!");
			rgsituacaoapagar.requestFocus();
			flagvalida = false;
		}

		if (flagvalida) {
			
			SQLiteDatabase banco = db.getWritableDatabase();
			
			if (cd_lancamento != ""){ //caso seja edição então apagando o registro e inserindo outro
				
				banco.execSQL("delete from financas where _id="+cd_lancamento);	
			}
			
			ContentValues values = new ContentValues();
			values.put("ds_historico", edthistorico.getText().toString());	
			
			dt_lancamento = ConvertToDate(dataGasto.getText().toString());
			values.put("dt_lancamento", dt_lancamento.getTime());
			dt_vencimento = ConvertToDate(dataVencimento.getText().toString()); 
			values.put("dt_vencimento", dt_vencimento.getTime());

			int tipo = rgreceitadespesa.getCheckedRadioButtonId();
			if (tipo == R.id.rgdespesa) 
			{
				values.put("ds_tipo", "D");
				values.put("vl_despesa",Double.valueOf(edtvalor.getText().toString()).doubleValue());
				values.put("vl_receita", 0);
			} else 
			{  
				values.put("ds_tipo", "R");
				values.put("vl_receita",Double.valueOf(edtvalor.getText().toString()).doubleValue());
				values.put("vl_despesa", 0);
			}
			
			tipo = radioGroup.getCheckedRadioButtonId();
			if (tipo == R.id.rgsituacaopgo) {
				values.put("ds_situacao", "P");
			} else
				values.put("ds_situacao", "A");
			
			
			String cd_cat = categoria.getSelectedItem().toString();
			db = new DatabaseHelper(getActivity());
			SQLiteDatabase d = db.getReadableDatabase();			
			Cursor cursor1 = d.rawQuery(
					"SELECT _id FROM categoria where ds_categoria=\""+ cd_cat +"\" order by ds_categoria", null);
			cursor1.moveToNext();
			cd_cat = cursor1.getString(0);
			cursor1.close();
			values.put("cd_categoria", cd_cat);
			
			
			String cd_pag = pagamento.getSelectedItem().toString();
			db = new DatabaseHelper(getActivity());			
			SQLiteDatabase d1 = db.getReadableDatabase();			
			Cursor cursor2 = d1.rawQuery(
					"SELECT _id FROM pagamento where ds_pagamento=\""+ cd_pag +"\" order by ds_pagamento", null);
			cursor2.moveToNext();
			cd_pag = cursor2.getString(0);
			cursor2.close();
			values.put("cd_pagamento", cd_pag);
			

			banco.insert("financas", null, values);
			edthistorico.setText("");
			edtvalor.setText("");
			
			Toast.makeText(getActivity(), "Lançamento salvo com sucesso!",
					Toast.LENGTH_LONG).show();
			
			Fragment fragment = new FiltroRelatFinanceiroFragment();
			getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
			
		}
	}
	
	private Date ConvertToDate(String dateString)
	{
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date convertedDate = new Date();
	    try {
	        convertedDate = dateFormat.parse(dateString);
	    } catch (ParseException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    return convertedDate;
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		
//	}


	public void carregaSpinnerCategoria(){
		nomesCategoria.clear();
		Cursor c = db.getReadableDatabase().rawQuery(
				"SELECT _id, ds_categoria FROM categoria order by ds_categoria", null);
		nomesCategoria.add("SELECIONE");
		while (c.moveToNext()) {
			nomesCategoria.add(c.getString(1));			
		}

		c.close();
		// Cria um ArrayAdapter usando um padrão de layout da classe R do
		// android, passando o ArrayList nomes
		arrayAdapterCategoria = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, nomesCategoria);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapterCategoria;
		spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		
		categoria.setAdapter(spinnerArrayAdapter);
	}
	
	public void carregaSpinnerPagamento(){
		nomesPagamento.clear();
		Cursor c1 = db.getReadableDatabase().rawQuery(
				"SELECT _id, ds_pagamento FROM pagamento order by ds_pagamento", null);
		nomesPagamento.add("SELECIONE");
		while (c1.moveToNext()) {
			nomesPagamento.add(c1.getString(1));			
		}

		c1.close();
		// Cria um ArrayAdapter usando um padrão de layout da classe R do
		// android, passando o ArrayList nomes
		arrayAdapterPagamento = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, nomesPagamento);
		ArrayAdapter<String> spinnerArrayAdapter2 = arrayAdapterPagamento;
		spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
		
		pagamento.setAdapter(spinnerArrayAdapter2);
	}
		
	public void tiraFoto(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory(),
				"test.jpg");
		outputFileUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, 9000);

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.solucao_sistemas, menu);
//		return true;
//	}
	
	@Override
	public void onResume(){
		super.onResume();
		carregaSpinnerCategoria();
		new Handler().postDelayed(new Runnable() {        
		    public void run() {
		    	categoria.setSelection(cat_position, true);
		    }
		  }, 100);
		
		carregaSpinnerPagamento();
		new Handler().postDelayed(new Runnable() {        
		    public void run() {
		    	pagamento.setSelection(pag_position, true);
		    }
		  }, 100);
	}

}