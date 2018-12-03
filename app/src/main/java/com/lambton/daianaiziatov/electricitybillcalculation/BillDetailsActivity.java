package com.lambton.daianaiziatov.electricitybillcalculation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class BillDetailsActivity extends AppCompatActivity {

    private TextView detailsEditText;
    private ElectricityBill electricityBill;
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        electricityBill = getIntent().getParcelableExtra("ElectricityBill");
        detailsEditText = findViewById(R.id.details_text_view);
        String billDetails = "Customer ID: " + electricityBill.getCustomerId() +
                "\nCustomer Name: " + electricityBill.getCustomerName() +
                "\nCustomer Email: " + electricityBill.getCustomerEmail() +
                "\nCustomer Gender: " + electricityBill.getGender() +
                "\nBill Date: " + dateTimeFormatter.format(electricityBill.getBillDate()) +
                "\nUnits Consumed: " + electricityBill.getUnitConsumed() +
                "\nTotal: $" + electricityBill.getTotalBill();
        detailsEditText.setText(billDetails);
    }
}
