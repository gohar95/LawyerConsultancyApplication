package paklegal2020.pl.paklegal.ClientSide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import paklegal2020.pl.paklegal.Adapters.LawyersAdapter;
import paklegal2020.pl.paklegal.Adapters.SpecialAdapter;
import paklegal2020.pl.paklegal.LoginActivity;
import paklegal2020.pl.paklegal.Models.QualificationModel;
import paklegal2020.pl.paklegal.Models.SpecialModel;
import paklegal2020.pl.paklegal.R;

public class ClientDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth auth;
    CircleImageView dImage;
    TextView dName,dEmail;
    SharedPreferences shared, shared1;

    RelativeLayout view_appoint;
    Button find_doc,cons_online;
    EditText search;
    RecyclerView recyclerView,lawyer_recycler;
    SpecialAdapter adapter;
    ArrayList<SpecialModel> list;
    LinearLayoutManager layoutManager;

    LawyersAdapter adapter1;
    ArrayList<QualificationModel> list1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.search_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();

        lawyer_recycler = findViewById(R.id.lawyer_recycler);
        lawyer_recycler.setLayoutManager(new LinearLayoutManager(this));
        list1 = new ArrayList<>();

        InsertDatainSearch();

        view_appoint = findViewById(R.id.view_appoint);
        search = findViewById(R.id.search);
        find_doc = findViewById(R.id.find_doc);
        cons_online = findViewById(R.id.cons_online);

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

        find_doc.setTextColor(getResources().getColorStateList(R.color.colorWhite));
        find_doc.setBackground(ContextCompat.getDrawable(ClientDashboard.this, R.drawable.shape1));
        cons_online.setTextColor(getResources().getColorStateList(R.color.colorBlack));
        cons_online.setBackground(ContextCompat.getDrawable(ClientDashboard.this, R.drawable.shape22));

        find_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find_doc.setTextColor(getResources().getColorStateList(R.color.colorWhite));
                find_doc.setBackground(ContextCompat.getDrawable(ClientDashboard.this, R.drawable.shape1));
                cons_online.setTextColor(getResources().getColorStateList(R.color.colorBlack));
                cons_online.setBackground(ContextCompat.getDrawable(ClientDashboard.this, R.drawable.shape22));

                list.clear();
                recyclerView.setVisibility(View.VISIBLE);
                lawyer_recycler.setVisibility(View.GONE);
                //Toast.makeText(ClientDashboard.this, recyclerView.getVisibility()+"\n"+lawyer_recycler.getVisibility(), Toast.LENGTH_SHORT).show();
                InsertDatainSearch();
                //Toast.makeText(ClientDashboard.this, "Find Doctor", Toast.LENGTH_SHORT).show();
            }
        });

        cons_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find_doc.setTextColor(getResources().getColorStateList(R.color.colorBlack));
                find_doc.setBackground(ContextCompat.getDrawable(ClientDashboard.this, R.drawable.shape11));
                cons_online.setTextColor(getResources().getColorStateList(R.color.colorWhite));
                cons_online.setBackground(ContextCompat.getDrawable(ClientDashboard.this, R.drawable.shape2));

                list1.clear();
                recyclerView.setVisibility(View.GONE);
                lawyer_recycler.setVisibility(View.VISIBLE);
                //Toast.makeText(ClientDashboard.this, recyclerView.getVisibility()+"\n"+lawyer_recycler.getVisibility(), Toast.LENGTH_SHORT).show();
                FETCH_LAWYER_DETAILS();
                //Toast.makeText(ClientDashboard.this, "Consult", Toast.LENGTH_SHORT).show();
            }
        });

        view_appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClientDashboard.this,AppointmentStat.class));
            }
        });

        auth = FirebaseAuth.getInstance();
        shared = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);
        shared1 = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);

        FETCH_USER_DETAILS();
        //UpdateToken();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        dImage = headerView.findViewById(R.id.drawerImage);
        dName = headerView.findViewById(R.id.drawerName);
        dEmail = headerView.findViewById(R.id.drawerEmail);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void filter(String s) {
        if(recyclerView.getVisibility() == View.VISIBLE){
            if(s.isEmpty()){
                adapter.filterList(list);
            }
            else{
                ArrayList<SpecialModel> filteredList = new ArrayList<>();
                for(SpecialModel item: list){
                    if(item.getTitle().toLowerCase().contains(s.toLowerCase())){
                        filteredList.add(item);
                    }
                }
                adapter.filterList(filteredList);
            }
        }
        else{
            if(s.isEmpty()){
                adapter1.filterList(list1);
            }
            else{
                ArrayList<QualificationModel> filteredList1 = new ArrayList<>();
                for(QualificationModel item: list1){
                    if(item.getChamberCity().toLowerCase().contains(s.toLowerCase())){
                        filteredList1.add(item);
                    }
                    if(item.getLawyerCategory().toLowerCase().contains(s.toLowerCase())){
                        filteredList1.add(item);
                    }
                }
                adapter1.filterList(filteredList1);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void InsertDatainSearch() {
        list.add(new SpecialModel("Government Lawyers","Here, Government Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Private Sector Lawyers","Here, Private Sector Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Trial Lawyers","Here, Trial Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Immigration Lawyers","Here, Immigration Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Estate Planning Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Personal Injury Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Toxic Tort Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Public Interest Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Civil Rights Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Criminal Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Entertainment Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Real Estate Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Digital Media Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Legal Business Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Family Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Property Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Medical Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Administrative Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Authorative Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Aerospace and Aviation Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Arbitration Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Asset Search and Investigation Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Attestation and Legalization Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Military Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Drug Research Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Tax Management Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        list.add(new SpecialModel("Degital Media Lawyers","Here, Estate Planning Lawyers are available!", BitmapFactory.decodeResource(getResources(), R.drawable.lawyer_icons)));
        adapter = new SpecialAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void FETCH_USER_DETAILS() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(shared1.getString("STATUS","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    String KEY = auth.getCurrentUser().getUid().toString();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String key = snapshot.getKey().toString();
                        if(key.equals(KEY)){
                            String name = snapshot.child("name").getValue().toString();
                            String phone = snapshot.child("phone").getValue().toString();
                            String email = snapshot.child("email").getValue().toString();
                            String password = snapshot.child("password").getValue().toString();
                            String address = snapshot.child("address").getValue().toString();
                            String imgURL = snapshot.child("imgURL").getValue().toString();
                            String dandt = snapshot.child("dateTime").getValue().toString();

                            SharedPreferences.Editor editor = shared.edit();
                            editor.putString("KEY", key);
                            editor.putString("NAME", name);
                            editor.putString("EMAIL", email);
                            editor.putString("PASS", password);
                            editor.putString("PHONE", phone);
                            editor.putString("ADDRESS", address);
                            editor.putString("IMAGE", imgURL);
                            editor.putString("DATE", dandt);
                            editor.apply();

                            dName.setText(name);
                            dEmail.setText(email);
                        }
                    }
                }
                else{
                    Toast.makeText(ClientDashboard.this, "No Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ClientDashboard.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void FETCH_LAWYER_DETAILS() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("LawyerQualification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list1.clear();
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                String LawyerEducation = snapshot.child("lawyerEducation").getValue().toString();
                                String Category = snapshot.child("lawyerCategory").getValue().toString();
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

                                list1.add(new QualificationModel(snapshot.getKey(),LawyerEducation,LawyerCNIC,LawyerLicence,LawyerLowerCrt,LawyerHigherCrt,VoterLowerCrt,VoterHigherCrt,LawyerLanguage,Category,ChamberCity,LawyerSpeciality
                                        ,LawerImage,LawyerName,LawyerPhone,LawyerDate,LawyerRating));

                    }

                    adapter1 = new LawyersAdapter(ClientDashboard.this,list1);
                    lawyer_recycler.setAdapter(adapter1);
                }
                else{
                    Toast.makeText(ClientDashboard.this, "No Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ClientDashboard.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notice) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_appStats) {
            startActivity(new Intent(ClientDashboard.this,AppointmentStat.class));
        }   else if (id == R.id.nav_log) {
            SharedPreferences.Editor editor = shared.edit();
            SharedPreferences.Editor editor1 = shared1.edit();
            editor.clear();
            editor1.clear();
            editor.apply();
            editor1.apply();
            auth.signOut();
            startActivity(new Intent(ClientDashboard.this,LoginActivity.class));
            finishAffinity();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
