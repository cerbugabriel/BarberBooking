package com.example.barberbooking.Interface;

import com.example.barberbooking.Model.Banner;

import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner> banners);
    void onBannerLoadFailed(String message);
}
