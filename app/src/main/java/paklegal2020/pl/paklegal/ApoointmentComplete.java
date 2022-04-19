package paklegal2020.pl.paklegal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import paklegal2020.pl.paklegal.Models.ReviewModel;
import paklegal2020.pl.paklegal.SendNotificationPack.APIService;
import paklegal2020.pl.paklegal.SendNotificationPack.Client;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ApoointmentComplete extends AppCompatActivity {

    String LKEY = "";
    String clientID = "";
    String appintKey = "";

    EditText ratingText;
    RatingBar ratingBar;
    Button reviewDone;

    ProgressDialog progressDialog;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apoointment_complete);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            LKEY = bundle.getString("LKEY");
            clientID = bundle.getString("clientID");
            appintKey = bundle.getString("appintKey");
        }

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        ratingText = findViewById(R.id.ratingText);
        ratingBar = findViewById(R.id.ratingBar);
        reviewDone = findViewById(R.id.reviewDone);
        progressDialog = new ProgressDialog(this);

        reviewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strReview = ratingText.getText().toString().trim();
                float rating = ratingBar.getRating();
                if(strReview.isEmpty())
                    ratingText.setError("Enter Comment here");
                else if(rating == 0)
                    Toast.makeText(ApoointmentComplete.this, "Please Give Rating First", Toast.LENGTH_SHORT).show();
                else{
                    PublishReviews(Float.toString(rating), strReview);
                }
            }
        });
    }

    private void PublishReviews(final String rating, String strReview) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Publishing Your Review...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("LawyerQualification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = auth.getCurrentUser().getUid();
                        if(key.equals(snapshot.getKey())) {
                            String rate = snapshot.child("lawyerRating").getValue().toString();
                            float rr = Float.parseFloat(rate);
                            float arr = Float.parseFloat(rating);

                            float answer = (rr+arr)/2.0f;
                            final String strRate = String.valueOf(answer);

                            ReviewModel values = new ReviewModel(strRate, rating, appintKey, clientID, LKEY);
                            FirebaseDatabase.getInstance().getReference("Reviews").child(appintKey).setValue(values)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("LawyerQualification").child(auth.getCurrentUser().getUid());
                                                Map<String, Object> updates = new HashMap<String,Object>();
                                                updates.put("lawyerRating", strRate);
                                                ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(ApoointmentComplete.this, "Done", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                        else {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(ApoointmentComplete.this, "Failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            }else{
                                                progressDialog.dismiss();
                                                Toast.makeText(ApoointmentComplete.this, "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            progressDialog.dismiss();
                        }
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(ApoointmentComplete.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}