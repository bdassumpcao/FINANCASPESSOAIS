package com.consultoriasolucao.appsolucaosistemas;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationAdapter extends BaseAdapter {
    private Activity activity;  
	ArrayList<Item_objct> arrayitms; 

   public NavigationAdapter(Activity activity,ArrayList<Item_objct>  listarray) {  
       super();  
       this.activity = activity;  
       this.arrayitms=listarray;
       }     
   //Retorna objeto Item_objct do arraylist
   @Override
   public Object getItem(int position) {       
       return arrayitms.get(position);
   }   
    public int getCount() {  
      // TODO Auto-generated method stub  
        return arrayitms.size();  
    }    
    @Override
    public long getItemId(int position) {
        return position;
    }   
    //Declaramos classe est�tica que representa a Fila
    public static class Fila  
    {  
    		TextView titulo_itm;
    		ImageView icone;
    }  
   public View getView(int position, View convertView, ViewGroup parent) {  
      // TODO Auto-generated method stub  
	   Fila view;  
       LayoutInflater inflator = activity.getLayoutInflater();  
      if(convertView==null)  
       {  
           view = new Fila();
           //Creo objeto item y lo obtengo del array
           Item_objct itm=arrayitms.get(position);
           convertView = inflator.inflate(R.layout.itm, null);
           //Titulo
           view.titulo_itm = (TextView) convertView.findViewById(R.id.title_item);
           //Seteo en el campo titulo el nombre correspondiente obtenido del objeto
           view.titulo_itm.setText(itm.getTitulo());           
           //icone
           view.icone = (ImageView) convertView.findViewById(R.id.icon);
           //Seta o icone
           view.icone.setImageResource(itm.getIcone());           
           convertView.setTag(view);  
        }  
        else  
        {  
           view = (Fila) convertView.getTag();  
        }  
        return convertView;  
    }
}
