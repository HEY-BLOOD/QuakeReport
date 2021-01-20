package com.example.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(@NonNull Context context, @NonNull List<Earthquake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 检查是否已经有可以重用的列表项视图（称为 convertView），
        // 否则，如果 convertView 为 null，则 inflate 一个新列表项布局。
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_item, parent, false);
        }

        // 在地震列表中的给定位置找到地震
        Earthquake currentEarthquake = getItem(position);

        // 找到视图 ID 为 magnitude 的 TextView
        TextView magnitudeView = (TextView) itemView.findViewById(R.id.mag_text);
        // 在该 TextView 中显示目前地震的震级
        magnitudeView.setText(currentEarthquake.getMag());

        // 位置信息的拆分
        String primaryPlace;
        String placeOffset;
        String originalPlace = currentEarthquake.getPlace();
        if (originalPlace.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalPlace.split(LOCATION_SEPARATOR);
            placeOffset = parts[0] + LOCATION_SEPARATOR;
            primaryPlace = parts[1];
        } else {
            placeOffset = getContext().getString(R.string.near_the);
            primaryPlace = originalPlace;
        }
        // 找到 place 位置信息 的两个 TextView
        TextView placeOffsetView = (TextView) itemView.findViewById(R.id.place_offset_text);
        TextView primaryPlaceView = (TextView) itemView.findViewById(R.id.primary_place_text);
        // 在该 TextView 中显示目前地震的位置
        placeOffsetView.setText(placeOffset);
        primaryPlaceView.setText(primaryPlace);

        // 根据地震时间（以毫秒为单位）创建一个新的 Date 对象
        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());

        // 找到视图 ID 为 date 的 TextView
        TextView dateView = (TextView) itemView.findViewById(R.id.date_text);
        // 设置日期字符串的格式（即 "Mar 3, 1984"）
        String formattedDate = formatDate(dateObject);
        // 在该 TextView 中显示目前地震的日期
        dateView.setText(formattedDate);

        // 找到视图 ID 为 time 的 TextView
        TextView timeView = (TextView) itemView.findViewById(R.id.time_text);
        // 设置时间字符串的格式（即 "4:30PM"）
        String formattedTime = formatTime(dateObject);
        // 在该 TextView 中显示目前地震的时间
        timeView.setText(formattedTime);

        // 返回目前显示适当数据的列表项视图
        return itemView;
    }

    /**
     * 从 Date 对象返回格式化的日期字符串（即 "Mar 3, 1984"）。
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * 从 Date 对象返回格式化的时间字符串（即 "4:30 PM"）。
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }


}
