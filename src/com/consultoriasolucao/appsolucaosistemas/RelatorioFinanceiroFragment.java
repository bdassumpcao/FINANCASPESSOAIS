package com.consultoriasolucao.appsolucaosistemas;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.xml.sax.DTDHandler;

import com.consultoriasolucao.appsolucaosistemas.R.array;
import com.consultoriasolucao.appsolucaosistemas.R.string;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ListActivity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.SimpleAdapter.ViewBinder;

public class RelatorioFinanceiroFragment extends Fragment implements
		OnItemClickListener {

	public static final String EXTRA_NOME_USUARIO = "AppSolucaoSistemas.EXTRA_NOME_USUARIO";
	private ListView lista;
	private DatabaseHelper db;
	private List<Map<String, String>> produtos;
	private TextView txtttdespesa;
	private TextView txtttreceita;
	private TextView txtsaldo;	
	private Date dt_ini, dt_fim, dt_aux;
	private String strsql;
	private String filtro;
	private String dtini;
	private String dtfim;
	private String categoria1;
	private String pagamento;
	private String situacao;
	private String ds_tipo;
	private Button btn_novaDespesa;
	private Button btn_filtro;

	
	RelatorioFinanceiroFragment(){};
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	  
	        View rootView = inflater.inflate(R.layout.activity_relatoriofinanceiro, container, false);
	        btn_filtro = (Button) rootView.findViewById(R.id.btn_filtro);
	        btn_novaDespesa = (Button) rootView.findViewById(R.id.btn_novaDespesa);
			lista = (ListView) rootView.findViewById(R.id.listarelatfin);
			txtttdespesa = (TextView) rootView.findViewById(R.id.txtttdespesa);
			txtttreceita = (TextView) rootView.findViewById(R.id.txtttreceita);
			txtsaldo = (TextView) rootView.findViewById(R.id.txtsaldo);

			lista.setOnItemClickListener(this);
			registerForContextMenu(lista);
		
			
			Intent intent = getActivity().getIntent();
			if(intent!=null){
				if (intent.hasExtra(EXTRA_NOME_USUARIO)) {
					filtro = intent.getStringExtra(EXTRA_NOME_USUARIO);
					buscarrelat();
				}
			}
			
	        btn_novaDespesa.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Fragment fragment = new LancaDespesaFragment();
					getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
				}
			});
	        
	        btn_filtro.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Fragment fragment = new FiltroRelatFinanceiroFragment();
					getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
					
				}
			});
			
	        return rootView;
	    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		
		db = new DatabaseHelper(getActivity());



	}


	
	public void buscarrelat() {
		StringTokenizer st = new StringTokenizer(filtro, "|");
		while (st.hasMoreTokens()) {
			dtini = st.nextToken();
			dtfim = st.nextToken();
			categoria1 = st.nextToken();
			pagamento = st.nextToken();
			situacao = st.nextToken();
			ds_tipo = st.nextToken();
		}
		String de[] = {  "dt_lancamento", "cd_lancamento","vl_lancamento",
				"ds_historico", "ds_categoria", "ds_pagamento","dt_vencimento","ds_situacao" };
		int para[] = { R.id.dt_lancamento, R.id.cd_lancamentolist,
				R.id.vl_lancamento, R.id.ds_historico, R.id.ds_categoria , R.id.ds_pagamento,R.id.dt_vencimento, R.id.ds_situacao};

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), buscarRelat(dtini,
				dtfim, categoria1, pagamento, situacao, ds_tipo),
				R.layout.listview_relatfinanceiro, de, para);

		lista.setAdapter(adapter);
	}

