package com.example.soujanya.moiveapp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.soujanya.moiveapp.DetailActivity;
import com.example.soujanya.moiveapp.MainActivity;
import com.example.soujanya.moiveapp.R;
import com.example.soujanya.moiveapp.adapter.GridViewAdapter;
import com.example.soujanya.moiveapp.modal.ImageItem;
import com.example.soujanya.moiveapp.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by soujanya on 10/28/15.
 */
public class MainFragment extends Fragment implements Constants {

    private MainActivity mainActivity;
    private GridViewAdapter adapter;
    private GridView gridView;
    private String movieJsonStr = null;
    private ArrayList<ImageItem> imgList;
    String movieId = null;
    String typeOfSort;
    ArrayList<ImageItem> unsortedlist;

    private final String LOG_TAG = MainFragment.class.getSimpleName();


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.grid_layout, null);

        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute();


        imgList = new ArrayList<ImageItem>();


        gridView = (GridView) rootView.findViewById(R.id.grid_view);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = adapter.getItem(position).getImageUrl();
                String plotSynopsis = adapter.getItem(position).getPlotSynopsis();
                String title = adapter.getItem(position).getTitle();
                String releaseDate = adapter.getItem(position).getReleaseDate();
                String movieId = adapter.getItem(position).getMovieId();
                String rating = adapter.getItem(position).getUserRating();


                Intent detailintent = new Intent(getActivity(), DetailActivity.class);

                detailintent.putExtra("url", url);
                detailintent.putExtra("plotSynopsis", plotSynopsis);
                detailintent.putExtra("title", title);
                detailintent.putExtra("releaseDate", releaseDate);
                detailintent.putExtra("movieId", movieId);
                detailintent.putExtra("rating", rating);


                startActivity(detailintent);
            }
        });

        setHasOptionsMenu(true);


        return rootView;
    }

    public void doSort(String sortBy) {

        if (adapter != null) {

            sortList(sortBy);


            adapter.setData(adapter.getData());
            adapter.notifyDataSetChanged();
        }
    }

    private void sortList(String sortBy) {

        if (sortBy.equals("rating")) {

            Collections.sort(imgList, new Comparator<ImageItem>() {
                @Override
                public int compare(ImageItem lhs, ImageItem rhs) {
                    Log.d(LOG_TAG, "comoparattoe" + lhs.getUserRating());

                    Float lhsRating = Float.valueOf(lhs.getUserRating().toString());

                    Float rhsRating = Float.valueOf(rhs.getUserRating().toString());

                    Log.d(LOG_TAG, "comoparattoe" + lhs.getUserRating() + " " + rhs.getUserRating());


                    return lhsRating.compareTo(rhsRating);

                }

            });


        } else {

            Collections.sort(imgList, new Comparator<ImageItem>() {
                @Override
                public int compare(ImageItem lhs, ImageItem rhs) {
                    Log.d(LOG_TAG, "else+++++" + lhs.getPopular());

                    Float lhsPopular = Float.valueOf(lhs.getPopular().toString());

                    Float rhsPopular = Float.valueOf(rhs.getPopular().toString());

                    Log.d(LOG_TAG, "comoparattoe" + lhs.getUserRating() + " " + rhs.getUserRating());


                    return rhsPopular.compareTo(lhsPopular);

                }
            });
        }
    }


    public class FetchMoviesTask extends AsyncTask<Void, Void, List<ImageItem>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        List<ImageItem> result;

        public void onPreExecute() {

            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        public List<ImageItem> doInBackground(Void... params) {

            result = new ArrayList<ImageItem>();

            Log.d(LOG_TAG, " ");

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {


                URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc");

                final String MOVIE_BASEURL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASEURL).buildUpon().appendQueryParameter(SORT_PARAM, "popularity.desc")
                        .appendQueryParameter(API_KEY, "6682498650772724ef3528371f9a8a87").build();

                URL newUrl = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) newUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                movieJsonStr = buffer.toString();


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }

                return getMoviesDataFromJson(movieJsonStr);
            }


        }

        private List<ImageItem> getMoviesDataFromJson(String movieJsonStr) {


            final String MOVIE_LIST = "results";
            final String POSTER_PATH = "poster_path";

            try {
                JSONObject jsonObj = new JSONObject(movieJsonStr);
                JSONArray jsonArr = jsonObj.getJSONArray(MOVIE_LIST);

                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonData = jsonArr.getJSONObject(i);
                    String posterPath = jsonData.getString("poster_path");
                    String movieId = jsonData.getString("id");

                    String posterUrl = "http://image.tmdb.org/t/p/w185/" + posterPath;
                    String plotSynopsis = jsonData.getString("overview");
                    String userRating = jsonData.getString("vote_average");
                    String title = jsonData.getString("original_title");
                    String releaseDate = jsonData.getString("release_date");
                    String popular = jsonData.getString("popularity");

                    result.add(new ImageItem(posterUrl, movieId, plotSynopsis, title, releaseDate, userRating, popular));


                }


                return result;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(List<ImageItem> result) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            typeOfSort = mainActivity.prefs.getString(SORT_TYPE);

            if (result != null) {


                for (ImageItem dayForecastStr : result) {

                    imgList.add(dayForecastStr);
                    unsortedlist = imgList;
                }

                adapter = new GridViewAdapter(mainActivity, imgList);
                gridView.setAdapter(adapter);

            }
        }
    }

}
