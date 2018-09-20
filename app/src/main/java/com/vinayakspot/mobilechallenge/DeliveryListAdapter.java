package com.vinayakspot.mobilechallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class DeliveryListAdapter extends ArrayAdapter<DeliveryBean> implements View.OnClickListener {

    private ArrayList<DeliveryBean> dataSet;
    Context mContext;


    static int temp = 0;

    private static class ViewHolder {
        ImageView image_delivery;
        TextView text_desc;
        TextView text_location;
    }

    public DeliveryListAdapter(ArrayList<DeliveryBean> data, Context context) {
        super(context, R.layout.deliveries_list_item, data);
        this.dataSet = data;
        this.mContext = context;
    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DeliveryBean dataModel=(DeliveryBean)object;

        switch (v.getId())
        {
            case R.id.text_desc:
                break;
        }

    }


    private int lastPosition = -1;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DeliveryBean dataModel = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.deliveries_list_item, parent, false);
            viewHolder.image_delivery = (ImageView) convertView.findViewById(R.id.image_delivery);
            viewHolder.text_desc = (TextView) convertView.findViewById(R.id.text_desc);
            viewHolder.text_location = (TextView)convertView.findViewById(R.id.text_location) ;

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        new DownLoadImageTask(viewHolder.image_delivery).execute(dataModel.getImg());

        viewHolder.text_desc.setText(dataModel.getDesc());
        viewHolder.text_location.setText(dataModel.getAdd());

        return convertView;
    }


    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();

                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