//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.menu_financeiro, menu);
//	}
	
	 @Override
	    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	        super.onCreateContextMenu(menu, v, menuInfo);
	        menu.add(Menu.NONE, R.id.remover_despesarec, Menu.NONE, "Excluir Lancamento");
	        menu.add(Menu.NONE, R.id.editar_lancamento, Menu.NONE, "Editar Lançamento");
	        menu.add(Menu.NONE, R.id.realiza_pagamento, Menu.NONE, "Realizar Pagamento");
	    }

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// Toast.makeText(this, item.toString(), Toast.LENGTH_LONG).show();
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		String cd_lancamento;

		switch (item.getItemId()) {
		case R.id.remover_despesarec:
			cd_lancamento = produtos.get(info.position).get("cd_lancamento");
			cd_lancamento = cd_lancamento.substring(0,
					cd_lancamento.indexOf("-"));
			produtos.remove(info.position);
			lista.invalidateViews();

			db.getWritableDatabase().execSQL(
					"delete from financas where _id =" + cd_lancamento);
			buscarrelat();
			
			return true;
			
		case R.id.realiza_pagamento:
			cd_lancamento = produtos.get(info.position).get("cd_lancamento");
			cd_lancamento = cd_lancamento.substring(0,
					cd_lancamento.indexOf("-"));
			produtos.remove(info.position);
			lista.invalidateViews();

			db.getWritableDatabase().execSQL(
					"update financas  set ds_situacao ='P' where _id ="
							+ cd_lancamento);
			buscarrelat();
			
			return true;		
			
		case R.id.editar_lancamento:
			cd_lancamento = produtos.get(info.position).get("cd_lancamento");
			cd_lancamento = cd_lancamento.substring(0,cd_lancamento.indexOf("-"));
			Intent intent = new Intent(getActivity(), LancaDespesa.class);			
			intent.putExtra(LancaDespesa.EXTRA_CD_LANCAMENTO, cd_lancamento);
			getActivity().setIntent(intent);			
			
//			Intent intent = new Intent(getActivity(), LancaDespesa.class);
//			cd_lancamento = produtos.get(info.position).get("cd_lancamento");
//			cd_lancamento = cd_lancamento.substring(0,cd_lancamento.indexOf("-"));
//			intent.putExtra(LancaDespesa.EXTRA_CD_LANCAMENTO, cd_lancamento);
//			startActivity(intent);		
			
			return true;			
			
		default:
			return super.onContextItemSelected(item);
		}
	}

	private Date ConvertToDate(String dateString) {
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

	private List<Map<String, String>> buscarRelat(String dataini,
			String datafim, String categoria, String pagamento, String situacao, String ds_tipo) {
		// buscar todos os produtos do banco

		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
		dataini = f.format(ConvertToDate(dataini));
		datafim = f.format(ConvertToDate(datafim));

		dt_ini = ConvertToDate(dataini);
		dt_fim = ConvertToDate(datafim);
		strsql = "select a._id,  a.dt_lancamento, a.vl_despesa,a.vl_receita , a.ds_historico, coalesce(b.ds_categoria,''), coalesce(c.ds_pagamento,''),a.dt_vencimento, "
				+ "a.ds_situacao  from financas a left join categoria b on (a.cd_categoria=b._id) join pagamento c on (a.cd_pagamento=c._id) ";

		if (situacao.substring(0, 1).equals("A")) // caso o usuario queira os
													// titulos em aberto puxa
													// pela data de vencimento
		{
			strsql = strsql + "where a.dt_vencimento between '"
					+ dt_ini.getTime() + "'  and '" + dt_fim.getTime() + "'  ";
		} else
			strsql = strsql + "where a.dt_lancamento between '"
					+ dt_ini.getTime() + "' and  '" + dt_fim.getTime() + "'  ";

		if (!categoria.equals("TODOS")) {
			String posicao = "";
			Cursor cursor1 = db.getReadableDatabase().rawQuery(
					"SELECT _id FROM categoria where ds_categoria=\""+ categoria +"\" order by ds_categoria", null);
			cursor1.moveToNext();
			posicao = cursor1.getString(0);
			cursor1.close();
			
			strsql = strsql + " AND a.cd_categoria ="+ posicao;
//			Log.i("financas", strsql);
		}
		
		if (!pagamento.equals("TODOS")) {
			String posicao = "";
			Cursor cursor2 = db.getReadableDatabase().rawQuery(
					"SELECT _id FROM pagamento where ds_pagamento=\""+ pagamento +"\" order by ds_pagamento", null);
			cursor2.moveToNext();
			posicao = cursor2.getString(0);
			cursor2.close();
			
			strsql = strsql + " AND a.cd_pagamento ="+ posicao;
//			Log.i("financas", strsql);
		}

		if (!situacao.equals("TODOS")) {

			strsql = strsql + " AND coalesce(a.ds_situacao,'P')='"
					+ situacao.substring(0, 1) + "' ";
		}

		if (!ds_tipo.equals("TODOS")) {

			strsql = strsql + " AND a.ds_tipo='" + ds_tipo.substring(0, 1)
					+ "' ";
		}

		strsql = strsql + "  order by a.dt_lancamento";

		Cursor c = db.getReadableDatabase().rawQuery(strsql, null);
		produtos = new ArrayList<Map<String, String>>();
		DecimalFormat df = new DecimalFormat(",##0.00");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

		while (c.moveToNext()) {
			Map<String, String> mapa = new HashMap<String, String>();			
			
			strsql =c.getString(0);
			mapa.put("cd_lancamento", c.getString(0) + "-");
			
			dt_aux = new Date(c.getLong(1));
			strsql = dateFormat.format(dt_aux);
			mapa.put("dt_lancamento","Data: "+ dateFormat.format(dt_aux));
			

			if (c.getDouble(2) > 0) {
				mapa.put("vl_lancamento", "-" + df.format(c.getDouble(2))
						+ "  ");
			} else
				mapa.put("vl_lancamento",  df.format(c.getDouble(3))
						+ "  ");

			mapa.put("ds_historico", c.getString(4).toString());
			mapa.put("ds_categoria", c.getString(5).toString());
			mapa.put("ds_pagamento", c.getString(6).toString());
			dt_aux = new Date(c.getLong(7));
			if (c.getLong(1) != c.getLong(7)) //caso o vencimento seja diferente da data de lançamento quer dizer que é um lançamento a vencer
			{
			  mapa.put("dt_vencimento","Venc.:  "+ dateFormat.format(dt_aux));
			} else mapa.put("dt_vencimento","");
			
			if (c.getString(8).toString().equals("A"))
			{
				mapa.put("ds_situacao","A Vencer");
			} else mapa.put("ds_situacao","Pago");
			
			produtos.add(mapa);
		}

		c.close();

		strsql = "select sum(vl_despesa) as vl_despesa, sum(vl_receita) as vl_receita    from financas ";

		if (situacao.substring(0, 1).equals("A")) // caso o usuario queira os
													// titulos em aberto puxa
													// pela data de vencimento
		{
			strsql = strsql + "where dt_vencimento between '"
					+ dt_ini.getTime() + "'  and '" + dt_fim.getTime() + "'  ";
		} else
			strsql = strsql + "where dt_lancamento between '"
					+ dt_ini.getTime() + "'  and '" + dt_fim.getTime() + "'  ";

		if (!categoria.equals("TODOS")) {
			String posicao = "";
			Cursor cursor1 = db.getReadableDatabase().rawQuery(
					"SELECT _id FROM categoria where ds_categoria=\""+ categoria +"\" order by ds_categoria", null);
			cursor1.moveToNext();
			posicao = cursor1.getString(0);
			cursor1.close();

			strsql = strsql + " AND cd_categoria ="+ posicao;
		}
		
		if (!pagamento.equals("TODOS")) {
			String posicao = "";
			Cursor cursor2 = db.getReadableDatabase().rawQuery(
					"SELECT _id FROM pagamento where ds_pagamento=\""+ pagamento +"\" order by ds_pagamento", null);
			cursor2.moveToNext();
			posicao = cursor2.getString(0);
			cursor2.close();

			strsql = strsql + " AND cd_pagamento ="+ posicao;
		}

		if (!situacao.equals("TODOS")) {

			strsql = strsql + " AND ds_situacao ='" + situacao.substring(0, 1)
					+ "' ";
		}

		if (!ds_tipo.equals("TODOS")) {

			strsql = strsql + " AND ds_tipo ='" + ds_tipo.substring(0, 1)
					+ "' ";
		}

		Cursor ct = db.getReadableDatabase().rawQuery(strsql, null);
		ct.moveToFirst();
		

		txtttdespesa.setText(df.format(ct.getDouble(0)));		
		txtttreceita.setText(df.format(ct.getDouble(1)));		
		txtsaldo.setText(df.format(ct.getDouble(1) - ct.getDouble(0)));
		if((ct.getDouble(1) - ct.getDouble(0)) >= 0.00){
			txtsaldo.setTextColor(Color.BLUE);
		}
		else
			txtsaldo.setTextColor(Color.RED);
		
		ct.close();
		// construir objeto de retorno que é uma String[]
		return produtos;
	}

	
//	public void lancaDespesa(View view)
//	{
////		this.finish();
//		startActivity(new Intent(this, LancaDespesa.class));
//	}
//	
//	
//	public void relatorioFinanceiro(View view)
//	{
//		this.finish();
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onResume(){
	    super.onResume();
	    buscarrelat();
	}
}
