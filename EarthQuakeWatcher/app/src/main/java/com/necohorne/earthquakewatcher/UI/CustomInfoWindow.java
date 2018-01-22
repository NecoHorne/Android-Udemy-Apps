package com.necohorne.earthquakewatcher.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.necohorne.earthquakewatcher.R;

/**
 * Created by necoh on 2018/01/18.
 */

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private View view;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomInfoWindow(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.custom_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        TextView mTitle = (TextView) view.findViewById(R.id.winTitle);
        mTitle.setText(marker.getTitle());

        TextView magnitude = (TextView) view.findViewById( R.id.magnitudeId );
        magnitude.setText( marker.getSnippet());

        Button moreBtn = (Button) view.findViewById( R.id.btn );

        return view;
    }
}
