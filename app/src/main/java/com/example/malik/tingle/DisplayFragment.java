package com.example.malik.tingle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;

/**
 * Created by malik on 2/23/16.
 */
public class DisplayFragment extends Fragment {

    private static ThingsDB thingsDB;
    public static String EXTRA_INT="com.malik.myInt";
    public static String BIG="BIG";

    private TextView textView;
    private String display;
    private Button delete;
    private ImageView mPhotoView;
    private File mFile;
    private static final String EXTRA="myFile";


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        thingsDB=ThingsDB.get(this.getContext());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_display,parent,false);

        textView=(TextView)v.findViewById(R.id.display_message);
       // display=getActivity().getIntent().getStringExtra("value");
        int pos= getArguments().getInt(EXTRA_INT,0);
        Thing thing=thingsDB.getThingsDB().get(pos);
        textView.setText(thing.toString());

        mPhotoView=(ImageView) v.findViewById(R.id.display_photo);
        mFile=new File(thing.getPhoto());
        Bitmap bitmap=BitmapMagic.getScaledBitmap(mFile.getPath(),getActivity());
        mPhotoView.setImageBitmap(bitmap);

        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", BIG+mFile.getName());
                Intent intent=new Intent(getActivity(),ImageActivity.class);
                intent.putExtra(EXTRA,mFile.getPath());
                startActivity(intent);
            }
        });


        delete=(Button) v.findViewById(R.id.button_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    thingsDB.delete(getActivity().getIntent().getIntExtra("position",0));
                thingsDB.delete(getArguments().getInt(EXTRA_INT,0));
                Intent intent=new Intent(DisplayFragment.this.getContext(),ListActivity.class);
                startActivity(intent);
            }
        });


        return v;
    }

    public static DisplayFragment newInstance(int position){
        Bundle args=new Bundle();
        args.putInt(EXTRA_INT, position);

        DisplayFragment displayFragment=new DisplayFragment();
        displayFragment.setArguments(args);

        return displayFragment;
    }
}
