package paklegal2020.pl.paklegal.ClientSide;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import paklegal2020.pl.paklegal.Models.RequestModel;
import paklegal2020.pl.paklegal.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BookingActivity extends AppCompatActivity {

    EditText cl_case;
    Button addCase;
    TextView cl_name,cl_email;

    FirebaseAuth auth;
    ProgressDialog progressDialog;

    String LawyerID = "";

    SharedPreferences shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            LawyerID = bundle.getString("LawyerID");
        }
        shared = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);

        InitilizeViews();

        addCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValidation();
            }
        });
    }

    private void InitilizeViews() {
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        cl_case = findViewById(R.id.cl_case);
        addCase = findViewById(R.id.addCase);
        cl_name = findViewById(R.id.cl_name);
        cl_email = findViewById(R.id.cl_email);

        cl_name.setText(shared.getString("NAME",""));
        cl_email.setText(shared.getString("EMAIL",""));
    }

    private void CheckValidation() {
        String client_case = cl_case.getText().toString();
        String client_name = cl_name.getText().toString();
        String client_email = cl_email.getText().toString();


        if(TextUtils.isEmpty(client_name))
            cl_name.setError("Name not Found");
        else if(TextUtils.isEmpty(client_email))
            cl_email.setError("Email not Found");
        else if(TextUtils.isEmpty(client_case))
            cl_case.setError("Please Enter Case");
        else{
            INSERT_IN_DATABASE(client_name,client_email,client_case);
        }
    }

    private void INSERT_IN_DATABASE(String name, String email, String cases) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sending Case to Lawyer");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        final String DateTime = dateFormat.format(date);

        String caseKey = UUID.randomUUID().toString();

        RequestModel values = new RequestModel(caseKey,name,email,cases,shared.getString("IMAGE",""),shared.getString("PHONE",""),auth.getCurrentUser().getUid(),LawyerID,DateTime,"Pending");

        progressDialog.show();
        FirebaseDatabase.getInstance().getReference("CaseRequests").child(caseKey).setValue(values)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(BookingActivity.this, "Case Sent", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            progressDialog.dismiss();
                            //Alert Dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                            alertDialog.setTitle("Failed");
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("Due to\nTechnical Issue\nTry Again");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
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
}