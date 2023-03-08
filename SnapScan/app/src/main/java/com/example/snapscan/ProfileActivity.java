package com.example.snapscan;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get the player object from the intent
        Player player = (Player) getIntent().getSerializableExtra("player");

        // Access the player's profile data
        String username = player.getUsername();
        String name = player.getName();
        int qrcodes = player.getQrcodes();
        int points = player.getPoints();

        // Update the UI
        TextView usernameTextView = findViewById(R.id.player_name);
        usernameTextView.setText(username);

        TextView qrcodesTextView = findViewById(R.id.qrcode_count);
        qrcodesTextView.setText(String.valueOf(qrcodes));

        TextView pointsTextView = findViewById(R.id.points_count);
        pointsTextView.setText(String.valueOf(points));
    }
}
