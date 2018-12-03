package com.lambton.daianaiziatov.electricitybillcalculation;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ElectricityBillActivity extends AppCompatActivity {

    private EditText customerIdEditText;
    private EditText customerNameEditText;
    private EditText customerEmailEditText;
    private RadioGroup customerGenderRadioGroup;
    private EditText billDateEditText;
    private EditText unitsConsumedEitText;
    private TextView totalTextView;

    private ElectricityBill electricityBill;
    private AppDatabase db;

    private Date currentDate = new Date(System.currentTimeMillis());
    private Calendar billDate = Calendar.getInstance();
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy");
    private String alertMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_bill);
        db = AppDatabase.getAppDatabase(this);
        electricityBill = new ElectricityBill();
        currentDate = new Date(System.currentTimeMillis());
        findById();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                billDate.set(Calendar.YEAR, year);
                billDate.set(Calendar.MONTH, monthOfYear);
                billDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                billDateEditText.setText(dateTimeFormatter.format(billDate.getTime()));
            }

        };

        billDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ElectricityBillActivity.this, date, billDate
                        .get(Calendar.YEAR), billDate.get(Calendar.MONTH),
                        billDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        customerGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male_radio_button: electricityBill.setGender("Male");
                        break;
                    case R.id.female_radio_button: electricityBill.setGender("Female");
                        break;
                    case R.id.other_radio_button: electricityBill.setGender("Other");
                        break;
                }
            }
        });
        unitsConsumedEitText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    int units = Integer.parseInt(unitsConsumedEitText.getText().toString());
                    totalTextView.setText("Total: $" + ElectricityBill.calculateTotal(units));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_my_bills: {
                Intent intent = new Intent(this, ListBillActivity.class);
                this.startActivity(intent);
                break;
            }
            case R.id.menu_log_out: {
                Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void confirmPressed(View view) {
        if (isValid()) {
            setValuesFromEditTextsForElectricityBill();
            saveBillToDB();
            Intent intent = new Intent(this, BillDetailsActivity.class);
            intent.putExtra("ElectricityBill", electricityBill);
            this.startActivity(intent);
        } else {
            showAlertWithMessage(alertMessage);
            alertMessage = "";
        }
    }

    private void setValuesFromEditTextsForElectricityBill() {
        final String customerId = customerIdEditText.getText().toString();
        final String customerName = customerNameEditText.getText().toString();
        final String customerEmail = customerEmailEditText.getText().toString();
        final int units = Integer.parseInt(unitsConsumedEitText.getText().toString());
        electricityBill.setCustomerId(customerId);
        electricityBill.setCustomerName(customerName);
        electricityBill.setCustomerEmail(customerEmail);
        electricityBill.setBillDate(billDate.getTime());
        electricityBill.setUnitConsumed(units);
        electricityBill.setTotalBill(ElectricityBill.calculateTotal(units));
    }

    private void saveBillToDB() {
        db.getBillDao().insertAll(electricityBill);
    }

    private void findById() {
        customerIdEditText = findViewById(R.id.customer_id_edit_text);
        customerNameEditText = findViewById(R.id.customer_name_edit_view);
        customerEmailEditText = findViewById(R.id.customer_email_edit_text);
        customerGenderRadioGroup = findViewById(R.id.gender_radio_group);
        billDateEditText = findViewById(R.id.bill_date_edit_text);
        unitsConsumedEitText = findViewById(R.id.units_consumed_edit_text);
        totalTextView = findViewById(R.id.total_text_view);
    }

    private boolean isValid() {
        boolean valid = true;
        Pattern regexp;
        Matcher matcher;
        String customerId = customerIdEditText.getText().toString();
        if (TextUtils.isEmpty(customerId)) {
            customerIdEditText.setError("Required.");
            alertMessage += "\n-ID is required.";
            valid = false;
        } else {
            customerIdEditText.setError(null);
            regexp = Pattern.compile("^[C][0-9]{10}");
            matcher = regexp.matcher(customerId);
            boolean result = matcher.matches();
            if (!result) {
                valid = false;
                customerIdEditText.setError("Start from \"C\" with 10 numbers");
                alertMessage += "\n-ID must start from \"C\" with 10 numbers.";
            }
        }

        String name = customerNameEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            customerNameEditText.setError("Required.");
            alertMessage += "\n-Name is required.";
            valid = false;
        } else {
            customerNameEditText.setError(null);
            regexp = Pattern.compile("^(?![\\s.]+$)[a-zA-Z\\s.]*$");
            matcher = regexp.matcher(name);
            boolean result = matcher.matches();
            if (!result) {
                valid = false;
                customerNameEditText.setError("Only alphabets and spaces.");
                alertMessage += "\n-Name must include only alphabets and spaces.";
            }
        }

        String email = customerEmailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            customerEmailEditText.setError("Required.");
            alertMessage += "\n-Email is required.";
            valid = false;
        } else {
            customerEmailEditText.setError(null);
            regexp = Pattern.compile("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$");
            matcher = regexp.matcher(email);
            boolean result = matcher.matches();
            if (!result) {
                valid = false;
                customerEmailEditText.setError("Invalid email format");
                alertMessage += "\n-Invalid email format.";
            }
        }

        String date = billDateEditText.getText().toString();
        if (TextUtils.isEmpty(date)) {
            billDateEditText.setError("Required.");
            alertMessage += "\n-Date is required.";
            valid = false;
        } else {
            billDateEditText.setError(null);
            if (dateTimeFormatter.format(billDate.getTime()).equals(dateTimeFormatter.format(currentDate))) {
                billDateEditText.setError("Can't be current date!");
                alertMessage += "\n-Bill date can't be current date.";
                valid = false;
            }
        }

        String units = unitsConsumedEitText.getText().toString();
        if (TextUtils.isEmpty(units)) {
            unitsConsumedEitText.setError("Required.");
            alertMessage += "\n-Units is required.";
            valid = false;
        } else {
            unitsConsumedEitText.setError(null);
        }

        if (electricityBill.getGender() == null) {
            alertMessage += "\n-Please choose gender.";
            valid = false;
        }

        return valid;
    }

    private void showAlertWithMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(ElectricityBillActivity.this).create();
        alertDialog.setTitle("Please check these fields:");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
