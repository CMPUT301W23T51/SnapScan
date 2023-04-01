package com.example.SnapScan.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SnapScan.R;
import com.example.SnapScan.model.QRcode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;


public class QRListFragment extends Fragment {
    public static ArrayList<QRcode> userQrList = new ArrayList<>();
    public static boolean dataLoaded;
    protected RecyclerView mRecyclerView;
    FirebaseFirestore db;
    BottomNavigationView bottomNavigationView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_qr_list, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView_qr_list);
        bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // hide the bottom navigation bar when scrolling
                if ((dy != 0) && bottomNavigationView.isShown()) {
                    bottomNavigationView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    // set the bottom navigation bar to visible after scrolling has stopped
                    // Display the bottom navigation bar when scrolling has stopped
                    bottomNavigationView.setVisibility(View.VISIBLE);
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        ProgressBar progressBar = view.findViewById(R.id.progressBar_qr_list);
        if (dataLoaded) {
            QRListAdapter qrListAdapter = new QRListAdapter(userQrList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(qrListAdapter);
            progressBar.setVisibility(View.INVISIBLE);
            return view;
        } else {
            // Chances of this code being executed are infinitesimally low as the data is loaded
            // in the MainActivity to prevent this code from being executed
            // Make the progress bar visible while the data is being loaded
            progressBar.setVisibility(View.VISIBLE);
            // Make a toast to tell the user that the data is being loaded
            Toast.makeText(getContext(), "Please wait a few nanoseconds and try again", Toast.LENGTH_SHORT).show();
            // Go back to Profile fragment as the data has not been loaded
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_profile);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
