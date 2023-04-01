package com.example.SnapScan.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SnapScan.MainActivity;
import com.example.SnapScan.R;
import com.example.SnapScan.model.QRcode;

import java.util.ArrayList;

public class QRListAdapter extends RecyclerView.Adapter<QRListAdapter.QRListViewHolder> {
    public ArrayList<QRcode> qrCodes;

    public QRListAdapter(ArrayList<QRcode> userQrCodes) {
        this.qrCodes = userQrCodes;
    }


    @NonNull
    @Override
    public QRListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_qr, parent, false);
        return new QRListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QRListViewHolder holder, int position) {
        // Get the selected qr code
        QRcode selected_qr = qrCodes.get(position);

        // Set the qr code information
        holder.qrCode_name.setText(selected_qr.getName());
        holder.qrCode_score.setText(String.valueOf(selected_qr.getPoints()));
        selected_qr.loadImage(holder.qrCode_VR);

        // Click lister for the card view to open a fragment with the qr code full information
        holder.qrCode_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open a fragment with qr code information
                FragmentManager fragmentManager = ((MainActivity) v.getContext()).getSupportFragmentManager();
                // send the qr code hash to the fragment so that we can retrieve the qr code from the database
                Bundle data = new Bundle();
                data.putString("QR Hash", selected_qr.getHash());
                fragmentManager.setFragmentResult("Hash", data);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                IndividualQRFragment newFragment = new IndividualQRFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return qrCodes.size();
    }

    public class QRListViewHolder extends RecyclerView.ViewHolder {
        public TextView qrCode_name;
        public TextView qrCode_score;
        public ImageView qrCode_VR;
        public CardView qrCode_card;

        public QRListViewHolder(@NonNull View itemView) {
            super(itemView);
            qrCode_name = itemView.findViewById(R.id.qr_list_name_placeholder);
            qrCode_score = itemView.findViewById(R.id.qr_list_score_placeholder);
            qrCode_VR = itemView.findViewById(R.id.qr_list_image);
            qrCode_card = itemView.findViewById(R.id.carView);
        }
    }
}