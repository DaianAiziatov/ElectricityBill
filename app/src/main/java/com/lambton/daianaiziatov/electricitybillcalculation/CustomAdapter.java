package com.lambton.daianaiziatov.electricitybillcalculation;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<ElectricityBill> {

    private ArrayList<ElectricityBill> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView customerIdTextView;
        TextView customerNameTextView;
        TextView unitstextView;
        TextView totalTextView;
        TextView billDateTextView;
    }

    public CustomAdapter(ArrayList<ElectricityBill> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

//    @Override
//    public boolean onLongClick(View v) {
//        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        ElectricityBill electricityBill = (ElectricityBill) object;
//        deleteBill(electricityBill);
//        return true;
//    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ElectricityBill electricityBill = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.customerNameTextView = (TextView) convertView.findViewById(R.id.customer_name_in_row);
            viewHolder.customerIdTextView = (TextView) convertView.findViewById(R.id.customer_id_in_row);
            viewHolder.unitstextView = (TextView) convertView.findViewById(R.id.units_in_row);
            viewHolder.totalTextView = (TextView) convertView.findViewById(R.id.total_in_row);
            viewHolder.billDateTextView = (TextView) convertView.findViewById(R.id.bill_date_in_row);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy");
        viewHolder.customerNameTextView.setText(electricityBill.getCustomerName());
        viewHolder.customerIdTextView.setText(electricityBill.getCustomerId());
        viewHolder.unitstextView.setText(String.valueOf(electricityBill.getUnitConsumed()));
        viewHolder.totalTextView.setText(String.valueOf(electricityBill.getTotalBill()));
        viewHolder.billDateTextView.setText(dateTimeFormatter.format(electricityBill.getBillDate()));
        // Return the completed view to render on screen
        return convertView;
    }

}
