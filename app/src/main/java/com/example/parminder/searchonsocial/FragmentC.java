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


public class FragmentC extends Fragment {
    protected static final String TAG = "ERROR";
    ListView l;
    ProgressDialog progress;
    ArrayList listName = new ArrayList();
    final ArrayList listUser = new ArrayList();
    ArrayList listImage = new ArrayList();
    mainHappening3 adapterObj;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String user=null;
        final View view = inflater.inflate(R.layout.activity_fragment_c, container, false);
        //fetching the varaible from shared prefences
        sharedPreferences object=new sharedPreferences(getActivity().getApplicationContext());
        user = object.getText();
        if(savedInstanceState==null) {
            progress=ProgressDialog.show(getActivity(),"Please Wait...","Fetching Data",true);
            //code for json
            String tag_json_obj = "json_obj_req";

            String url = "https://api.github.com/search/users?q=" + user;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            l = (ListView) view.findViewById(R.id.listView);
                            try {
                                JSONArray dataSet = (JSONArray) response.get("items");
                                if (dataSet != null) {
                                    int len = dataSet.length();
                                    for (int i = 0; i < len; i++) {
                                        JSONObject json = dataSet.getJSONObject(i);
                                        listName.add(json.get("login"));
                                        listUser.add(json.get("html_url"));
                                        listImage.add(json.get("avatar_url"));
                                    }
                                }
//                            l.setAdapter(adapter);
                                progress.hide();
                                adapterObj = new mainHappening3(getActivity(), listName, listUser, listImage);

                                l.setAdapter(adapterObj);
                                l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String b = (String) listUser.get(position);
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
            adapterObj= (mainHappening3) savedInstanceState.getParcelable("myData");
            l.setAdapter(adapterObj);
            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String b = (String) listUser.get(position);
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
class MyViewHolder3{
    TextView nameText;
    TextView userText;
    ImageView myImage;
    MyViewHolder3(View row){
        nameText = (TextView) row.findViewById(R.id.nameText);
        myImage = (ImageView) row.findViewById(R.id.photo);
        userText = (TextView) row.findViewById(R.id.userText);

    }
}

class mainHappening3 extends ArrayAdapter implements Parcelable {
    protected static final String TAG = "ERROR";
    Context context;
     String[] images;
    String[] nameList;
    String[] userList;
    public mainHappening3(Context c,ArrayList names,ArrayList user,ArrayList imgs) {
        super(c,R.layout.single_row,R.id.textView,names);
        this.context=c;
        this.nameList= (String[]) names.toArray(new String[names.size()]);
        this.userList= (String[]) user.toArray(new String[user.size()]);
        this.images= (String[]) imgs.toArray(new String[imgs.size()]);
    }
    public View getView(int position,View convertView,ViewGroup parent){
        View row=convertView;
        String a = images[position];
        final MyViewHolder3 holder;
        if(row==null) {
            //only run if row is created for first time
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_row, parent, false);
            holder = new MyViewHolder3(row);
            row.setTag(holder);
        }
        else{
            holder= (MyViewHolder3) row.getTag();
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
                    ImageView myImage;
                    holder.myImage.setImageBitmap(response.getBitmap());
                }
            }
        });
        if(!(nameList[position].isEmpty()))
            holder.nameText.setText(nameList[position]);
        else
            holder.nameText.setText("Nothing to show");
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
