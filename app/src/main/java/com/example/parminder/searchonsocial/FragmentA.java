package com.example.parminder.searchonsocial;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentA extends Fragment {
    protected static final String TAG = "ERROR";

    ListView l;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String user=null;
        final View view = inflater.inflate(R.layout.activity_fragment, container, false);
        final TextView loading= (TextView) view.findViewById(R.id.loading);
        //fetching the varaible from shared prefences
        sharedPreferences object=new sharedPreferences(getActivity().getApplicationContext());

        if(object!=null) {
           user = object.getText();
            //Toast.makeText(getActivity(),user,Toast.LENGTH_LONG).show();
        }
        else {

        }
       //code for json
        String tag_json_obj = "json_obj_req";
        String url = "http://socialsearch.esy.es/instagram.php?q="+user;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        l= (ListView) view.findViewById(R.id.listView);
                        try {
                            ArrayList listName=new ArrayList();
                            final ArrayList listUser=new ArrayList();
                            ArrayList listImage=new ArrayList();
                            JSONArray dataSet = (JSONArray) response.get("data");
                            if (dataSet != null) {
                                int len = dataSet.length();
                                for (int i=0;i<len;i++){
                                    JSONObject json=dataSet.getJSONObject(i);
                                    listName.add(json.get("full_name"));
                                    listUser.add(json.get("username"));
                                    listImage.add(json.get("profile_picture"));

                                }
                            }
//                            l.setAdapter(adapter);
                            mainHappening adapterObj=new mainHappening(getActivity(),listName,listUser,listImage);
                            loading.setText(" ");
                            l.setAdapter(adapterObj);
                            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String b= (String) listUser.get(position);
                                    Toast.makeText(getActivity(),"You clicked "+b,Toast.LENGTH_LONG).show();
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/"+b));
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
        return view;

    }



}

class mainHappening extends ArrayAdapter{
    protected static final String TAG = "ERROR";
    Context context;
    String[] images;
    String[] nameList;
    String[] userList;
    TextView loading;
    public mainHappening(Context c,ArrayList names,ArrayList user,ArrayList imgs) {
        super(c,R.layout.single_row,R.id.textView,names);
        this.context=c;
        this.images= (String[]) imgs.toArray(new String[imgs.size()]);
        this.nameList= (String[]) names.toArray(new String[names.size()]);
        this.userList= (String[]) user.toArray(new String[user.size()]);
    }
    public View getView(int position,View convertView,ViewGroup parent){
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.single_row,parent,false);
        final ImageView myImage = (ImageView) row.findViewById(R.id.photo);
        TextView nameText = (TextView) row.findViewById(R.id.nameText);
        TextView userText = (TextView) row.findViewById(R.id.userText);
        String a = images[position];

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
                    myImage.setImageBitmap(response.getBitmap());
                }
            }
        });
        if(!(nameList[position].isEmpty()))
             nameText.setText(nameList[position]);
        else
            nameText.setText("Nothing to show");
        if(!(userList[position].isEmpty()))
            userText.setText(userList[position]);
        else
            nameText.setText("Nothing to show");

        //image setting finished
        return row;
    }
}
