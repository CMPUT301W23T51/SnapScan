package com.example.SnapScan.ui.qr;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QRViewModel extends ViewModel {

    private QRcode ScannedQrCode;

    public QRViewModel() {
    }

    public void setScannedQrCode(QRcode qrCode){
        this.ScannedQrCode = qrCode;
    }
    public QRcode getScannedQrCode(){
        return this.ScannedQrCode;
    }
}