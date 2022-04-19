package paklegal2020.pl.paklegal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import paklegal2020.pl.paklegal.Adapters.RequestAdapter;
import paklegal2020.pl.paklegal.Models.QualificationModel;
import paklegal2020.pl.paklegal.Models.RequestModel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewQualification extends AppCompatActivity {

    EditText lawyer_education,lawyer_cnic,lawyer_licence,lawyer_lcourt,lawyer_hcourt,lawyer_vlcourt,lawyer_vhcourt,lawyer_chambercity,lawyer_speciality;
    Button addData;
    TextView lawyer_language,lawyer_category;

    FirebaseAuth auth;
    ProgressDialog progressDialog;

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_qualification);

        shared = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);

        InitilizeViews();

        lawyer_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenLanguageDialog();
            }
        });

        lawyer_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCategoryDialog();
            }
        });

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckValidation();
            }
        });
    }

    private void InitilizeViews() {
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Data");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        lawyer_education = findViewById(R.id.lawyer_education);
        lawyer_cnic = findViewById(R.id.lawyer_cnic);
        lawyer_licence = findViewById(R.id.lawyer_licence);
        lawyer_lcourt = findViewById(R.id.lawyer_lcourt);
        lawyer_hcourt = findViewById(R.id.lawyer_hcourt);
        lawyer_vlcourt = findViewById(R.id.lawyer_vlcourt);
        lawyer_vhcourt = findViewById(R.id.lawyer_vhcourt);
        lawyer_chambercity = findViewById(R.id.lawyer_chambercity);
        lawyer_speciality = findViewById(R.id.lawyer_speciality);
        addData = findViewById(R.id.addData);
        lawyer_language = findViewById(R.id.lawyer_language);
        lawyer_category = findViewById(R.id.lawyer_category);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("LawyerQualification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = auth.getCurrentUser().getUid();
                        if(key.equals(snapshot.getKey())) {
                            lawyer_education.setText(snapshot.child("lawyerEducation").getValue().toString());
                            lawyer_cnic.setText(snapshot.child("lawyerCNIC").getValue().toString());
                            lawyer_licence.setText(snapshot.child("lawyerLicence").getValue().toString());
                            lawyer_lcourt.setText(snapshot.child("lawyerLowerCrt").getValue().toString());
                            lawyer_hcourt.setText(snapshot.child("lawyerHigherCrt").getValue().toString());
                            lawyer_vlcourt.setText(snapshot.child("voterLowerCrt").getValue().toString());
                            lawyer_vhcourt.setText(snapshot.child("voterHigherCrt").getValue().toString());
                            lawyer_chambercity.setText(snapshot.child("chamberCity").getValue().toString());
                            lawyer_speciality.setText(snapshot.child("lawyerSpeciality").getValue().toString());
                            lawyer_language.setText(snapshot.child("lawyerLanguage").getValue().toString());
                            lawyer_category.setText(snapshot.child("lawyerCategory").getValue().toString());
                        }
                    }
                }
                else{
                    Toast.makeText(ViewQualification.this, "No Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewQualification.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void OpenLanguageDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (this).getLayoutInflater();
        builder.setCancelable(false);
        builder.setIcon(R.drawable.logo_copy);
        final View dialogView = inflater.inflate(R.layout.language_dialog, null);
        final CheckBox box1 = dialogView.findViewById(R.id.lang1);
        final CheckBox box2 = dialogView.findViewById(R.id.lang2);
        final CheckBox box3 = dialogView.findViewById(R.id.lang3);
        final CheckBox box4 = dialogView.findViewById(R.id.lang4);
        final CheckBox box5 = dialogView.findViewById(R.id.lang5);

        builder.setView(dialogView);
        // Add action buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                StringBuilder builder1 = new StringBuilder();
                if(box1.isChecked())
                    builder1.append(box1.getText().toString());
                if(box2.isChecked())
                    builder1.append(","+box2.getText().toString());
                if(box3.isChecked())
                    builder1.append(","+box3.getText().toString());
                if(box4.isChecked())
                    builder1.append(","+box4.getText().toString());
                if(box5.isChecked())
                    builder1.append(","+box5.getText().toString());

                lawyer_language.setText(builder1);
            }
        });
        builder.create();
        builder.show();
    }

    private void OpenCategoryDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (this).getLayoutInflater();
        builder.setCancelable(false);
        builder.setIcon(R.drawable.logo_copy);
        final View dialogView = inflater.inflate(R.layout.lawyercategory_dialog, null);
        final CheckBox box1 = dialogView.findViewById(R.id.cat1);
        final CheckBox box2 = dialogView.findViewById(R.id.cat2);
        final CheckBox box3 = dialogView.findViewById(R.id.cat3);
        final CheckBox box4 = dialogView.findViewById(R.id.cat4);
        final CheckBox box5 = dialogView.findViewById(R.id.cat5);
        final CheckBox box6 = dialogView.findViewById(R.id.cat6);
        final CheckBox box7 = dialogView.findViewById(R.id.cat7);
        final CheckBox box8 = dialogView.findViewById(R.id.cat8);
        final CheckBox box9 = dialogView.findViewById(R.id.cat9);
        final CheckBox box10 = dialogView.findViewById(R.id.cat10);
        final CheckBox box11 = dialogView.findViewById(R.id.cat11);
        final CheckBox box12 = dialogView.findViewById(R.id.cat12);
        final CheckBox box13 = dialogView.findViewById(R.id.cat13);
        final CheckBox box14 = dialogView.findViewById(R.id.cat14);
        final CheckBox box15 = dialogView.findViewById(R.id.cat15);
        final CheckBox box16 = dialogView.findViewById(R.id.cat16);
        final CheckBox box17 = dialogView.findViewById(R.id.cat17);
        final CheckBox box18 = dialogView.findViewById(R.id.cat18);
        final CheckBox box19 = dialogView.findViewById(R.id.cat19);
        final CheckBox box20 = dialogView.findViewById(R.id.cat20);
        final CheckBox box21 = dialogView.findViewById(R.id.cat21);
        final CheckBox box22 = dialogView.findViewById(R.id.cat22);
        final CheckBox box23 = dialogView.findViewById(R.id.cat23);
        final CheckBox box24 = dialogView.findViewById(R.id.cat24);
        final CheckBox box25 = dialogView.findViewById(R.id.cat25);
        final CheckBox box26 = dialogView.findViewById(R.id.cat26);
        final CheckBox box27 = dialogView.findViewById(R.id.cat27);

        builder.setView(dialogView);
        // Add action buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                StringBuilder builder1 = new StringBuilder();
                if(box1.isChecked())
                    builder1.append(box1.getText().toString());
                if(box2.isChecked())
                    builder1.append(","+box2.getText().toString());
                if(box3.isChecked())
                    builder1.append(","+box3.getText().toString());
                if(box4.isChecked())
                    builder1.append(","+box4.getText().toString());
                if(box5.isChecked())
                    builder1.append(","+box5.getText().toString());
                if(box6.isChecked())
                    builder1.append(","+box6.getText().toString());
                if(box7.isChecked())
                    builder1.append(","+box7.getText().toString());
                if(box8.isChecked())
                    builder1.append(","+box8.getText().toString());
                if(box9.isChecked())
                    builder1.append(","+box9.getText().toString());
                if(box10.isChecked())
                    builder1.append(","+box10.getText().toString());
                if(box11.isChecked())
                    builder1.append(","+box11.getText().toString());
                if(box12.isChecked())
                    builder1.append(","+box12.getText().toString());
                if(box13.isChecked())
                    builder1.append(","+box13.getText().toString());
                if(box14.isChecked())
                    builder1.append(","+box14.getText().toString());
                if(box15.isChecked())
                    builder1.append(","+box15.getText().toString());
                if(box16.isChecked())
                    builder1.append(","+box16.getText().toString());
                if(box17.isChecked())
                    builder1.append(","+box17.getText().toString());
                if(box18.isChecked())
                    builder1.append(","+box18.getText().toString());
                if(box19.isChecked())
                    builder1.append(","+box19.getText().toString());
                if(box20.isChecked())
                    builder1.append(","+box20.getText().toString());
                if(box21.isChecked())
                    builder1.append(","+box21.getText().toString());
                if(box22.isChecked())
                    builder1.append(","+box22.getText().toString());
                if(box23.isChecked())
                    builder1.append(","+box23.getText().toString());
                if(box24.isChecked())
                    builder1.append(","+box24.getText().toString());
                if(box25.isChecked())
                    builder1.append(","+box25.getText().toString());
                if(box26.isChecked())
                    builder1.append(","+box26.getText().toString());
                if(box27.isChecked())
                    builder1.append(","+box27.getText().toString());

                lawyer_category.setText(builder1);
            }
        });
        builder.create();
        builder.show();
    }

    private void CheckValidation() {
        String Education = lawyer_education.getText().toString();
        String CNIC = lawyer_cnic.getText().toString();
        String Licence = lawyer_licence.getText().toString();
        String LowerCrt = lawyer_lcourt.getText().toString();
        String HigherCrt = lawyer_hcourt.getText().toString();
        String VCrt = lawyer_vlcourt.getText().toString();
        String HCrt = lawyer_vhcourt.getText().toString();
        String LawerLang = lawyer_language.getText().toString();
        String LawerCat = lawyer_category.getText().toString();
        String ChamberCity = lawyer_chambercity.getText().toString();
        String LawyerSpec = lawyer_speciality.getText().toString();

        if(TextUtils.isEmpty(Education))
            lawyer_education.setError("Please Enter Education");
        else if(TextUtils.isEmpty(CNIC))
            lawyer_cnic.setError("Please Enter CNIC");
        else if(TextUtils.isEmpty(Licence))
            lawyer_licence.setError("Please Enter Licence No");
        else if(TextUtils.isEmpty(LowerCrt))
            lawyer_lcourt.setError("Please Enter Lower-Court");
        else if(TextUtils.isEmpty(HigherCrt))
            lawyer_hcourt.setError("Please Enter Higher-Court");
        else if(TextUtils.isEmpty(VCrt))
            lawyer_vlcourt.setError("Enter Voter for Lower-Court");
        else if(TextUtils.isEmpty(HCrt))
            lawyer_vhcourt.setError("Enter Voter for Higher-Court");
        else if(TextUtils.isEmpty(ChamberCity))
            lawyer_chambercity.setError("Please Enter Chamber City");
        else if(TextUtils.isEmpty(LawyerSpec))
            lawyer_speciality.setError("Please Enter Speciality");
        else if(LawerLang.equals("Choose Language"))
            Toast.makeText(this, "Please Choose Language", Toast.LENGTH_SHORT).show();
        else if(LawerCat.equals("Choose Category"))
            Toast.makeText(this, "Please Choose Categories", Toast.LENGTH_SHORT).show();
        else{
            INSERT_IN_DATABASE(Education,CNIC,Licence,LowerCrt,HigherCrt,VCrt,HCrt,LawerLang,LawerCat,ChamberCity,LawyerSpec);
        }
    }

    private void INSERT_IN_DATABASE(String education, String cnic, String licence, String lowerCrt, String higherCrt, String vCrt, String hCrt, String lawerLang, String lawerCat, String chamberCity, String lawyerSpec) {
        QualificationModel values = new QualificationModel(auth.getCurrentUser().getUid(),education,cnic,licence,lowerCrt,higherCrt,vCrt,hCrt,lawerLang,lawerCat,chamberCity,lawyerSpec,shared.getString("IMAGE",""),shared.getString("NAME",""),shared.getString("PHONE",""),shared.getString("DATE",""),"1.5");

        progressDialog.show();
        FirebaseDatabase.getInstance().getReference("LawyerQualification").child(auth.getCurrentUser().getUid()).setValue(values)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(ViewQualification.this, "Inserted", Toast.LENGTH_SHORT).show();
                            finish();

                        }else{
                            progressDialog.dismiss();
                            //Alert Dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                            alertDialog.setTitle("Uploading Failed");
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