package com.example.SnapScan.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.SnapScan.R;

import androidx.fragment.app.FragmentTransaction;

import com.example.SnapScan.databinding.FragmentProfileBinding;
import com.example.SnapScan.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
/**

 A fragment to display user profile information, including a button to navigate to the user's list of QR codes

 and a button to open the leaderboard.
 */

public class ProfileFragment extends Fragment {

    public static String username;

    private FragmentProfileBinding binding;
    /**

     Inflates the fragment's layout, sets up button click listeners, and returns the root view.

     @param inflater The LayoutInflater object that can be used to inflate any views in the fragment

     @param container The parent view that the fragment UI should be attached to

     @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here

     @return The root view of the fragment's layout
     */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Open the QRListFragment when the button is clicked
        Button MyQrsButton = binding.myQrButton;
        MyQrsButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
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
                Intent intent = new Intent(getContext(),leaderboard.class);
                startActivity(intent);
            }
        });

        // Trying to implement the logout button
        /*
        Button logout = binding.getRoot().findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(getContext(), LoginActivity.class);
                startActivity(login_intent);
            }
        });

         */

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
