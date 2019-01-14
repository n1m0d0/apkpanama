package com.example.john_pc.prueba;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter_forms extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<obj_form> items;

    public adapter_forms(Activity activity, ArrayList<obj_form> items) {
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
            vi = inflater.inflate(R.layout.personalization2, null);

        }


        obj_form item = items.get(position);

        TextView tvDescription = vi.findViewById(R.id.tvDescription);
        tvDescription.setText(item.getDescriptionForm());

        ImageView ivImage = vi.findViewById(R.id.ivImage);
        ivImage.setImageResource(R.drawable.correo);

        /*obj_events item = items.get(position);

        TextView id = vi.findViewById(R.id.tvIdEvent);
        id.setText(""+item.getId());*/


        return vi;

    }
}
