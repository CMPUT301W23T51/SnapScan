package com.example.SnapScan.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.SnapScan.R;
import com.example.SnapScan.databinding.FragmentProfileBinding;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A fragment to display user profile information, including a button to navigate to the user's list of QR codes
 * and a button to open the leaderboard.
 *
 */


public class ProfileFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView player_qrCount = binding.qrCountInt;
        TextView player_totalPoints = binding.totalPointsInt;

        // Open the QRListFragment when the button is clicked
        Button MyQrsButton = binding.myQrButton;
        MyQrsButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, new QRListFragment());
            // Allow use to go back to profile when back button is pressed
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        // opens the leaderboard
        Button Leaderboard_button = binding.getRoot().findViewById(R.id.leaderboard_button);
        Leaderboard_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Leaderboard.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
