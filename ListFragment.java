package com.example.malik.tingle;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malik on 2/17/16.
 */
public class ListFragment extends Fragment{
    private static ThingsDB allThings;
    private ListView listView;
    private View v;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  allThings=getIntent().getStringArrayListExtra("all");
        allThings = ThingsDB.get(this.getContext());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_list,parent,false);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, copyToString(allThings.getThingsDB()));
        listView = (ListView) v.findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListFragment.this.getContext(), PagerActivity.class);
               /* listText = (TextView) view.findViewById(R.id.listText);
                String text = listText.getText().toString();
                intent.putExtra("value", text);*/
                intent.putExtra("position",position);
                startActivity(intent);


            }
        });
        return v;
    }



    private ArrayList<String> copyToString(List<Thing> thingsDB){
        ArrayList<String> allThings=new ArrayList<String>();
        for(int i=0;i<thingsDB.size();i++)
            allThings.add(thingsDB.get(i).toString());
        return allThings;
    }

    public void updateUI(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.list_view_row, R.id.listText, copyToString(allThings.getThingsDB()));
        listView = (ListView) v.findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);
    }

}
