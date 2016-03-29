package com.example.malik.tingle;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by malik on 2/18/16.
 */
public class TingleFragment extends Fragment{

    private static ThingsDB thingsDB;

    private Button addThing,searchThing,mShowAll;
    private TextView lastAdded;
    private TextView newWhat,newWhere;


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        thingsDB=ThingsDB.get(this.getContext());
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tingle, parent, false);

        lastAdded=(TextView) v.findViewById(R.id.last_thing);
        // updateUI();


        searchThing=(Button) v.findViewById(R.id.search);
        addThing=(Button) v.findViewById(R.id.add_button);

        newWhat=(TextView) v.findViewById(R.id.what_text);
        newWhere=(TextView) v.findViewById(R.id.where_text);

        // version=(TextView) findViewById(R.id.version);
        // version.setText(Build.VERSION.CODENAME);

        addThing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thingsDB.addThing(new Thing(newWhat.getText().toString(), newWhere.getText().toString()));
                newWhat.setText("");
                newWhere.setText("");
                updateUI();
                FragmentManager fm=getActivity().getSupportFragmentManager();
                ListFragment fragment_list=(ListFragment)fm.findFragmentById(R.id.list_tingle);
                if(fragment_list!=null)
                    fragment_list.updateUI();
            }
        });


        searchThing.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String thing=newWhat.getText().toString();
                Thing t=searchMyThing(thing);
                Toast.makeText(TingleFragment.this.getContext(), t.getmWhere(), Toast.LENGTH_SHORT).show();


            }
        });

        if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE) {
            mShowAll = (Button) v.findViewById(R.id.list_all);
            mShowAll.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(TingleFragment.this.getContext(), ListActivity.class);
                    ArrayList<String> allThings = copyToString((ArrayList<Thing>) thingsDB.getThingsDB());
                    intent.putExtra("all", allThings);
                    startActivity(intent);
                }
            });
        }

        return v;
    }

    private void updateUI() {
        int s=thingsDB.size();
        if(s>0)
            lastAdded.setText(thingsDB.get(s-1).toString());
    }


    private Thing searchMyThing(String s){

        String where="";
        for (int i=0;i<thingsDB.size();i++){
            if(thingsDB.get(i).getmWhat().equals(s))
                where=thingsDB.get(i).getmWhere();
        }
        if (where.equals(""))
            where="object not found";
        return new Thing(s,where);
    }

    private ArrayList<String> copyToString(ArrayList<Thing> thingsDB){
        ArrayList<String> allThings=new ArrayList<String>();
        for(int i=0;i<thingsDB.size();i++)
            allThings.add(thingsDB.get(i).toString());
        return allThings;
    }

}
