package com.lambton.daianaiziatov.electricitybillcalculation;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@TypeConverters(DateConverter.class)
public class ElectricityBill implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String gender;
    private Date billDate;
    private int unitConsumed;
    private double totalBill;
    @Ignore
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy");

    @Ignore
    public ElectricityBill() {
    }

    public ElectricityBill(String customerId, String customerName, String customerEmail, String gender, Date billDate, int unitConsumed) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.gender = gender;
        this.billDate = billDate;
        this.unitConsumed = unitConsumed;
        this.totalBill = calculateTotal(this.unitConsumed);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerId);
        dest.writeString(customerName);
        dest.writeString(customerEmail);
        dest.writeString(gender);
        dest.writeString(dateTimeFormatter.format(billDate));
        dest.writeInt(unitConsumed);
        dest.writeDouble(totalBill);
    }

    @Ignore
    protected ElectricityBill(Parcel in) {
        customerId = in.readString();
        customerName = in.readString();
        customerEmail = in.readString();
        gender = in.readString();
        try {
            billDate = dateTimeFormatter.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        unitConsumed = in.readInt();
        totalBill = in.readDouble();
    }

    public static final Creator<ElectricityBill> CREATOR = new Creator<ElectricityBill>() {
        @Override
        public ElectricityBill createFromParcel(Parcel in) {
            return new ElectricityBill(in);
        }

        @Override
        public ElectricityBill[] newArray(int size) {
            return new ElectricityBill[size];
        }
    };


    public static double calculateTotal(int unitConsumed) {
        if (unitConsumed <= 100) {
            return unitConsumed * 0.75;
        } else if (unitConsumed > 100 && unitConsumed <= 250) {
            return  (100 * 0.75) + (unitConsumed - 100) * 1.25;
        } else if (unitConsumed > 250 && unitConsumed <= 450) {
            return  (100 * 0.75) + (150 * 1.25) + (unitConsumed - 250) * 1.75;
        } else {
            return (100 * 0.75) + (150 * 1.25) + (200 * 1.75) + (unitConsumed - 450) * 2.25;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public int getUnitConsumed() {
        return unitConsumed;
    }

    public void setUnitConsumed(int unitConsumed) {
        this.unitConsumed = unitConsumed;
    }

    public double getTotalBill() {
        return totalBill;
    }


}
