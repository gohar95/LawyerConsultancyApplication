package paklegal2020.pl.paklegal.ClientSide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import paklegal2020.pl.paklegal.Adapters.AppointStatAdapter;
import paklegal2020.pl.paklegal.Models.AppointmentModel;
import paklegal2020.pl.paklegal.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppointmentStat extends AppCompatActivity {

    RecyclerView recyclerView;
    AppointStatAdapter adapter;
    ArrayList<AppointmentModel> list;
    LinearLayoutManager layoutManager;

    Boolean b= false;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_stat);

        recyclerView = findViewById(R.id.appointRecycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();

        auth = FirebaseAuth.getInstance();

        FETCH_APPOINT_DETAILS();
    }

    private void FETCH_APPOINT_DETAILS() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Appointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    list.clear();
                    for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
                        final String key = auth.getCurrentUser().getUid();
                        String key2 = snapshot.getKey();
                        final String clientID = snapshot.child("clientID").getValue().toString();
                        if(key.equals(clientID)){

                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                            reference1.child("CaseRequests").child(key2).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot !=null){
                                        String status = dataSnapshot.child("status").getValue().toString();
                                        if(status.equals("Appointment Set")){
                                            b=true;
                                        }
                                        else
                                            b=false;
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(AppointmentStat.this, "No Data", Toast.LENGTH_SHORT).show();
                                }
                            });

                            if(!b){
                                String lawyerName = snapshot.child("lawyerName").getValue().toString();
                                String lawyerPhone = snapshot.child("lawyerPhone").getValue().toString();
                                String caseTitle = snapshot.child("caseTitle").getValue().toString();
                                String dateTime = snapshot.child("dateTime").getValue().toString();
                                String lawyerImage = snapshot.child("lawyerImage").getValue().toString();
                                String lawyerID = snapshot.child("lawyerID").getValue().toString();

                                list.add(new AppointmentModel(snapshot.getKey(),lawyerName,lawyerPhone,caseTitle,dateTime,lawyerImage,clientID,lawyerID));
                            }

                        }
                    }



                    adapter = new AppointStatAdapter(AppointmentStat.this,list);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(AppointmentStat.this, "No Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AppointmentStat.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}