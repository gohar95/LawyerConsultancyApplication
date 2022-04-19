package paklegal2020.pl.paklegal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import paklegal2020.pl.paklegal.Adapters.AppointAdapter;
import paklegal2020.pl.paklegal.ClientSide.AppointmentStat;
import paklegal2020.pl.paklegal.Models.AppointmentModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AppointFragment extends Fragment {

    RecyclerView recyclerView;
    AppointAdapter adapter;
    ArrayList<AppointmentModel> list;
    LinearLayoutManager layoutManager;

    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_appoint, container, false);

        recyclerView = v.findViewById(R.id.appointRecycler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();

        auth = FirebaseAuth.getInstance();

        FETCH_APPOINT_DETAILS();

        return v;
    }

    private void FETCH_APPOINT_DETAILS() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Appointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    list.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String key = auth.getCurrentUser().getUid();
                        String lawyerID = snapshot.child("lawyerID").getValue().toString();
                        if(key.equals(lawyerID)){
                            String lawyerName = snapshot.child("lawyerName").getValue().toString();
                            String lawyerPhone = snapshot.child("lawyerPhone").getValue().toString();
                            String caseTitle = snapshot.child("caseTitle").getValue().toString();
                            String dateTime = snapshot.child("dateTime").getValue().toString();
                            String lawyerImage = snapshot.child("lawyerImage").getValue().toString();
                            String clientID = snapshot.child("clientID").getValue().toString();

                            list.add(new AppointmentModel(snapshot.getKey(),lawyerName,lawyerPhone,caseTitle,dateTime,lawyerImage,clientID,lawyerID));
                        }
                    }

                    adapter = new AppointAdapter(getContext(),list);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }


}