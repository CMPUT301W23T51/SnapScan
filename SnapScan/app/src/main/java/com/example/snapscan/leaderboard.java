package com.example.snapscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class leaderboard extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "Leaderboard";
    private RecyclerView mRecyclerView;
    private ScoreAdapter mAdapter;
    private List<Score> mScoresList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button mSearchButton;
    private EditText mSearchEditText;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Find the RecyclerView in the layout file and set its layout manager and adapter
        mRecyclerView = findViewById(R.id.Recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ScoreAdapter((ArrayList<Score>) mScoresList, new ScoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String playerName = mScoresList.get(position).getName();
                Intent intent = new Intent(leaderboard.this, OtherProfile.class);
                intent.putExtra("username", playerName);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

//         Retrieve scores data from Firestore and rank in ascending order
        db.collection("QR")
                .orderBy("Points", Query.Direction.ASCENDING) // Sort by "points" field in ascending order
                .get() // Get the query results
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Convert each document to a Score object and add it to the list
                                Score score = new Score(
                                        document.getString("Name"),
                                        document.getString("Points"));
                                mScoresList.add(score);
                            }
                            // Sort the scores list in ascending order based on the "Points" attribute
                            Collections.sort(mScoresList, new Comparator<Score>() {
                                @Override
                                public int compare(Score score1, Score score2) {
                                    return (int) (score1.getPoints() - score2.getPoints());
                                }
                            });
                            mAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        // Find the search EditText and search button
        EditText searchEditText = findViewById(R.id.search_edit_text);
        Button searchButton = findViewById(R.id.search_button);
        // Set an OnClickListener on the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the search query from the EditText
                String searchQuery = searchEditText.getText().toString().trim();
                // Query the Firestore collection for documents where the "Name" field matches the search query
                db.collection("QR")
                        .whereEqualTo("Name", searchQuery)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    // Clear the current scores list
                                    mScoresList.clear();

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Convert each document to a Score object and add it to the list
                                        Score score = new Score(
                                                document.getString("Name"),
                                                document.getString("Points"));
                                        mScoresList.add(score);
                                    }
                                    // Sort the scores list in ascending order based on the "Points" attribute
                                    Collections.sort(mScoresList, new Comparator<Score>() {
                                        @Override
                                        public int compare(Score score1, Score score2) {
                                            return (int) (score1.getPoints() - score2.getPoints());
                                        }
                                    });

                                    // Notify the adapter that the data has changed
                                    mAdapter.notifyDataSetChanged();

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leaderboard, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Perform search when user submits the query
        mSearchButton.performClick();
        return false;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Not used
        return false;
    }

}
