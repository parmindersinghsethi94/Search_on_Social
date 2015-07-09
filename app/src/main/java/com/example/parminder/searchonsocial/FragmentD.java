package com.example.parminder.searchonsocial;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentD extends Fragment {
    protected static final String TAG = "ERROR";
    ListView l;
    mainHappening4 adapterObj;
    ProgressDialog progressDialog;
    ArrayList listName = new ArrayList();
    final ArrayList mainUrl = new ArrayList();
    ArrayList listImage = new ArrayList();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String user=null;
        final View view = inflater.inflate(R.layout.activity_fragment_d, container, false);
        //fetching the varaible from shared prefences
        sharedPreferences object=new sharedPreferences(getActivity().getApplicationContext());
        user = object.getText();

        if(savedInstanceState==null) {
            progressDialog=ProgressDialog.show(getActivity(),"Please Wait","Fetching Data",true);
            //code for json
            String tag_json_obj = "json_obj_req";
            String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + user;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            l = (ListView) view.findViewById(R.id.listView);
                            try {
                                JSONObject dataArray = (JSONObject) response.get("responseData");
                                JSONArray dataSet = (JSONArray) dataArray.get("results");
                                if (dataSet != null) {
                                    int len = dataSet.length();
                                    for (int i = 0; i < len; i++) {
                                        JSONObject json = dataSet.getJSONObject(i);
                                        listName.add(json.get("title"));
                                        mainUrl.add(json.get("originalContextUrl"));
                                        listImage.add(json.get("url"));

                                    }
                                }
//                            l.setAdapter(adapter);
                                progressDialog.hide();
                                 adapterObj = new mainHappening4(getActivity(), listName, mainUrl, listImage);
                                l.setAdapter(adapterObj);
                                l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String b = (String) mainUrl.get(position);
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(b));
                                        startActivity(browserIntent);
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error: " + error);


                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        }
        else{
            l= (ListView) view.findViewById(R.id.listView);
            adapterObj= (mainHappening4) savedInstanceState.getParcelable("myData");
            l.setAdapter(adapterObj);
            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String b = (String) mainUrl.get(position);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(b));
                    startActivity(browserIntent);
                }
            });
        }
        return view;

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("myData", (Parcelable) adapterObj);
    }

}
class MyViewHolder4{
    TextView nameText;
    ImageView myImage;
    MyViewHolder4(View row){
         myImage = (ImageView) row.findViewById(R.id.photo);
         nameText = (TextView) row.findViewById(R.id.nameText);
    }
}
class mainHappening4 extends ArrayAdapter implements Parcelable{
    protected static final String TAG = "ERROR";
    Context context;
    String[] images;
    String[] nameList;
    String[] userList;
    public mainHappening4(Context c,ArrayList names,ArrayList url,ArrayList imgs) {
        super(c,R.layout.single_row,R.id.textView,names);
        this.context=c;
        this.images= (String[]) imgs.toArray(new String[imgs.size()]);
        this.nameList= (String[]) names.toArray(new String[names.size()]);
        this.userList= (String[]) url.toArray(new String[url.size()]);
    }
    public View getView(int position,View convertView,ViewGroup parent){
        View row=convertView;
        String a = images[position];
        final MyViewHolder4 holder;
        //TextView userText = (TextView) row.findViewById(R.id.userText);
        if(row==null) {
            //only run if row is created for first time
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_row, parent, false);
            holder = new MyViewHolder4(row);
            row.setTag(holder);
        }
        else{
            holder= (MyViewHolder4) row.getTag();
        }
        //to set image
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(a, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    holder.myImage.setImageBitmap(response.getBitmap());
                }
            }
        });
        if(!(nameList[position].isEmpty()))
            holder.nameText.setText(nameList[position]);
        else
            holder.nameText.setText("Nothing to show");


        //image setting finished
        return row;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
