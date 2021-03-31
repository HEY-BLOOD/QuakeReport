package com.example.quakereport;

public class Earthquake {
    /**
     * 地震震级
     */
    private double mag;

    /**
     * 地震位置（完整位置）
     */
    private String place;

    /**
     * Time of the earthquake
     */
    private long time;

    /**
     * 地震的网站 URL
     */
    private String url;

    /**
     * 返回地震的震级
     */
    public double getMag() {
        return mag;
    }

    /**
     * 返回地震发生的位置
     */
    public String getPlace() {
        return place;
    }

    /**
     * 返回地震的时间。
     */
    public long getTime() {
        return time;
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
     * @param mag   表示地震的震级（大小）
     * @param place 表示地震的城市位置
     * @param time  表示地震发生时以毫秒（根据 Epoch）计的时间
     * @param url   表示用于查找关于地震的更多详细信息的网站 URL
     */
    public Earthquake(double mag, String place, long time, String url) {
        this.mag = mag;
        this.place = place;
        this.time = time;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "mag=" + mag +
                ", place='" + place + '\'' +
                ", time=" + time +
                ", url='" + url + '\'' +
                '}';
    }
}
