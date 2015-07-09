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
    mainHappening2 adapterObj;
    ProgressDialog progress;
    ArrayList listName = new ArrayList();
    final ArrayList listUser = new ArrayList();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String user = null;
        final View view = inflater.inflate(R.layout.activity_fragment_b, container, false);
        //fetching the varaible from shared prefences
        sharedPreferences object = new sharedPreferences(getActivity().getApplicationContext());
        user = object.getText();
       if(savedInstanceState==null){
           progress = ProgressDialog.show(getActivity(), "Please Wait",
                   "Fetching Data...", true);
        //code for json
        String tag_json_obj = "json_obj_req";
        String url = "http://socialsearch.esy.es/facebook.php?q=" + user;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        l = (ListView) view.findViewById(R.id.listView);
                        try {
                            // ArrayList listImage=new ArrayList();
                            JSONArray dataSet = (JSONArray) response.get("data");
                            if (dataSet != null) {
                                int len = dataSet.length();
                                for (int i = 0; i < len; i++) {
                                    JSONObject json = dataSet.getJSONObject(i);
                                    listName.add(json.get("name"));
                                    listUser.add(json.get("id"));
                                    //  listImage.add(json.get("profile_picture"));

                                }
                            }
//                            l.setAdapter(adapter);
                            progress.hide();
                            adapterObj = new mainHappening2(getActivity(), listName, listUser);

                            l.setAdapter(adapterObj);
                            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String b = (String) listUser.get(position);
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.fb.com/" + b));
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
           adapterObj= (mainHappening2) savedInstanceState.getParcelable("myData");
           l.setAdapter(adapterObj);
           l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   String b = (String) listUser.get(position);
                   Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.fb.com/" + b));
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

class MyViewHolder2{
    TextView nameText;
    TextView userText;
    MyViewHolder2(View row){
          nameText = (TextView) row.findViewById(R.id.nameText);
          userText = (TextView) row.findViewById(R.id.userText);
    }
}

class mainHappening2 extends ArrayAdapter implements Parcelable {
    protected static final String TAG = "ERROR";
    Context context;
    String[] nameList;
    String[] userList;
    public mainHappening2(Context c,ArrayList names,ArrayList user) {
        super(c,R.layout.single_row,R.id.textView,names);
        this.context=c;
        this.nameList= (String[]) names.toArray(new String[names.size()]);
        this.userList= (String[]) user.toArray(new String[user.size()]);
    }
    public View getView(int position,View convertView,ViewGroup parent){
        View row=convertView;
        final MyViewHolder2 holder;
        if(row==null) {
            //only run if row is created for first time
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_row, parent, false);
            holder =new MyViewHolder2(row);
            row.setTag(holder);
        }
        else{
            holder= (MyViewHolder2) row.getTag();
        }
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
