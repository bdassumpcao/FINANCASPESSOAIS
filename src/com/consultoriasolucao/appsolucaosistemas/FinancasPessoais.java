package com.consultoriasolucao.appsolucaosistemas;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


@SuppressLint("NewApi")
public class FinancasPessoais extends Activity {
	public Intent intent = null;
	private DatabaseHelper helper;
    private String[] titulos;
    private DrawerLayout NavDrawerLayout;
    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private TypedArray NavIcons;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    NavigationAdapter NavAdapter;
    public static final String EXTRA_NOME_USUARIO = "AppSolucaoSistemas.EXTRA_NOME_USUARIO";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	

		
			
			Calendar calendar = Calendar.getInstance();
			int ano = calendar.get(Calendar.YEAR);
			int mes = calendar.get(Calendar.MONTH);
			int dia = calendar.get(Calendar.DAY_OF_MONTH);	
			String dtini = "01/" + (mes+1) + "/" + ano;
			String dtfim = dia + "/" + (mes+1) + "/" + ano;
			String filtro = dtini+"|"+dtfim+"|TODOS|TODOS|TODOS|TODOS|";
			Log.i("financas", "filtro inicial: "+filtro);
			Intent intent = new Intent(this, RelatorioFinanceiroFragment.class);
			intent.putExtra(RelatorioFinanceiroFragment.EXTRA_NOME_USUARIO, filtro);
			setIntent(intent);			
		
		//Drawer Layout
		NavDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		//Lista
        NavList = (ListView) findViewById(R.id.lista);
        //Declaramos el header el cual sera el layout de header.xml
        View header = getLayoutInflater().inflate(R.layout.header, null);
        //Establecemos header
        NavList.addHeaderView(header);
		//Tomamos listado  de imgs desde drawable
        NavIcons = getResources().obtainTypedArray(R.array.navigation_icones);			
		//Tomamos listado  de titulos desde el string-array de los recursos @string/nav_options
        titulos = getResources().getStringArray(R.array.nav_options);
        //Listado de titulos de barra de navegacion
        NavItms = new ArrayList<Item_objct>();
        //Agregamos objetos Item_objct al array
        //Perfil	      
        NavItms.add(new Item_objct(titulos[0], NavIcons.getResourceId(0, -1)));
        //Favoritos
        NavItms.add(new Item_objct(titulos[1], NavIcons.getResourceId(1, -1)));
        //Eventos
        NavItms.add(new Item_objct(titulos[2], NavIcons.getResourceId(2, -1)));
        //Lugares
        NavItms.add(new Item_objct(titulos[3], NavIcons.getResourceId(3, -1)));
        //Configuracion
        NavItms.add(new Item_objct(titulos[4], NavIcons.getResourceId(5, -1)));
        //Share
        NavItms.add(new Item_objct(titulos[5], NavIcons.getResourceId(6, -1)));
      
        //Declaramos y seteamos nuestro adaptador al cual le pasamos el array con los titulos	       
        NavAdapter= new NavigationAdapter(this,NavItms);
        NavList.setAdapter(NavAdapter);	
        //Siempre vamos a mostrar el mismo titulo
        mTitle = mDrawerTitle = getTitle();
        
        //Declaramos el mDrawerToggle y las imgs a utilizar
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                NavDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* Icono de navegacion*/
                R.string.app_name,  /* "open drawer" description */
                R.string.hello_world  /* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
            	Log.e("Cerrado completo", "!!");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                Log.e("Apertura completa", "!!");
            }
        };	        
        
        // Establecemos que mDrawerToggle declarado anteriormente sea el DrawerListener
        NavDrawerLayout.setDrawerListener(mDrawerToggle);
        //Establecemos que el ActionBar muestre el Boton Home
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //Establecemos la accion al clickear sobre cualquier item del menu.
        //De la misma forma que hariamos en una app comun con un listview.
        NavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
            	MostrarFragment(position);
            }
        });
        
        //Cuando la aplicacion cargue por defecto mostrar la opcion Home
        MostrarFragment(1);
        
        
		
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
		db.close();
		
		db = helper.getReadableDatabase();
		Cursor cursor1 = db.rawQuery("SELECT _id, ds_pagamento FROM pagamento",	null);
		
		if (cursor1.getCount() == 0) //caso ainda nao tenha nehuma categoria cadastrada então cadastra 
		{
			SQLiteDatabase dbexec = helper.getWritableDatabase();
			dbexec.execSQL("INSERT INTO pagamento (ds_pagamento) values ('A VISTA'); ");
			dbexec.execSQL("INSERT INTO pagamento (ds_pagamento) values ('BOLETO'); ");
			dbexec.execSQL("INSERT INTO pagamento (ds_pagamento) values ('CHEQUE'); ");
			dbexec.execSQL("INSERT INTO pagamento (ds_pagamento) values ('CARTAO'); ");
			dbexec.execSQL("INSERT INTO pagamento (ds_pagamento) values ('DEPOSITO BANCARIO'); ");
			dbexec.execSQL("INSERT INTO pagamento (ds_pagamento) values ('OUTROS'); ");
			dbexec.close();			
		}
		cursor1.close();
		db.close();
	}
	
//	public void listCategoria(View view)
//	{
//		startActivity(new Intent(this, ConsultaCagetoria.class));
//	}
//
//	public void cadCategoria(View view)
//	{
//		startActivity(new Intent(this, Categoria.class));
//	}
//	
//	public void relatorioFinanceiro(View view)
//	{
//		startActivity(new Intent(this,FiltroRelatFinanceiro.class));
//	}
//	
//	public void QuemSomos(View view){
//		startActivity(new Intent(this, ConsultaFormaPagamento.class));
//	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.solucao_sistemas, menu);
//		return true;
//	}

	/*Pasando la posicion de la opcion en el menu nos mostrara el Fragment correspondiente*/
    private void MostrarFragment(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
        case 1:
            fragment = new RelatorioFinanceiroFragment();
            break;
        case 2:
        	fragment = new ConsultaCagetoriaFragment();
        	break;  
        case 3:
        	fragment = new ConsultaPagamentoFragment();
        	break;
        case 4:
        	fragment = new FiltroRelatFinanceiroFragment();
        	break;
        	
        default:
        	//Mostra mensagem e retorna para position 1
        	Toast.makeText(getApplicationContext(),"Opção "+titulos[position-1]+" não disponivel!", Toast.LENGTH_SHORT).show();
            fragment = new RelatorioFinanceiroFragment();
            position=1;
            break;
        }
        //Valida se o fragment é nulo
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
 
            // Actualiza o conteudo segundo a opção escolhida
            NavList.setItemChecked(position, true);
            NavList.setSelection(position);
            
            //Muda o titulo
            setTitle(titulos[position-1]);
            //Fecha o menu deslizante
            NavDrawerLayout.closeDrawer(NavList);
        } else {
            //Se o fragment é nulo mostra uma mensagem de erro.
            Log.e("Error  ", "MostrarFragment"+position);
        }
    }
	  
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.e("mDrawerToggle pushed", "x");
          return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	
}
