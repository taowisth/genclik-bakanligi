package tr.gov.gsb.bakanlik.hackathon;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HediyeActivity extends AppCompatActivity {



    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler = new Handler();

    ListView listView;
    GiftPostClass adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ArrayList<String> useremailFromFB;
    ArrayList<String> usercommentFromFB;
    ArrayList<String> userimageFromFB;
    ArrayList<String> usercodecommentFromFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        useremailFromFB = new ArrayList<String>();
        usercommentFromFB = new ArrayList<String>();
        usercodecommentFromFB = new ArrayList<String>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new GiftPostClass(useremailFromFB, usercommentFromFB, usercodecommentFromFB, this);
        listView.setAdapter(adapter);

        getDataFromFirebase();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = user.getDisplayName();
        final int result = Integer.parseInt(name);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);
        // Start long running operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus = result;
                    // Update the progress  bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            textView.setText(result + "/" + progressBar.getMax() + "XP");
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void startFavoriteAct (View view){
        Intent intent = new Intent(getApplicationContext(),FavoritesActivity.class);
        startActivity(intent);

    }
    public void startLeaguesAct (View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }
    public void startMessageAct (View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }

    public void startLeftActivity(View view){
        Intent intent = new Intent(getApplicationContext(),SolManuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
    }

    public void getDataFromFirebase() {

        DatabaseReference newReference = firebaseDatabase.getReference("Gifts");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //System.out.println("FBV children: " + dataSnapshot.getChildren() );
                //System.out.println("FBV key: " + dataSnapshot.getKey() );
                //System.out.println("FBV value: " + dataSnapshot.getValue() );
                //System.out.println("FBV priority: " + dataSnapshot.getPriority() );

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    //System.out.println("FBV ds value: " + ds.getValue());

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    useremailFromFB.add(hashMap.get("useremail"));
                    usercommentFromFB.add(hashMap.get("comment"));
                    usercodecommentFromFB.add(hashMap.get("codecomment"));
                    adapter.notifyDataSetChanged();
                    String useremail = hashMap.get("useremail");


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }
}