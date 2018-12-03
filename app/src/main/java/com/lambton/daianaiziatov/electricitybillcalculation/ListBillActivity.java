package com.lambton.daianaiziatov.electricitybillcalculation;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ListBillActivity extends AppCompatActivity {

    private ListView listView;
    private CustomAdapter customAdapter;
    private AppDatabase db;
    private ArrayList<ElectricityBill> billList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bill);
        db = AppDatabase.getAppDatabase(this);
        billList = (ArrayList<ElectricityBill>) db.getBillDao().getAllBills();

        listView = findViewById(R.id.list_view);
        customAdapter = new CustomAdapter(billList, this);

        listView.setAdapter(customAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteBill(position);
                return false;
            }
        });

    }

    private void deleteBill(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete bill?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.getBillDao().delete(billList.get(position));
                customAdapter.remove(billList.get(position));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
