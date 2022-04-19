package paklegal2020.pl.paklegal;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class ForgotPassword extends AppCompatActivity {

    String NAME = "";
    String STATUS ="";
    EditText tvEmail;
    Button bSMS;
    boolean status = false;
    int randomNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            STATUS  = bundle.getString("STATUS");
        }


        tvEmail = findViewById(R.id.forgotEmail);

        bSMS = findViewById(R.id.forgotSMS);

        bSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(tvEmail.getText().toString().trim()) && Patterns.EMAIL_ADDRESS.matcher(tvEmail.getText().toString().trim()).matches())
                    tvEmail.setError("This Field is Required");
                else{
                    Random random = new Random();
                    randomNumber = random.nextInt(999999);
                    String str= Integer.toString(randomNumber);
                    SendSMS(tvEmail.getText().toString().trim(),str);
                }
            }
        });
    }

    public void SendSMS(String email, String pin)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new SendMail(pin,email).execute();
    }

    private class SendMail extends AsyncTask<String, Void, Integer>
    {
        ProgressDialog pd = null;
        String error = null;
        Integer result;
        String pincode;
        String email;

        public SendMail(String pincode, String email) {
            this.pincode = pincode;
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(ForgotPassword.this);
            pd.setTitle("Sending Code to your Mail");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            // TODO Auto-generated method stub

            MailSender sender = new MailSender("muhammadz43753@gmail.com", "Ali156866");

            sender.setTo(new String[]{email});
            sender.setFrom("muhammadz43753@gmail.com");
            sender.setSubject("Pak-Legal Help Center ");
            sender.setBody("OTP Code of your Pak-Legal Account\n\n" +
                    "Thanks for creating an Account with the following Email : "+email+"\n\n" +
                    "Please Use the Following OTP for Recover Your Account : "+pincode);
            try {
                if(sender.send()) {
                    System.out.println("Message sent");
                    return 1;
                } else {
                    return 2;
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("SendMail", e.getMessage(), e);
            }

            return 3;
        }

        protected void onPostExecute(Integer result) {
            pd.dismiss();
            if(error!=null) {

            }
            if(result==1) {
                Toast.makeText(ForgotPassword.this,
                        "Code sent successfully to your Mail.", Toast.LENGTH_LONG).show();

                SharedPreferences shared = getSharedPreferences("SESSION", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("OTP", pincode);
                editor.commit();

                Intent i = new Intent(ForgotPassword.this, OtpActivity.class);
                i.putExtra("OTPR",pincode);
                i.putExtra("EMAIL",email);
                i.putExtra("STATUS",STATUS);
                startActivity(i);
                finish();
            } else if(result==2) {
                Toast.makeText(ForgotPassword.this,
                        "Email not sent.", Toast.LENGTH_LONG).show();
            } else if(result==3) {
                Toast.makeText(ForgotPassword.this,
                        "There is a problem sending the email.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}