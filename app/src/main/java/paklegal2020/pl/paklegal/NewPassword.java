package paklegal2020.pl.paklegal;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NewPassword extends AppCompatActivity {

    Switch select_login;

    EditText p1,p2;
    Button b;
    String KEY = "";
    int ID ;
    String EMAIL = "";
    ProgressDialog p;
    String STATUS = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        Bundle bb =  getIntent().getExtras();
        if(bb != null){
            EMAIL = bb.getString("EMAIL");
            STATUS = bb.getString("STATUS");
        }

        p1 = findViewById(R.id.passPassword);
        p2 = findViewById(R.id.passCPassword);
        b = findViewById(R.id.passButton);

        select_login = findViewById(R.id.select_login);
        p = new ProgressDialog(this);
        select_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    select_login.setText("Users");
                }
                else{
                        select_login.setText("Lawyer");
                }

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(p1.getText().toString().trim()))
                    p1.setError("This Field is Required");
                else if(TextUtils.isEmpty(p2.getText().toString().trim()))
                    p2.setError("This Field is Required");
                else if((p1.getText().toString().trim()).equals(p2.getText().toString().trim())){
                    String newPassword = p1.getText().toString().trim();
                    if((select_login.getText().toString()).equals("Users"))
                        getKey(newPassword);
                    else{
                        getKeyLawyer(newPassword);
                    }
                }
                else
                    p2.setError("Not Matched");
            }
        });
    }

    public void getKey(final String newPassword){
        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Fetching Key...");
        p.show();
        p.setCancelable(false);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String email = snapshot.child("email").getValue().toString();

                    if(email.equals(EMAIL)){
                        p.dismiss();
                        String key = dataSnapshot.getKey();
                        UpdateinFirebase(newPassword,key,"Users");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                p.dismiss();
            }
        });
    }


    public void getKeyLawyer(final String newPassword){
        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Fetching Key...");
        p.show();
        p.setCancelable(false);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Lawyer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String email = snapshot.child("email").getValue().toString();

                    if(email.equals(EMAIL)){
                        p.dismiss();
                        String key = dataSnapshot.getKey();
                        UpdateinFirebase(newPassword,key,"Lawyer");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                p.dismiss();
            }
        });
    }


    private void UpdateinFirebase(final String newPassword, final String key, final String table) {

        p.setMessage("Updating...");
        p.show();
        p.setCancelable(false);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(table).child(key);
        Map<String, Object> updates = new HashMap<String,Object>();
        updates.put("password", newPassword);
        ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    updateIbAuth(newPassword , key);
                }
                else {
                    Toast.makeText(NewPassword.this, "Failed", Toast.LENGTH_SHORT).show();
                    p.dismiss();
                }
            }

            private void updateIbAuth(String newPassword, String key) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            p.dismiss();
                            Toast.makeText(NewPassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(NewPassword.this,LoginActivity.class);
                            startActivity(i);
                            finishAffinity();
                            finish();
                        } else {
                            Toast.makeText(NewPassword.this, "Password Not Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

