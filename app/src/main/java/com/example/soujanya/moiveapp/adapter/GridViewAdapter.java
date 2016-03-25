package com.example.soujanya.moiveapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.soujanya.moiveapp.R;
import com.example.soujanya.moiveapp.modal.ImageItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soujanya on 10/26/15.
 */
public class GridViewAdapter extends ArrayAdapter<ImageItem> {

    private final String LOG_TAG = GridViewAdapter.class.getSimpleName();
    private List<ImageItem> items;
    private Context context;

    private LayoutInflater inflater;


    public GridViewAdapter(Context context, ArrayList<ImageItem> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageItem item = items.get(position);

        ImageView imgView;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
            imgView = (ImageView) convertView.findViewById(R.id.grid_image);

        } else {
            imgView = (ImageView) convertView;
        }


        Picasso
                .with(context)
                .load(item.getImageUrl())

                .into(imgView, new Callback() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        // progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub

                    }
                });

        return imgView;
    }

    public List<ImageItem> getData() {
        return items;
    }


    public void setData(List<ImageItem> imageUrls) {
        this.items = imageUrls;
    }

    public int getCount() {
        if (items != null)
            return items.size();
        return 0;
    }

}
