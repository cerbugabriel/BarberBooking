package com.example.barberbooking.Interface;

import com.example.barberbooking.Model.Salon;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSucces(List<Salon> salonList);
    void onBranchLoadFailed(String message);
}

