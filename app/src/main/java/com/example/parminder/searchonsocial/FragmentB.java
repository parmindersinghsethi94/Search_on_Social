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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentB extends Fragment {
    protected static final String TAG = "ERROR";
     ListView l;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String user=null;
        final View view = inflater.inflate(R.layout.activity_fragment_b, container, false);
        final TextView loading= (TextView) view.findViewById(R.id.loading);
        //fetching the varaible from shared prefences
        sharedPreferences object=new sharedPreferences(getActivity().getApplicationContext());
        if(object!=null) {
            user = object.getText();
            Toast.makeText(getActivity(),user+" clicked",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getActivity(),"clicked",Toast.LENGTH_LONG).show();
        }
        //code for json
        String tag_json_obj = "json_obj_req";

        String url = "http://10.10.20.169:82/Social_Search/facebook.php?q="+user;
//        String url = "http://socialsearch.esy.es/facebook.php?q="+user;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        l= (ListView) view.findViewById(R.id.listView);
                        try {
                            ArrayList listName=new ArrayList();
                            final ArrayList listUser=new ArrayList();
                           // ArrayList listImage=new ArrayList();
                            JSONArray dataSet = (JSONArray) response.get("data");
                            if (dataSet != null) {
                                int len = dataSet.length();
                                for (int i=0;i<len;i++){
                                    JSONObject json=dataSet.getJSONObject(i);
                                    listName.add(json.get("name"));
                                    listUser.add(json.get("id"));
                                  //  listImage.add(json.get("profile_picture"));

                                }
                            }
//                            l.setAdapter(adapter);
                            mainHappening2 adapterObj=new mainHappening2(getActivity(),listName,listUser);
                            loading.setText(" ");
                            l.setAdapter(adapterObj);
                            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String b= (String) listUser.get(position);
                                    Toast.makeText(getActivity(), "You clicked " + b, Toast.LENGTH_LONG).show();
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.fb.com/"+ b));
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

class mainHappening2 extends ArrayAdapter {
    protected static final String TAG = "ERROR";
    Context context;
   // String[] images;
    String[] nameList;
    String[] userList;
    public mainHappening2(Context c,ArrayList names,ArrayList user) {
        super(c,R.layout.single_row,R.id.textView,names);
        this.context=c;
        this.nameList= (String[]) names.toArray(new String[names.size()]);
        this.userList= (String[]) user.toArray(new String[user.size()]);
    }
    public View getView(int position,View convertView,ViewGroup parent){
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.single_row,parent,false);
        TextView nameText = (TextView) row.findViewById(R.id.nameText);
        TextView userText = (TextView) row.findViewById(R.id.userText);
        if(!(nameList[position].isEmpty()))
            nameText.setText(nameList[position]);
        else
            nameText.setText("Nothing to show");
        return row;
    }
}
