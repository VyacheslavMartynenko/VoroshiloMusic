package com.music.voroshilo.model.networking;

import com.google.gson.annotations.SerializedName;

public class DataBody {
    @SerializedName("net_type")
    private int netType;

    @SerializedName("popup")
    private int popup;

    @SerializedName("popup_url")
    private String popupUrl;

    public DataBody(int netType, int popup, String popupUrl) {
        this.netType = netType;
        this.popup = popup;
        this.popupUrl = popupUrl;
    }

    public int getNetType() {
        return netType;
    }

    public void setNetType(int netType) {
        this.netType = netType;
    }

    public int getPopup() {
        return popup;
    }

    public void setPopup(int popup) {
        this.popup = popup;
    }

    public String getPopupUrl() {
        return popupUrl;
    }

    public void setPopupUrl(String popupUrl) {
        this.popupUrl = popupUrl;
    }
}
