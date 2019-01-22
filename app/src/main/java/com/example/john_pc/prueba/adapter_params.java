package com.example.john_pc.prueba;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class adapter_params extends ArrayAdapter<obj_params>{


    public adapter_params(Context context, ArrayList<obj_params> datos) {
        super(context,0, datos);



    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return intView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return intView(position, convertView, parent);
    }

    private View intView(int position, View convertView, ViewGroup parent){

        if(convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.personalization3, parent, false);
        }

        ImageView ivImg = convertView.findViewById(R.id.ivIcono);
        TextView tvText = convertView.findViewById(R.id.tvText);

        obj_params datos = getItem(position);

        if(datos != null) {

            ivImg.setImageResource(R.drawable.usuario);
            tvText.setText(datos.getDescription());

        }

        return  convertView;

    }

}

/*public class adapter_params extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<obj_params> items;

    public adapter_params (Activity activity, ArrayList<obj_params> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.personalization3, null);

        }

        obj_params item = items.get(position);

        TextView tvDescription = vi.findViewById(R.id.tvText);
        tvDescription.setText(item.getDescription());


        return vi;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.personalization3, null);

        }

        obj_params item = items.get(position);

        TextView tvDescription = vi.findViewById(R.id.tvText);
        tvDescription.setText(item.getDescription());

        return vi;

    }

}*/

/*public class adapter_params extends ArrayAdapter<obj_params> {
    private Context context;

    List<obj_params> datos = null;

    public adapter_params(Context context, List<obj_params> datos) {
        //se debe indicar el layout para el item que seleccionado (el que se muestra sobre el botón del botón)
        super(context, R.layout.personalization3, datos);
        this.context = context;
        this.datos = datos;
    }

    //este método establece el elemento seleccionado sobre el botón del spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.personalization3, null);
        }
        ((TextView) convertView.findViewById(R.id.texto)).setText(datos.get(position).getDescription());
        //((ImageView) convertView.findViewById(R.id.icono)).setBackgroundResource(datos.get(position).getIcono());

        return convertView;
    }

    //gestiona la lista usando el View Holder Pattern. Equivale a la típica implementación del getView
    //de un Adapter de un ListView ordinario
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.personalization3, parent, false);
        }

        if (row.getTag() == null) {
            SocialNetworkHolder redSocialHolder = new SocialNetworkHolder();
            //redSocialHolder.setIcono((ImageView) row.findViewById(R.id.icono));
            redSocialHolder.setTextView((TextView) row.findViewById(R.id.texto));
            row.setTag(redSocialHolder);
        }

        //rellenamos el layout con los datos de la fila que se está procesando
        obj_params redSocial = datos.get(position);
        //((SocialNetworkHolder) row.getTag()).getIcono().setImageResource(redSocial.getIcono());
        ((SocialNetworkHolder) row.getTag()).getTextView().setText(redSocial.getDescription());

        return row;
    }

    /**
     * Holder para el Adapter del Spinner
     *
     * @author danielme.com
     */
    /*private static class SocialNetworkHolder {

        private ImageView icono;

        private TextView textView;

        public ImageView getIcono() {
            return icono;
        }

        public void setIcono(ImageView icono) {
            this.icono = icono;
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

    }
}*/