package paklegal2020.pl.paklegal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    ImageView edit_name_icon,edit_phone_icon,edit_add_icon, edit_pass_icon;
    ImageView edit_name_ok,edit_phone_ok,edit_add_ok, edit_pass_ok;
    TextView text_name, text_phone, text_add, text_pass;
    EditText edit_name,edit_phone, edit_add, edit_pass;
    Button update_btn;
    CircleImageView profile_image;
    SharedPreferences shared, shared1;

    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        shared = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);
        shared1 = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);

        profile_image = findViewById(R.id.profile_image);

        edit_name_icon = findViewById(R.id.edit_name_icon);
        edit_phone_icon = findViewById(R.id.edit_phone_icon);
        edit_add_icon = findViewById(R.id.edit_add_icon);
        edit_pass_icon = findViewById(R.id.edit_pass_icon);

        edit_name_ok = findViewById(R.id.edit_name_ok);
        edit_phone_ok = findViewById(R.id.edit_phone_ok);
        edit_add_ok = findViewById(R.id.edit_add_ok);
        edit_pass_ok = findViewById(R.id.edit_pass_ok);

        text_name = findViewById(R.id.text_name);
        text_phone = findViewById(R.id.text_phone);
        text_add = findViewById(R.id.text_add);
        text_pass = findViewById(R.id.text_pass);

        edit_name = findViewById(R.id.edit_name);
        edit_phone = findViewById(R.id.edit_phone);
        edit_add = findViewById(R.id.edit_add);
        edit_pass = findViewById(R.id.edit_pass);

        update_btn = findViewById(R.id.update_btn);

        AddDataToAllViews();

        edit_name_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = text_name.getText().toString();
                text_name.setVisibility(View.GONE);
                edit_name.setVisibility(View.VISIBLE);
                edit_name_icon.setVisibility(View.GONE);
                edit_name_ok.setVisibility(View.VISIBLE);
                edit_name.setText(text);
            }
        });
        edit_name_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edit_name.getText().toString();
                text_name.setVisibility(View.VISIBLE);
                edit_name.setVisibility(View.GONE);
                edit_name_icon.setVisibility(View.VISIBLE);
                edit_name_ok.setVisibility(View.GONE);
                text_name.setText(text);
            }
        });


        edit_phone_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = text_phone.getText().toString();
                text_phone.setVisibility(View.GONE);
                edit_phone.setVisibility(View.VISIBLE);
                edit_phone_icon.setVisibility(View.GONE);
                edit_phone_ok.setVisibility(View.VISIBLE);
                edit_phone.setText(text);
            }
        });
        edit_phone_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edit_phone.getText().toString();
                text_phone.setVisibility(View.VISIBLE);
                edit_phone.setVisibility(View.GONE);
                edit_phone_icon.setVisibility(View.VISIBLE);
                edit_phone_ok.setVisibility(View.GONE);
                text_phone.setText(text);
            }
        });


        edit_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = text_add.getText().toString();
                text_add.setVisibility(View.GONE);
                edit_add.setVisibility(View.VISIBLE);
                edit_add_icon.setVisibility(View.GONE);
                edit_add_ok.setVisibility(View.VISIBLE);
                edit_add.setText(text);
            }
        });
        edit_add_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edit_add.getText().toString();
                text_add.setVisibility(View.VISIBLE);
                edit_add.setVisibility(View.GONE);
                edit_add_icon.setVisibility(View.VISIBLE);
                edit_add_ok.setVisibility(View.GONE);
                text_add.setText(text);
            }
        });


        edit_pass_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = text_pass.getText().toString();
                text_pass.setVisibility(View.GONE);
                edit_pass.setVisibility(View.VISIBLE);
                edit_pass_icon.setVisibility(View.GONE);
                edit_pass_ok.setVisibility(View.VISIBLE);
                edit_pass.setText(text);
            }
        });
        edit_pass_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edit_pass.getText().toString();
                text_pass.setVisibility(View.VISIBLE);
                edit_pass.setVisibility(View.GONE);
                edit_pass_icon.setVisibility(View.VISIBLE);
                edit_pass_ok.setVisibility(View.GONE);
                text_pass.setText(text);
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = text_name.getText().toString();
                String Phone = text_phone.getText().toString();
                String Address = text_add.getText().toString();
                final String Password = text_pass.getText().toString();

                UpdatePassword(Name,Phone,Address,Password);
            }
        });
    }

    private void UpdatePassword(final String name, final String phone, final String address, final String password){
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AuthCredential credential = EmailAuthProvider
                .getCredential(shared.getString("EMAIL",""), shared.getString("PASS",""));

        auth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            auth.getCurrentUser().updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Profile.this, "Password updated", Toast.LENGTH_SHORT).show();
                                        UpdateData(name,phone,address,password);
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(Profile.this, "Error password not updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Profile.this, "Error auth failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void UpdateData(final String name, final String phone, final String address, final String password) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(shared1.getString("STATUS","")).child(shared.getString("KEY",""));
        Map<String, Object> updates = new HashMap<String,Object>();
        updates.put("name", name);
        updates.put("phone", phone);
        updates.put("address", address);
        updates.put("password", password);
        ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    UpdateSession(name,phone,address,password);
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(Profile.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UpdateSession(String name, String phone, String address, String password) {
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("NAME", name);
        editor.putString("PASS", password);
        editor.putString("PHONE", phone);
        editor.putString("ADDRESS", address);
        editor.apply();
        Toast.makeText(this, "All Data Updated", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    private void AddDataToAllViews() {
        String encoded = shared.getString("IMGURL","");
        byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
        profile_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        text_name.setText(shared.getString("NAME",""));
        text_phone.setText(shared.getString("PHONE",""));
        text_pass.setText(shared.getString("PASS",""));
        text_add.setText(shared.getString("ADDRESS",""));
    }

}
