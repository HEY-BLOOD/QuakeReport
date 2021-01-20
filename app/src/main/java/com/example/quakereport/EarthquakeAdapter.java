package com.example.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(@NonNull Context context, @NonNull List<Earthquake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        Earthquake earthquake;
        ViewHolder holder;

        if (null == itemView) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_item, null);
            holder = new ViewHolder();
            holder.tvMag = itemView.findViewById(R.id.mag_text);
            holder.tvPlace = itemView.findViewById(R.id.place_text);
            holder.tvTime = itemView.findViewById(R.id.time_text);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        earthquake = getItem(position);

        holder.tvMag.setText(earthquake.getMag());
        holder.tvPlace.setText(earthquake.getPlace());
        holder.tvTime.setText(earthquake.getTime());

        return itemView;
    }

    private static class ViewHolder {
        private TextView tvMag;
        private TextView tvPlace;
        private TextView tvTime;
    }
}
