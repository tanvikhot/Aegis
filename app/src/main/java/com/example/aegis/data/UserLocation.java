package com.example.aegis.data;

public class UserLocation {
    private Double latitude;
    private Double longitude;
    private String helpText;
    private String responseText;
    private String acceptHelp;

    public String getAcceptHelp() {
        return acceptHelp;
    }

    public void setAcceptHelp(String acceptHelp) {
        this.acceptHelp = acceptHelp;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public UserLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.helpText = "";
        this.responseText = "";
        this.acceptHelp = "";
    }

    public UserLocation() {
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
