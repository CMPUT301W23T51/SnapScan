package com.example.SnapScan.ui.profile;

import static com.example.SnapScan.MainActivity.USER_ID;

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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SnapScan.R;
import com.example.SnapScan.model.QRcode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;


public class QRListFragment extends Fragment {
    public static ArrayList<QRcode> userQrList = new ArrayList<>();
    public static boolean dataLoaded;
    protected RecyclerView mRecyclerView;
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_qr_list, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView_qr_list);
        bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);

        // How to hide the bottom navigation bar when scrolling
        // https://stackoverflow.com/questions/44777869/hide-show-bottomnavigationview-on-scroll
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                // hide the bottom navigation bar when scrolling
//                if ((dy != 0) && bottomNavigationView.isShown()) {
//                    bottomNavigationView.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE)
//                    // set the bottom navigation bar to visible after scrolling has stopped
//                    // Display the bottom navigation bar when scrolling has stopped
//                    bottomNavigationView.setVisibility(View.VISIBLE);
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });
        ProgressBar progressBar = view.findViewById(R.id.progressBar_qr_list);
        if (dataLoaded) {
            QRListAdapter qrListAdapter = new QRListAdapter(userQrList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(qrListAdapter);
            progressBar.setVisibility(View.INVISIBLE);
            // Set the swipe controller
            setupSwipeController();
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

    private void setupSwipeController() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            //Set Boundaries for the swipe
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.6f;
            }



            /**
             * This method is called when the user swipes an QR code , it removes the QR code from the list
             * and the database
             * @param viewHolder The view holder of the item that was swiped
             * @param direction The direction of the swipe
             */
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Get the position of the item that was swiped
                int position = viewHolder.getAdapterPosition();
                // Get the QR code that was swiped
                QRcode qrCode = userQrList.get(position);
                // Remove the QR code from the list
                userQrList.remove(position);

                // Notify the adapter that the item was removed
                mRecyclerView.getAdapter().notifyItemRemoved(position);
                Snackbar undoOption = Snackbar.make(mRecyclerView, "QR code " + qrCode.getName() + " Deleted", Snackbar.LENGTH_LONG);
                undoOption
                        .setTextColor(getResources().getColor(R.color.ComplementaryBlueLight))
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Add the QR code back to the list
                                userQrList.add(position, qrCode);
                                // Notify the adapter that the item was added
                                mRecyclerView.getAdapter().notifyItemInserted(position);
                            }
                        }).show();

                // Delete the QR code from the database
                undoOption.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (event != DISMISS_EVENT_ACTION) {
                            // Delete the QR code from the database
                            CollectionReference collectionReference = db.collection("users").document(USER_ID).collection("Scanned QRs");
                            collectionReference.document(qrCode.getHash()).delete();
                        }
                    }
                });
            }

        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onDestroyView() {
        // Display the bottom navigation bar when the fragment is destroyed
        bottomNavigationView.setVisibility(View.VISIBLE);
        super.onDestroyView();
    }

}
