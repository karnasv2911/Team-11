package com.kickstart.woc.wocdriverapp.utils.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.model.ClusterMarker;

public class WocClusterManagerRenderer extends DefaultClusterRenderer<ClusterMarker> {

    private final IconGenerator iconGenerator;
    private View markerView;
    private Context context;
    private LayoutInflater inflater;

    public WocClusterManagerRenderer(Context context, GoogleMap map, ClusterManager<ClusterMarker> clusterManager) {
        super(context, map, clusterManager);
        this.context = context.getApplicationContext();
        iconGenerator = new IconGenerator(context);

        inflater = LayoutInflater.from(context);
        markerView = inflater.inflate(R.layout.marker_view, null, false);
        iconGenerator.setContentView(markerView);
    }

    @Override
    protected void onBeforeClusterItemRendered(ClusterMarker item, MarkerOptions markerOptions) {
        markerOptions.icon(convertIntImageToBitMapDescriptor(item)).title(item.getTitle());
    }

    private BitmapDescriptor convertIntImageToBitMapDescriptor(final ClusterMarker item) {
        TextView txtName = (TextView) markerView.findViewById(R.id.title);
        txtName.setText(item.getTitle());
        ImageView imageView = (ImageView) markerView.findViewById(R.id.image);
        imageView.setImageResource(item.getImage());
        Bitmap icon = iconGenerator.makeIcon();
        return BitmapDescriptorFactory.fromBitmap(icon);
    }

    @Override
    protected void onClusterItemUpdated(ClusterMarker item, Marker marker) {
        marker.setIcon(convertIntImageToBitMapDescriptor(item));
        marker.setTitle(item.getTitle());
        marker.setPosition(item.getPosition());
        marker.setSnippet(item.getSnippet());
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<ClusterMarker> cluster) {
        return false;
    }

    /**
     * Update the GPS coordinate of a ClusterItem
     *
     * @param clusterMarker
     */
    public void setUpdateMarker(ClusterMarker clusterMarker) {
        Marker marker = getMarker(clusterMarker);
        if (marker != null) {
            marker.setPosition(clusterMarker.getPosition());
        }
    }
}
