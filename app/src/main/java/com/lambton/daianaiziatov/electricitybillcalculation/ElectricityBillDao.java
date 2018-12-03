package com.lambton.daianaiziatov.electricitybillcalculation;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface ElectricityBillDao {
    // Add Bill to DB
    @Insert
    void insertAll(ElectricityBill... electricityBills);

    // Delete Bill from DB
    @Delete
    void delete(ElectricityBill electricityBill);

    // Get all bills
    @Query("SELECT * FROM electricityBill")
    List<ElectricityBill> getAllBills();


}
