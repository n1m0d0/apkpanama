package com.example.john_pc.prueba;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter_events extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<obj_events> items;

    public adapter_events(Activity activity, ArrayList<obj_events> items) {
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
            vi = inflater.inflate(R.layout.personalization, null);

        }

        obj_events item = items.get(position);

        TextView id = vi.findViewById(R.id.tvIdEvent);
        id.setText(""+item.getId());

        TextView values = vi.findViewById(R.id.tvKeyValue);
        values.setText(item.getVariable());

        TextView startDate = vi.findViewById(R.id.tvDateEventBegin);
        startDate.setText(item.getFecha_inicio());

        TextView endDate = vi.findViewById(R.id.tvDateEventEnd);
        endDate.setText(item.getFecha_fin());

        /*if(item.getId_from() == 1) {



        } else {

            if(item.getId_from() == 2) {



            } else {

                if(item.getId_from() == 3) {



                } else {



                }

            }

        }

        TextView tv0003 = vi.findViewById(R.id.tv0003);
        tv0003.setText(item.getNumero_persona());

        TextView tv0004 = vi.findViewById(R.id.tv0004);
        tv0004.setText(item.getNumero_container());*/

        return vi;
    }
}
