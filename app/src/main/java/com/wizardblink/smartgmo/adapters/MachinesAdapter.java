package com.wizardblink.smartgmo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wizardblink.smartgmo.R;
import com.wizardblink.smartgmo.models.Machines;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MachinesAdapter extends BaseAdapter {

    private Context context;
    private List<Machines> list;
    private int layout;

    public MachinesAdapter(Context context, List<Machines> machines, int layout){
        this.context = context;
        this.list = machines;
        this.layout = layout;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Machines getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vh;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new ViewHolder();
            vh.tittle = (TextView) convertView.findViewById(R.id.textViewBoardTittle);
            vh.notes = (TextView) convertView.findViewById(R.id.textViewBoardNotes);
            vh.createdAt = (TextView) convertView.findViewById(R.id.textViewBoardDate);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }

        Machines machines = list.get(position);
        vh.tittle.setText(machines.getMachines());
        int numberOfOts = machines.getOts().size();
        String textForOts = (numberOfOts==1) ? numberOfOts + " OT" : numberOfOts + " OTs";
        vh.notes.setText(textForOts);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        //String createdAt = df.format(machines.getCreatedAt());
        //vh.createdAt.setText(createdAt);

        return convertView;
    }

    public class ViewHolder {
        TextView tittle;
        TextView notes;
        TextView createdAt;
    }
}
