package paklegal2020.pl.paklegal;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import de.hdodenhof.circleimageview.CircleImageView;
import paklegal2020.pl.paklegal.databinding.ActivityDashboardBinding;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        FirebaseAuth auth;
        CircleImageView dImage;
        TextView dName,dEmail;
        SharedPreferences shared, shared1;

        LinearLayout requests,appoint,chattt;

        ActivityDashboardBinding binding;

        TextView rtext,atext,ctext;
        ImageView rimage,aimage,cimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dashboard);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        shared = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);
        shared1 = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);

        requests = findViewById(R.id.requests);
        rtext = findViewById(R.id.rtext);
        rimage = findViewById(R.id.rimage);

        appoint = findViewById(R.id.appoint);
        atext = findViewById(R.id.atext);
        aimage = findViewById(R.id.aimage);

        chattt = findViewById(R.id.chattt);
        ctext = findViewById(R.id.ctext);
        cimage = findViewById(R.id.cimage);

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

        RequestsFragment userFragment = new RequestsFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.replacingFragment,userFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        rtext.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.colorAccent));
        rimage.setColorFilter(ContextCompat.getColor(Dashboard.this, R.color.colorAccent));
        atext.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
        aimage.setColorFilter(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
        ctext.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
        cimage.setColorFilter(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));

        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestsFragment userFragment = new RequestsFragment();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.replacingFragment,userFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                rtext.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.colorAccent));
                rimage.setColorFilter(ContextCompat.getColor(Dashboard.this, R.color.colorAccent));
                atext.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
                aimage.setColorFilter(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
                ctext.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
                cimage.setColorFilter(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
            }
        });
        appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppointFragment userFragment = new AppointFragment();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.replacingFragment,userFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                rtext.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
                rimage.setColorFilter(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
                atext.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.colorAccent));
                aimage.setColorFilter(ContextCompat.getColor(Dashboard.this, R.color.colorAccent));
                ctext.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
                cimage.setColorFilter(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
            }
        });
        chattt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatFragment userFragment = new ChatFragment();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.replacingFragment,userFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                rtext.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
                rimage.setColorFilter(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
                atext.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
                aimage.setColorFilter(ContextCompat.getColor(Dashboard.this, R.color.colorWhite));
                ctext.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.colorAccent));
                cimage.setColorFilter(ContextCompat.getColor(Dashboard.this, R.color.colorAccent));
            }
        });
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

                            SharedPreferences.Editor editor = shared.edit();
                            editor.putString("KEY", key);
                            editor.putString("NAME", name);
                            editor.putString("EMAIL", email);
                            editor.putString("PASS", password);
                            editor.putString("PHONE", phone);
                            editor.putString("ADDRESS", address);
                            editor.putString("IMAGE", imgURL);
                            editor.apply();

                            dName.setText(name);
                            dEmail.setText(email);
                            new DownloadImageFromInternet(dImage).execute(imgURL);
                        }
                    }
                }
                else{
                    Toast.makeText(Dashboard.this, "No Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Dashboard.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateToken(){
        String Token = FirebaseInstanceId.getInstance().getToken();
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Lawyer").child(auth.getCurrentUser().getUid());
        Map<String, Object> updates = new HashMap<String,Object>();
        updates.put("token", Token);
        ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                }
                else {
                }
            }
        });
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        CircleImageView imageView;

        public DownloadImageFromInternet(CircleImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bimage;
        }

        @SuppressLint("WrongThread")
        protected void onPostExecute(Bitmap result) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encoded = Base64.encodeToString(b, Base64.DEFAULT);
            SharedPreferences.Editor editor = shared.edit();
            editor.putString("IMGURL", encoded);
            editor.apply();
            //imageView.setImageBitmap(result);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Profile) {
            startActivity(new Intent(Dashboard.this,Profile.class));
        } else if (id == R.id.nav_addBio) {
            startActivity(new Intent(Dashboard.this,Qualification.class));
        } else if (id == R.id.nav_viewRequest) {
            RequestsFragment userFragment = new RequestsFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.replacingFragment,userFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_topratedlawyer) {
            startActivity(new Intent(Dashboard.this,ViewQualification.class));
        }   else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor = shared.edit();
            SharedPreferences.Editor editor1 = shared1.edit();
            editor.clear();
            editor1.clear();
            editor.apply();
            editor1.apply();
            auth.signOut();
            startActivity(new Intent(Dashboard.this,LoginActivity.class));
            finishAffinity();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
