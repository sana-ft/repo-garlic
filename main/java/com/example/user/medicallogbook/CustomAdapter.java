package com.example.user.medicallogbook;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/23/2015.
 */
public class CustomAdapter extends BaseAdapter implements Filterable {
    Context context;
    List<LogItems> items;
    List<LogItems> itemsFiltered;
    private ItemFilter mFilter = new ItemFilter();

    CustomAdapter(Context context, List<LogItems> items) {
        this.context = context;
        this.items = items;
        this.itemsFiltered=items;
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
        return items.indexOf(getItem(position));
    }



    private class ViewHolder {
        ImageView procedure;
        TextView procedure_Name;
        TextView patient;
        TextView dateP;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter, null);

            holder.procedure_Name= (TextView) convertView
                    .findViewById(R.id.proc_name);
            holder.procedure = (ImageView) convertView
                    .findViewById(R.id.icon);
            holder.patient= (TextView) convertView.findViewById(R.id.patient_name);
            holder.dateP = (TextView) convertView
                    .findViewById(R.id.date_proc);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        LogItems row_pos = items.get(position);

        holder.procedure.setImageResource(R.drawable.doctor2);
        holder.procedure_Name.setText(row_pos.getPatient_name());
        holder.patient.setText(row_pos.getProc_date());
        holder.dateP.setText(row_pos.getProcedur_name());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(mFilter==null) {
            mFilter = new ItemFilter();
        }
        return mFilter;
    }

    //this class is used to filter the list view, in order to search a procedure by its name
    private class ItemFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if(constraint!=null && constraint.length()>0){
                constraint=constraint.toString().toUpperCase();
                ArrayList<LogItems> filters = new ArrayList<LogItems>();

                for(int i=0;i<itemsFiltered.size();i++)
                {
                    if(itemsFiltered.get(i).getPatient_name().toUpperCase().contains(constraint))
                    {
                        LogItems logitem=new LogItems(itemsFiltered.get(i).getPatient_name(),itemsFiltered.get(i).getProc_date(),itemsFiltered.get(i).getProcedur_name());
                        filters.add(logitem);
                    }
                }
                results.count=filters.size();
                results.values=filters;
            }else{
                results.count=itemsFiltered.size();
                results.values=itemsFiltered;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items= (ArrayList<LogItems>) results.values;
            notifyDataSetChanged();
        }
    }

}
