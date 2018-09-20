package com.vinayakspot.mobilechallenge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeLayout;
    ListView lv;
    ArrayList<DeliveryBean> dataModels;

    Spinner spin;
    String[] itemPerPage = { "10", "25", "50", "ALL"};


    private static DeliveryListAdapter adapter;

    private ProgressDialog dialog;

    String offset;
    String limit;

    ImageView noData, serverError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noData = (ImageView)findViewById(R.id.img_noData);
        serverError = (ImageView)findViewById(R.id.img_serverError);

        AndroidNetworking.initialize(getApplicationContext());
        offset = "0";

        spin = (Spinner) findViewById(R.id.spinner_quantity);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,itemPerPage);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                loadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lv = (ListView)findViewById(R.id.listView);
        dataModels = new ArrayList<>();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv = view.findViewById(R.id.text_desc);

                Intent intent = new Intent(getApplicationContext(), DeliveryDetailActivity.class);
                intent.putExtra("loc_latitude",dataModels.get(position).getLat());
                intent.putExtra("loc_longitude",dataModels.get(position).getLng());
                intent.putExtra("text_desc",dataModels.get(position).getDesc());
                intent.putExtra("text_address",dataModels.get(position).getAdd());
                startActivity(intent);


            }
        });

        swipeLayout = findViewById(R.id.swipe_container);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadData();

                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        swipeLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });


        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

    }



    public void getDeliveryList(){



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                if(spin.getSelectedItem().toString() == "ALL"){
                    limit = Integer.toString(Integer.MAX_VALUE);
                }
                else
                {
                    limit = spin.getSelectedItem().toString();
                }

                Log.d("abcd",limit);
                Log.d("abcd",Constants.ip+"/deliveries");

                OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                        .connectTimeout(7, TimeUnit.SECONDS)
                        .readTimeout(7, TimeUnit.SECONDS)
                        . writeTimeout(7, TimeUnit.SECONDS)
                        .build();

                AndroidNetworking.get(Constants.ip+"/deliveries")
                        .addQueryParameter("offset", offset)
                        .addQueryParameter("limit", limit)
                        .setPriority(Priority.LOW)
                        .setOkHttpClient(okHttpClient)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {

                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }

                                Log.d("abcd", response.toString());
                                JSONObject jsonObj = new JSONObject();

                                try {
                                    if(response.getJSONObject(0).getString("id")==null){

                                        noData();
                                        Log.d("abcd", "Json Empty");

                                    }
                                    else
                                    {
                                        //Success
                                        for(int i = 0; i < response.length(); i++)
                                        {
                                            jsonObj = response.getJSONObject(i);

                                            JSONObject objJSON = jsonObj.getJSONObject("location");

                                            dataModels.add(
                                                    new DeliveryBean(
                                                            jsonObj.getString("id"),
                                                            jsonObj.getString("description"),
                                                            jsonObj.getString("imageUrl"),
                                                            objJSON.getString("lat"),
                                                            objJSON.getString("lng"),
                                                            objJSON.getString("address")
                                                    ));
                                        }

                                        adapter= new DeliveryListAdapter(dataModels, getApplicationContext());
                                        lv.setAdapter(adapter);



                                    }
                                } catch (JSONException e) {

                                    serverError();

                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    e.printStackTrace();
                                    Log.d("abcd ", e.getMessage());
                                }
                            }

                            @Override
                            public void onError(ANError anError) {

                                serverError();

                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Log.d("abcd", anError.getMessage());
                            }
                        });
            }
        });

        thread.start();
    }

    public void loadData()
    {

        noData.setVisibility(View.GONE);
        serverError.setVisibility(View.GONE);

        lv.setAdapter(null);
        dataModels.clear();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.show();

        getDeliveryList();
    }

    public void serverError(){

        noData.setVisibility(View.GONE);
        serverError.setVisibility(View.VISIBLE);

    }

    public void noData(){

        noData.setVisibility(View.VISIBLE);
        serverError.setVisibility(View.GONE);

    }

}
