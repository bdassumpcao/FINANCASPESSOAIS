package com.consultoriasolucao.appsolucaosistemas;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CategoriaFragment extends Fragment {
	private DatabaseHelper helper;
	
	EditText edtds_categoria;
	Button inserir;
	
	public CategoriaFragment(){}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.activity_categoria, container, false);
        this.edtds_categoria = (EditText) rootView.findViewById(R.id.edtds_categoria);
        this.inserir = (Button) rootView.findViewById(R.id.inserirCat);
        this.inserir.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				inserirCategoria();
			}
		});
          
        return rootView;
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		
		// prepara acesso ao banco de dados
		helper = new DatabaseHelper(getActivity());	
		
		
		
	}
	
	public void inserirCategoria()
	{
		Boolean flagvalida = true;

		if (edtds_categoria.getText().toString().equals("")) {
			edtds_categoria.setError("Entre com a descrição");
			edtds_categoria.requestFocus();
			flagvalida = false;
		}
		
		if (flagvalida)
		{
			
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("ds_categoria", this.edtds_categoria.getText().toString());
		long resultado = db.insert("categoria", null, values);

		
		this.edtds_categoria.setText("");
		MostrarFragment();
		}
		
	}

	public void MostrarFragment(){
		Fragment fragment = new ConsultaCagetoriaFragment();
		getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
	}

}
