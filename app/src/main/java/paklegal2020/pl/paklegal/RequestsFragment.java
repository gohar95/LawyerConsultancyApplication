package paklegal2020.pl.paklegal;

import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import paklegal2020.pl.paklegal.Adapters.AppointAdapter;
import paklegal2020.pl.paklegal.Adapters.RequestAdapter;
import paklegal2020.pl.paklegal.ClientSide.AppointmentStat;
import paklegal2020.pl.paklegal.Models.AppointmentModel;
import paklegal2020.pl.paklegal.Models.RequestModel;

public class RequestsFragment extends Fragment {

    RecyclerView recyclerView;
    RequestAdapter adapter;
    ArrayList<RequestModel> list;
    LinearLayoutManager layoutManager;

    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_requests, container, false);

        recyclerView = itemView.findViewById(R.id.requestRecycler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();

        auth = FirebaseAuth.getInstance();

        FETCH_REQUEST_DETAILS();

        return itemView;
    }
    private void FETCH_REQUEST_DETAILS() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("CaseRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    list.clear();
                    String key = auth.getCurrentUser().getUid();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String lawyerKey = snapshot.child("lawyerKey").getValue().toString();
                        String status = snapshot.child("status").getValue().toString();
                        if(key.equals(lawyerKey) && "Pending".equals(status)){

                            String clientKey = snapshot.child("clientKey").getValue().toString();
                            String caseKey = snapshot.child("caseKey").getValue().toString();
                            String dateTime = snapshot.child("dateTime").getValue().toString();
                            String email = snapshot.child("email").getValue().toString();
                            String image = snapshot.child("image").getValue().toString();
                            String phone = snapshot.child("phone").getValue().toString();
                            String name = snapshot.child("name").getValue().toString();
                            String rcase = snapshot.child("rcase").getValue().toString();

                            list.add(new RequestModel(caseKey,name,email,rcase,image,phone,clientKey,lawyerKey,dateTime,status));
                        }
                    }

                    adapter = new RequestAdapter(getContext(),list);
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