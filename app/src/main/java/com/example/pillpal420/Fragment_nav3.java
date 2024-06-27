package com.example.pillpal420;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

//Inventur Fragment
public class Fragment_nav3 extends Fragment {

    private BtnClickListener btnListener;
    private RecyclerView recView;
    private ArrayList<InvItem> invList;
    private InvAdapter invAdapter;
    public interface BtnClickListener{
        void onButtonClicked();
    }
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        try{
            btnListener = (BtnClickListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View invView = inflater.inflate(R.layout.fragment_inventur, container, false);

        //RecyclerView
        recView = invView.findViewById(R.id.recView);
        invList = new ArrayList<InvItem>();
        invAdapter = new InvAdapter(invList);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recView.setAdapter(invAdapter);
        //CamButton
        Button invBtn = (Button) invView.findViewById(R.id.camButton);

        invBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnListener.onButtonClicked();
            }
        });

        return invView;
    }


    //get recyclerView
    public RecyclerView getRecView(){
        return recView;
    }
    public ArrayList<InvItem> getInvList(){return invList;}
    public InvAdapter getInvAdapter(){return invAdapter;}

}