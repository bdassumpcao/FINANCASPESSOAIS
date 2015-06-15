package com.consultoriasolucao.appsolucaosistemas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ConsultaFormaPagamento extends Activity implements OnItemClickListener{
	private ListView lista;

	private DatabaseHelper db;
	private List<Map<String, String>> pagamentos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consultapagamento);
		
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().hide();
		
	    lista = (ListView) findViewById(R.id.listapagamento);

		db = new DatabaseHelper(this);
		
		lista.setOnItemClickListener(this);
		registerForContextMenu(lista); 
		
		String de[] = { "cd_pagamento", "ds_pagamento"};
		int para[] = { R.id.cd_pagamento, R.id.ds_pagamento	};

		SimpleAdapter adapter = new SimpleAdapter(this, buscarPagamento(),
				R.layout.listview_pagamento, de, para);

		lista.setAdapter(adapter);
	
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	ContextMenuInfo menuInfo) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.menu_pagamento, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
	        case R.id.remover_pagamento:
	        	//Toast.makeText(this, item.toString(), Toast.LENGTH_LONG).show();
	        	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
	        	
	        	String cd_pagamento = pagamentos.get(info.position).get("cd_pagamento");
	        	pagamentos.remove(info.position);
	        	lista.invalidateViews();
	        	
	        	 db.getWritableDatabase().execSQL("delete from pagamento where _id ="+cd_pagamento);
	        	 
	            return true;
	        
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	private List<Map<String, String>> buscarPagamento() {
		// buscar todos os produtos do banco

		Cursor c = db
				.getReadableDatabase()
				.rawQuery(
						"select _id, ds_pagamento from pagamento order by ds_pagamento",
						null);
		pagamentos = new ArrayList<Map<String, String>>();
		
		while (c.moveToNext()) {
			Map<String, String> mapa = new HashMap<String, String>();

			mapa.put("cd_pagamento",  c.getString(0));
			mapa.put("ds_pagamento",c.getString(1));
			
			pagamentos.add(mapa);
		}
		
		c.close();
		
				
		
		return pagamentos;
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}


}
