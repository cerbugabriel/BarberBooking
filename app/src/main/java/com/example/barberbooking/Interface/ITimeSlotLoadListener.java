package com.example.barberbooking.Interface;

import com.example.barberbooking.Model.TimeSlot;

import java.sql.Time;
import java.util.List;

public interface ITimeSlotLoadListener {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlots);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty();
}
