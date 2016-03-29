package com.example.malik.tingle;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private RecyclerView mRecyclerView;
    private TingleAdapter mAdapter;
    private View v;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_list,parent,false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.listview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();


        return v;
    }
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }



    private ArrayList<String> copyToString(List<Thing> thingsDB){
        ArrayList<String> allThings=new ArrayList<String>();
        for(int i=0;i<thingsDB.size();i++)
            allThings.add(thingsDB.get(i).toString());
        return allThings;
    }

    public void updateUI(){
        ThingsDB thingsDB=ThingsDB.get(getActivity());
        ArrayList<Thing> things=thingsDB.getThingsDB();
        if(mAdapter==null){
        mAdapter =new TingleAdapter(things);
        mRecyclerView.setAdapter(mAdapter);}
        else
            mAdapter.notifyDataSetChanged();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView=(TextView)itemView;
            mTextView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view){
            Intent intent = new Intent(getContext(), PagerActivity.class);
            int position=getLayoutPosition();
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }

    private class TingleAdapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<Thing> things;

        public TingleAdapter(ArrayList<Thing> a){
            things=a;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Thing thing=things.get(position);
            holder.mTextView.setText(thing.getmWhat()+" is here :"+thing.getmWhere());

        }

        @Override
        public int getItemCount() {
            return things.size();
        }
    }

}
