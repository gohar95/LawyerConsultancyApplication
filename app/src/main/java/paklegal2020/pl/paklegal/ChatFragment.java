package paklegal2020.pl.paklegal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import paklegal2020.pl.paklegal.Adapters.UserAdapter;
import paklegal2020.pl.paklegal.Models.LawyerModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    FirebaseAuth auth;
    RecyclerView recyclerView;
    UserAdapter adapter;

    FirebaseUser fUser;
    DatabaseReference reference;

    private List<String> userList;
    private List<LawyerModel> uList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = itemView.findViewById(R.id.allChatsRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();

        userList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String message = snapshot.child("message").getValue().toString();
                    String receiver = snapshot.child("receiver").getValue().toString();
                    String sender = snapshot.child("sender").getValue().toString();

                    if(sender.equals(fUser.getUid()))
                        userList.add(receiver);
                    if(receiver.equals(fUser.getUid()))
                        userList.add(sender);
                }

                readChats();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return itemView;
    }

    private void readChats() {
        uList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Client");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uList.clear();
                for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                    String name = snapshot1.child("name").getValue().toString();
                    String imgURL = snapshot1.child("imgURL").getValue().toString();
                    String dateTime = snapshot1.child("dateTime").getValue().toString();
                    String address = snapshot1.child("address").getValue().toString();
                    String email = snapshot1.child("email").getValue().toString();
                    String password = snapshot1.child("password").getValue().toString();
                    String phone = snapshot1.child("phone").getValue().toString();
                    String status = snapshot1.child("status").getValue().toString();
                    String token = snapshot1.child("token").getValue().toString();

                    for(String id : userList){
                        if(snapshot1.getKey().equals(id)){
                            if(uList.size() != 0){
                                for(LawyerModel model : uList){
                                    if(!snapshot1.getKey().equals(model.getKey())){
                                        uList.add(new LawyerModel(snapshot1.getKey(),token,name,email,password,phone,address,status,imgURL,dateTime));
                                    }
                                }
                            }
                            else{
                                uList.add(new LawyerModel(snapshot1.getKey(),token,name,email,password,phone,address,status,imgURL,dateTime));
                            }
                        }
                    }

                }

                adapter = new UserAdapter(getContext(),uList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}