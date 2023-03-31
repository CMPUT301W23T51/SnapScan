package com.example.SnapScan.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SnapScan.R;
import com.example.SnapScan.model.QRcode;

import java.util.ArrayList;


public class QRListFragment extends Fragment {
    protected RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_qr_list, container, false);
        ArrayList<QRcode> qrList = new ArrayList<>();
        qrList.add(new QRcode("test"));
        qrList.add(new QRcode("work"));
        recyclerView = view.findViewById(R.id.recyclerView_qr_list);
        QRListAdapter qrListAdapter = new QRListAdapter(qrList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(qrListAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
