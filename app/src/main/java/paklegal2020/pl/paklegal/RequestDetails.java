package paklegal2020.pl.paklegal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import paklegal2020.pl.paklegal.ClientSide.BookingActivity;
import paklegal2020.pl.paklegal.Models.AppointmentModel;
import paklegal2020.pl.paklegal.Models.RequestModel;
import paklegal2020.pl.paklegal.SendNotificationPack.APIService;
import paklegal2020.pl.paklegal.SendNotificationPack.Client;
import paklegal2020.pl.paklegal.SendNotificationPack.Data;
import paklegal2020.pl.paklegal.SendNotificationPack.MyResponse;
import paklegal2020.pl.paklegal.SendNotificationPack.NotificationSender;
import paklegal2020.pl.paklegal.databinding.ActivityRequestDetailsBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestDetails extends AppCompatActivity {

    String NAME = "";
    String EMAIL = "";
    String RCASE = "";
    String IMAGE = "";
    String PHONE = "";
    String CKEY = "";
    String DATETIME = "";
    String STATUS = "";
    String CASEKEY = "";

    private APIService apiService;
    ProgressDialog progressDialog;
    ActivityRequestDetailsBinding binding;
    SharedPreferences shared;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_request_details);
        binding = ActivityRequestDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            NAME = bundle.getString("NAME");
            EMAIL = bundle.getString("EMAIL");
            RCASE = bundle.getString("RCASE");
            IMAGE = bundle.getString("IMAGE");
            PHONE = bundle.getString("PHONE");
            CKEY = bundle.getString("CKEY");
            DATETIME = bundle.getString("DATETIME");
            STATUS = bundle.getString("STATUS");
            CASEKEY = bundle.getString("CASEKEY");
        }

        auth = FirebaseAuth.getInstance();

        Glide.with(this).load(IMAGE).into(binding.customerImage);
        binding.custName.setText(NAME);
        binding.caseTitle.setText(RCASE);
        binding.caseDate.setText(DATETIME);
        binding.clientPhone.setText(PHONE);
        binding.requestStatus.setText(STATUS);

        progressDialog = new ProgressDialog(this);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        shared = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);

        binding.doChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestDetails.this, LawyerChat.class);
                intent.putExtra("name", NAME);
                intent.putExtra("image", IMAGE);
                intent.putExtra("phone", PHONE);
                intent.putExtra("ClientID", CKEY);
                startActivity(intent);
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateRequestStatus("Canceled");
            }
        });

        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAppointmentDay();
            }
        });
    }

    private void OpenAppointmentDay() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (this).getLayoutInflater();
        builder.setCancelable(false);
        builder.setIcon(R.drawable.calender_icon);
        final View dialogView = inflater.inflate(R.layout.day_dialog, null);

        final DatePicker picker = dialogView.findViewById(R.id.dayPick);

        builder.setView(dialogView);
        // Add action buttons
        builder.setPositiveButton("Set Appointment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date myDate = inFormat.parse(picker.getDayOfMonth()+"-"+(picker.getMonth() + 1)+"-"+picker.getYear());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                    String dayName=simpleDateFormat.format(myDate);
                    String dte = picker.getDayOfMonth()+"-"+ (picker.getMonth() + 1)+"-"+picker.getYear();
                    UpdateRequestStatus("Appointment Set");
                    AddAppintment(dayName,dte);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create();
        builder.show();
    }

    private void AddAppintment(String dayName, String dte) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Setting Appointment with Client");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);



        AppointmentModel values = new AppointmentModel(CASEKEY,shared.getString("NAME",""),shared.getString("PHONE",""),RCASE,dayName+", "+dte,shared.getString("IMAGE",""),CKEY,auth.getCurrentUser().getUid());

        progressDialog.show();
        FirebaseDatabase.getInstance().getReference("Appointments").child(CASEKEY).setValue(values)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(RequestDetails.this, "Appointment Set", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            progressDialog.dismiss();
                            //Alert Dialog
                            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getApplicationContext()).create();
                            alertDialog.setTitle("Failed");
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("Due to\nTechnical Issue\nTry Again");
                            alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
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

    private void UpdateRequestStatus(final String sts) {
        progressDialog.setMessage(sts+"...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("CaseRequests").child(CASEKEY);
        Map<String, Object> updates = new HashMap<String,Object>();
        updates.put("status", sts);
        ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();

                    sendNotification("Request "+sts,
                            "Your Request "+sts+" with the following Request ID: "+CASEKEY);
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(RequestDetails.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendNotification(final String title, final String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Client").child(CKEY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    String fcmToken = dataSnapshot.child("token").getValue().toString();

                    Data data = new Data(title, message);
                    NotificationSender sender = new NotificationSender(data, fcmToken);
                    apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success != 1) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RequestDetails.this, title+"\n"+"Notification Failed", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(RequestDetails.this, title+"\n"+"Notification Sent to Customer", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Toast.makeText(RequestDetails.this, title+"\n"+"Notification Failed", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RequestDetails.this, title+"\n"+"Notification Failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}