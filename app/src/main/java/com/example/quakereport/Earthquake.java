package com.example.quakereport;

public class Earthquake {
    /**
     * 地震震级
     */
    private double magnitude;

    /**
     * 地震位置（完整位置）
     */
    private String place;

    /**
     * Time of the earthquake
     */
    private long timeInMilliseconds;

    /**
     * 地震的网站 URL
     */
    private String url;

    public double getMagnitude() {
        return magnitude;
    }

    public String getPlace() {
        return place;
    }

    /**
     * 返回地震的时间。
     */
    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    /**
     * 返回用于查找关于地震的更多信息的网站 URL。
     */
    public String getUrl() {
        return url;
    }

    /**
     * 构造一个新的 {@link Earthquake} 对象。
     *
     * @param magnitude          表示地震的震级（大小）
     * @param place              表示地震的城市位置
     * @param timeInMilliseconds 表示地震发生时以毫秒（根据 Epoch）计的时间
     * @param url                表示用于查找关于地震的更多详细信息的网站 URL
     */
    public Earthquake(double magnitude, String place, long timeInMilliseconds, String url) {
        this.magnitude = magnitude;
        this.place = place;
        this.timeInMilliseconds = timeInMilliseconds;
        this.url = url;
    }
}
