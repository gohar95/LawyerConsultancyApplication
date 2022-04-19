package paklegal2020.pl.paklegal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OtpActivity extends AppCompatActivity {

    TextView tvPhone, tvNOT;
    EditText edOTP1,edOTP2,edOTP3,edOTP4,edOTP5,edOTP6;
    Button bOTP;
    String OTPR = "";
    String EMAIL = "";
    String OTP = "";
    StringBuilder sb;
    String STATUS = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Bundle b = getIntent().getExtras();
        if(b != null){
            OTPR = b.getString("OTPR");
            EMAIL = b.getString("EMAIL");
            STATUS = b.getString("STATUS");
        }

        sb=new StringBuilder();

        SharedPreferences shared = getSharedPreferences("SESSION", Context.MODE_PRIVATE);
        OTP = shared.getString("OTP","");

        tvPhone = findViewById(R.id.otpPhone);
        edOTP1 = findViewById(R.id.otp1);
        edOTP2 = findViewById(R.id.otp2);
        edOTP3 = findViewById(R.id.otp3);
        edOTP4 = findViewById(R.id.otp4);
        edOTP5 = findViewById(R.id.otp5);
        edOTP6 = findViewById(R.id.otp6);
        bOTP = findViewById(R.id.otpButton);

        tvPhone.setText(EMAIL);

        bOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String o1 = edOTP1.getText().toString().trim();
                String o2 = edOTP2.getText().toString().trim();
                String o3 = edOTP3.getText().toString().trim();
                String o4 = edOTP4.getText().toString().trim();
                String o5 = edOTP5.getText().toString().trim();
                String o6 = edOTP6.getText().toString().trim();

                String otp = o1+o2+o3+o4+o5+o6;

                if(otp.equals(OTPR)){
                    Intent i = new Intent(OtpActivity.this,NewPassword.class);
                    i.putExtra("EMAIL", EMAIL);
                    i.putExtra("STATUS", STATUS);
                    startActivity(i);
                    finish();
                }
                else
                    Toast.makeText(OtpActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
            }
        });

        edOTP1.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length()==0&edOTP1.length()==1) {
                    sb.append(s);
                    edOTP1.clearFocus();
                    edOTP2.requestFocus();
                    edOTP2.setCursorVisible(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(sb.length()==1) {
                    sb.deleteCharAt(0);
                }
            }
            public void afterTextChanged(Editable s) {
                if(sb.length()==0) {
                    edOTP1.requestFocus();
                }
            }
        });

        edOTP2.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length()==0&edOTP2.length()==1) {
                    sb.append(s);
                    edOTP2.clearFocus();
                    edOTP3.requestFocus();
                    edOTP3.setCursorVisible(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(sb.length()==1) {
                    sb.deleteCharAt(0);
                }
            }
            public void afterTextChanged(Editable s) {
                if(sb.length()==0) {
                    edOTP2.requestFocus();
                }
            }
        });

        edOTP3.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length()==0&edOTP3.length()==1) {
                    sb.append(s);
                    edOTP3.clearFocus();
                    edOTP4.requestFocus();
                    edOTP4.setCursorVisible(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(sb.length()==1) {
                    sb.deleteCharAt(0);
                }
            }
            public void afterTextChanged(Editable s) {
                if(sb.length()==0) {
                    edOTP3.requestFocus();
                }
            }
        });

        edOTP4.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length()==0&edOTP4.length()==1) {
                    sb.append(s);
                    edOTP4.clearFocus();
                    edOTP5.requestFocus();
                    edOTP5.setCursorVisible(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(sb.length()==1) {
                    sb.deleteCharAt(0);
                }
            }
            public void afterTextChanged(Editable s) {
                if(sb.length()==0) {
                    edOTP4.requestFocus();
                }
            }
        });

        edOTP5.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length()==0&edOTP5.length()==1) {
                    sb.append(s);
                    edOTP5.clearFocus();
                    edOTP6.requestFocus();
                    edOTP6.setCursorVisible(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(sb.length()==1) {
                    sb.deleteCharAt(0);
                }
            }
            public void afterTextChanged(Editable s) {
                if(sb.length()==0) {
                    edOTP5.requestFocus();
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
            pd = new ProgressDialog(OtpActivity.this);
            pd.setTitle("Re-Sending Code to your Mail");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            // TODO Auto-generated method stub

            MailSender sender = new MailSender("legalpak.help@gmail.com", "paklegal1155");
            sender.setTo(new String[]{email});
            sender.setFrom("legalpak.help@gmail.com");
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
                SharedPreferences shared = getSharedPreferences("SESSION", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("OTP", pincode);
                editor.commit();
                Toast.makeText(OtpActivity.this,
                        "Code sent successfully.", Toast.LENGTH_LONG).show();
            } else if(result==2) {
                Toast.makeText(OtpActivity.this,
                        "Code not sent.", Toast.LENGTH_LONG).show();
            } else if(result==3) {
                Toast.makeText(OtpActivity.this,
                        "There is a problem sending the email.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}