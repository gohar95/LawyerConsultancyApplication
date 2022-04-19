package paklegal2020.pl.paklegal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import paklegal2020.pl.paklegal.ClientSide.ClientDashboard;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextView forgetPaas, s1, s2, createAcc;
    EditText email, password;
    Button btn;
    ProgressDialog progressDialog;
    Switch select_log;
    Boolean bb= false;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        s1 = findViewById(R.id.textView);
        s2 = findViewById(R.id.textView2);
        createAcc = findViewById(R.id.moveAccount);
        email = findViewById(R.id.user_email);
        password = findViewById(R.id.user_password);
        btn = findViewById(R.id.signin_btn);
        forgetPaas = findViewById(R.id.login);
            select_log = findViewById(R.id.select_log);

        s1.setTypeface(s1.getTypeface(), Typeface.BOLD);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckValidation();
            }
        });

            select_log.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                        select_log.setText("Client");
                    else
                        select_log.setText("Lawyer");
                }
            });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,Signup.class));
                finish();
            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,Signup.class));
                finish();
            }
        });
        forgetPaas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,ForgotPassword.class) ;
                i.putExtra("STATUS", "Users");
                startActivity(i);
                finish();
            }
        });

    }

    private void CheckValidation() {
        String Email = email.getText().toString();
        String Password = password.getText().toString();

        if(TextUtils.isEmpty(Email)){
            email.setError("Write Email Here");
        }
        else if(TextUtils.isEmpty(Password)){
            password.setError("Write Password Here");
        }
        else{
            UserLogin(Email, Password);
        }
    }

    private void UserLogin(final String email, String password) {
        progressDialog.setMessage("Login\nPlease Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if((select_log.getText().toString().equals("Client"))){
                                FETCH_USER_DETAILS("Client",email);
                            }
                            else{
                                FETCH_USER_DETAILS("Lawyer",email);
                            }

                        }
                        else{
                            progressDialog.dismiss();
                            //Alert Dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                            alertDialog.setTitle("Login Failed");
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("Incorrect\nEmail or Password\nTry Again");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                });
    }


    private void FETCH_USER_DETAILS(final String status, final String Email) {
        progressDialog.setTitle("Fetching User Data");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(status).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String email = snapshot.child("email").getValue().toString();

                        if(email.equals(Email)){

                            Notifications.createNotificationChannel(LoginActivity.this);

                            bb = true;
                            progressDialog.dismiss();
                            if(status.equals("Lawyer")){
                                UpdateToken();

                                Intent i = new Intent(LoginActivity.this,Dashboard.class);
                                SharedPreferences shared = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = shared.edit();
                                editor.putString("STATUS", status);
                                editor.apply();
                                startActivity(i);
                                finishAffinity();
                                finish();
                            }
                            else{
                                UpdateTokenClient();

                                Intent i = new Intent(LoginActivity.this,ClientDashboard.class);
                                SharedPreferences shared = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = shared.edit();
                                editor.putString("STATUS", status);
                                editor.apply();
                                startActivity(i);
                                finishAffinity();
                                finish();
                            }
                        }
                    }

                    if(!bb){
                        bb = false;
                        progressDialog.dismiss();
                        //Alert Dialog
                        auth.signOut();
                    }
                }
                else{
                    progressDialog.dismiss();
                    //Alert Dialog
                    auth.signOut();
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Login Failed");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Incorrect\nEmail or Password\nTry Again");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                //Alert Dialog
                auth.signOut();
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("Login Failed");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Incorrect\nEmail or Password\nTry Again");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private void UpdateTokenClient(){
        String Token = FirebaseInstanceId.getInstance().getToken();
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Client").child(auth.getCurrentUser().getUid());
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

}
