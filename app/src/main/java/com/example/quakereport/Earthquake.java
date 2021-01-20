package com.example.quakereport;

public class Earthquake {
    private String mag;
    private String place;

    /**
     * Time of the earthquake
     */
    private long timeInMilliseconds;

    public String getMag() {
        return mag;
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
     * 构造一个新的 {@link Earthquake} 对象。
     *
     * @param mag   表示地震的震级（大小）
     * @param place 表示地震的城市位置
     * @param timeInMilliseconds  表示地震发生时以毫秒（根据 Epoch）计的时间
     */
    public Earthquake(String mag, String place, long timeInMilliseconds) {
        this.mag = mag;
        this.place = place;
        this.timeInMilliseconds = timeInMilliseconds;
    }
}
