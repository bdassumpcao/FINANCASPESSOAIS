package com.consultoriasolucao.appsolucaosistemas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ConsultaPagamentoFragment extends Fragment implements OnItemClickListener{
	private ListView lista;
	private Button inserirPag;
	private DatabaseHelper db;
	private List<Map<String, String>> pagamentos;
	
	
	public ConsultaPagamentoFragment() {
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.activity_consultapagamento, container, false);	
        inserirPag = (Button) rootView.findViewById(R.id.inserirPag);
        lista = (ListView) rootView.findViewById(R.id.listapagamento);
        
		lista.setOnItemClickListener(this);
		registerForContextMenu(lista); 
		
		String de[] = { "cd_pagamento", "ds_pagamento"};
		int para[] = { R.id.cd_pagamento, R.id.ds_pagamento	};

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), buscarPagamento(),
				R.layout.listview_pagamento, de, para);

		lista.setAdapter(adapter);
		
        inserirPag.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MostrarFragment();
			}
		});
        
        return rootView;
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new DatabaseHelper(getActivity());
		
	}
	
	public void MostrarFragment(){
		Fragment fragment = new PagamentoFragment();
		getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
	}
	
	 @Override
	    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	        super.onCreateContextMenu(menu, v, menuInfo);
	        menu.add(Menu.NONE, R.id.remover_pagamento, Menu.NONE, "Remover Forma de Pagamento");
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
