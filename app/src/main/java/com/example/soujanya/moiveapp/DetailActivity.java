package com.example.soujanya.moiveapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by soujanya on 10/29/15.
 */
public class DetailActivity extends AppCompatActivity {


    String url;
    String overview;
    String title;
    String releaseDate;
    String rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        ImageView img = (ImageView) findViewById(R.id.img);
        TextView tv_title = (TextView) findViewById(R.id.title);
        TextView tv_overView = (TextView) findViewById(R.id.tv_synopsis);
        TextView tv_releaseDate = (TextView) findViewById(R.id.releaseDate);
        TextView tv_rating = (TextView) findViewById(R.id.rating);


        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
            overview = getIntent().getStringExtra("plotSynopsis");
            title = getIntent().getStringExtra("title");
            releaseDate = getIntent().getStringExtra("releaseDate");
            rating = getIntent().getStringExtra("rating");


            Picasso
                    .with(getApplicationContext())
                    .load(url)
                    .into(img, new Callback() {

                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onError() {
                            // TODO Auto-generated method stub

                        }
                    });

            tv_overView.setText(overview);
            tv_rating.setText(rating);
            tv_releaseDate.setText(releaseDate);
            tv_title.setText(title);

        }


    }
}
