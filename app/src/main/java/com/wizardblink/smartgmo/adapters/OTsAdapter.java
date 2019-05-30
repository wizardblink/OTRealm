package com.wizardblink.smartgmo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wizardblink.smartgmo.R;
import com.wizardblink.smartgmo.models.OTs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OTsAdapter extends BaseAdapter {

    private Context context;
    private List<OTs> list;
    private int layout;

    public OTsAdapter(Context context, List<OTs> ots, int layout){
        this.context = context;
        this.list = ots;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public OTs getItem(int position) {
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
            vh.descripcion = (TextView) convertView.findViewById(R.id.textViewOtDescripciónNew);
            vh.usuario = (TextView) convertView.findViewById(R.id.textViewOtUsuarioNew);
            vh.prioridad = (TextView) convertView.findViewById(R.id.textViewOtPrioridadNew);
            vh.state = (TextView) convertView.findViewById(R.id.textViewOtStateNew);
            vh.tipo = (TextView) convertView.findViewById(R.id.textViewOtTipoNew);
            vh.createdAt = (TextView) convertView.findViewById(R.id.textViewOtDateNew);
            vh.equipo = (TextView) convertView.findViewById(R.id.textViewOtMachineNew);
            vh.generatedBy = (TextView) convertView.findViewById(R.id.textViewOtGeneratedByNew);
            vh.dateFinal= (TextView) convertView.findViewById(R.id.textViewOtDateFinalNew);
            vh.dateAsign = (TextView) convertView.findViewById(R.id.textViewOtDateAsignNew);

            convertView.setTag(vh);

        }else {

            vh = (ViewHolder) convertView.getTag();
        }

        OTs oTs= list.get(position);
        vh.descripcion.setText(oTs.getDescription());
        vh.prioridad.setText(oTs.getPriority());
        vh.state.setText(oTs.getState());
        vh.usuario.setText(oTs.getUsuario());
        vh.tipo.setText(oTs.getType());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String createdAt = df.format(oTs.getCreatedAt());
        vh.createdAt.setText(createdAt);
        vh.equipo.setText(oTs.getEquipo());
        vh.generatedBy.setText(oTs.getGeneratedBy());
        if(oTs.getFinishedAt()!=null) {
            DateFormat dff = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            String dateFinal = dff.format(oTs.getFinishedAt());
            vh.dateFinal.setText(dateFinal);
        }else{
            vh.dateFinal.setText("Sin fecha de finalización");
        }
        if(oTs.getRealInit()!=null){
            DateFormat dfff = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            String fechaAsignación = dfff.format(oTs.getRealInit());
            vh.dateAsign.setText(fechaAsignación);
        }else {
            vh.dateAsign.setText("Sin fecha de asignación");
        }
        return convertView;
    }

    public class ViewHolder {

        TextView descripcion;
        TextView usuario;
        TextView prioridad;
        TextView state;
        TextView tipo;
        TextView createdAt;
        TextView equipo;
        TextView generatedBy;
        TextView dateFinal;
        TextView dateAsign;

    }
}
