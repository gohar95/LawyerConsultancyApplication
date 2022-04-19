package paklegal2020.pl.paklegal.ClientSide;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import paklegal2020.pl.paklegal.Adapters.LawyersAdapter;
import paklegal2020.pl.paklegal.Models.QualificationModel;
import paklegal2020.pl.paklegal.R;

public class ShowLawyers extends AppCompatActivity {

    String TITLE = "";
    RecyclerView recyclerView;
    LawyersAdapter adapter;
    ArrayList<QualificationModel> list;
    LinearLayoutManager layoutManager;

    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lawyers);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            TITLE = bundle.getString("TITLE");
        }

        search = findViewById(R.id.search_lawyer);
        recyclerView = findViewById(R.id.lawyer_recycle);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        FETCH_USER_DETAILS();

    }

    private void filter(String s) {
        if(s.isEmpty()){
            adapter.filterList(list);
        }
        else{
            ArrayList<QualificationModel> filteredList = new ArrayList<>();
            for(QualificationModel item: list){
                if(item.getChamberCity().toLowerCase().contains(s.toLowerCase())){
                    filteredList.add(item);
                }
                if(item.getLawyerCategory().toLowerCase().contains(s.toLowerCase())){
                    filteredList.add(item);
                }
            }
            adapter.filterList(filteredList);
        }
    }


    private void FETCH_USER_DETAILS() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("LawyerQualification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    list.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String Category = snapshot.child("lawyerCategory").getValue().toString();
                        String[] CategoryList = Category.split(",");
                        for(String name : CategoryList){
                            if(name.equals(TITLE)){
                                //String Category = snapshot.child("lawyerCategory").getValue().toString();
                                String LawyerEducation = snapshot.child("lawyerEducation").getValue().toString();
                                String LawyerCNIC = snapshot.child("lawyerCNIC").getValue().toString();
                                String LawyerLicence = snapshot.child("lawyerLicence").getValue().toString();
                                String LawyerLowerCrt = snapshot.child("lawyerLowerCrt").getValue().toString();
                                String LawyerHigherCrt = snapshot.child("lawyerHigherCrt").getValue().toString();
                                String VoterLowerCrt = snapshot.child("voterLowerCrt").getValue().toString();
                                String VoterHigherCrt = snapshot.child("voterHigherCrt").getValue().toString();
                                String LawyerLanguage = snapshot.child("lawyerLanguage").getValue().toString();
                                String ChamberCity = snapshot.child("chamberCity").getValue().toString();
                                String LawyerSpeciality = snapshot.child("lawyerSpeciality").getValue().toString();
                                String LawerImage = snapshot.child("lawerImage").getValue().toString();
                                String LawyerName = snapshot.child("lawyerName").getValue().toString();
                                String LawyerPhone = snapshot.child("lawyerPhone").getValue().toString();
                                String LawyerDate = snapshot.child("lawyerDate").getValue().toString();
                                String LawyerRating = snapshot.child("lawyerRating").getValue().toString();

                                list.add(new QualificationModel(snapshot.getKey(),LawyerEducation,LawyerCNIC,LawyerLicence,LawyerLowerCrt,LawyerHigherCrt,VoterLowerCrt,VoterHigherCrt,LawyerLanguage,Category,ChamberCity,LawyerSpeciality
                                ,LawerImage,LawyerName,LawyerPhone,LawyerDate,LawyerRating));

                            }
                        }

                    }

                    adapter = new LawyersAdapter(ShowLawyers.this,list);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(ShowLawyers.this, "No Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ShowLawyers.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
