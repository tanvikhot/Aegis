package com.example.aegis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.aegis.data.UserLocation;
import com.example.aegis.data.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("userprofile");
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public void saveProfile(View view) {
        String gender = "Female";
        TextView textView = (TextView) findViewById(R.id.name);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.genderRadio);
        if (radioGroup.getCheckedRadioButtonId() == R.id.maleRadio) {
            gender = "Male";
        }

        dbref.child(currentUser.getUid()).setValue(new UserProfile(textView.getText().toString(), gender));
        finish();
    }

    //UI should return to MainActivity login screen
    public void updateUI() {
        startActivity(new Intent(ProfileActivity.this, MapsActivity.class));
    }
}
