package paklegal2020.pl.paklegal.ClientSide;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import paklegal2020.pl.paklegal.R;

public class LawyerBooking extends AppCompatActivity {

    String LawyerKey = "";
    String LawyerEducation = "";
    String LawyerCNIC = "";
    String LawyerLicence = "";
    String LawyerLowerCrt = "";
    String LawyerHigherCrt = "";
    String VoterLowerCrt = "";
    String VoterHigherCrt = "";
    String LawyerLanguage = "";
    String LawyerCategory = "";
    String ChamberCity = "";
    String LawyerSpeciality = "";
    String LawerImage = "";
    String LawyerName = "";
    String LawyerPhone = "";
    String LawyerDate = "";
    String LawyerRating = "";

    TextView lName;
    ImageView backButton, callButton;
    Button card_book,card_chat;
    ImageView card_image, rate1, rate2, rate3, rate4, rate5;
    TextView card_name, card_rating_count, card_mostbooked, card_topreviwed, card_categories, card_education, card_consult, card_available;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_booking);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            LawyerKey = bundle.getString("LawyerKey");
            LawyerEducation = bundle.getString("LawyerEducation");
            LawyerCNIC = bundle.getString("LawyerCNIC");
            LawyerLicence = bundle.getString("LawyerLicence");
            LawyerLowerCrt = bundle.getString("LawyerLowerCrt");
            LawyerHigherCrt = bundle.getString("LawyerHigherCrt");
            VoterLowerCrt = bundle.getString("VoterLowerCrt");
            VoterHigherCrt = bundle.getString("VoterHigherCrt");
            LawyerLanguage = bundle.getString("LawyerLanguage");
            LawyerCategory = bundle.getString("LawyerCategory");
            ChamberCity = bundle.getString("ChamberCity");
            LawyerSpeciality = bundle.getString("LawyerSpeciality");
            LawerImage = bundle.getString("LawerImage");
            LawyerName = bundle.getString("LawyerName");
            LawyerPhone = bundle.getString("LawyerPhone");
            LawyerDate = bundle.getString("LawyerDate");
            LawyerRating = bundle.getString("LawyerRating");
        }

        card_book = findViewById(R.id.card_book);
        card_chat = findViewById(R.id.card_chat);
        card_image = findViewById(R.id.card_image);
        rate1 = findViewById(R.id.rate1);
        rate2 = findViewById(R.id.rate2);
        rate3 = findViewById(R.id.rate3);
        rate4 = findViewById(R.id.rate4);
        rate5 = findViewById(R.id.rate5);
        card_name = findViewById(R.id.card_name);
        card_rating_count = findViewById(R.id.card_rating_count);
        card_mostbooked = findViewById(R.id.card_mostbooked);
        card_topreviwed = findViewById(R.id.card_topreviwed);
        card_categories = findViewById(R.id.card_categories);
        card_education = findViewById(R.id.card_education);
        card_consult = findViewById(R.id.card_consult);
        card_available = findViewById(R.id.card_available);
        lName = findViewById(R.id.lName);
        backButton = findViewById(R.id.backButton);
        callButton = findViewById(R.id.callButton);

        lName.setText(LawyerName);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(LawyerBooking.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LawyerBooking.this, new String[]{Manifest.permission.CALL_PHONE}, 1);

                }

                if (ActivityCompat.checkSelfPermission(LawyerBooking.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+LawyerPhone));
                    startActivity(callIntent);
                }
            }
        });

        InitViews();

        card_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LawyerBooking.this,BookingActivity.class);
                i.putExtra("LawyerID",LawyerKey);
                startActivity(i);
            }
        });

        card_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LawyerBooking.this,ClientChat.class);
                i.putExtra("LawyerID",LawyerKey);
                i.putExtra("name",LawyerName);
                i.putExtra("image",LawerImage);
                i.putExtra("phone",LawyerPhone);
                startActivity(i);
            }
        });

    }

    private void InitViews() {
        //Picasso.with(ctx).load(model.getLawerImage()).centerCrop().into(holder.card_image);
        card_name.setText(LawyerName);
        float rate = Float.valueOf(LawyerRating);
        if ((rate == 0.0f)) {
            rate1.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
            rate2.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
            rate3.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
            rate4.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
            rate5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
        } else if ((rate > 0.0f) && (rate < 1.5f)) {
            rate1.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate2.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
            rate3.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
            rate4.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
            rate5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
        } else if ((rate == 1.5f)) {
            rate1.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate2.setImageDrawable(getResources().getDrawable(R.drawable.half_star));
            rate3.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
            rate4.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
            rate5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
        } else if ((rate > 1.5f) && (rate < 2.5f)) {
            rate1.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate2.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate3.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
            rate4.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
            rate5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
        } else if ((rate == 2.5f)) {
            rate1.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate2.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate3.setImageDrawable(getResources().getDrawable(R.drawable.half_star));
            rate4.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
            rate5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
        } else if ((rate > 2.5f) && (rate < 3.5f)) {
            rate1.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate2.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate3.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate4.setImageDrawable(getResources().getDrawable(R.drawable.half_star));
            rate5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
        } else if ((rate == 3.5f)) {
            rate1.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate2.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate3.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate4.setImageDrawable(getResources().getDrawable(R.drawable.half_star));
            rate5.setImageDrawable(getResources().getDrawable(R.drawable.empty_star));
        } else if ((rate > 3.5f) && (rate < 4.8f)) {
            rate1.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate2.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate3.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate4.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate5.setImageDrawable(getResources().getDrawable(R.drawable.half_star));
        } else if ((rate == 5.0f)) {
            rate1.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate2.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate3.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate4.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
            rate5.setImageDrawable(getResources().getDrawable(R.drawable.fill_star));
        }
        card_rating_count.setText("(" + LawyerRating + "/5.0)");

        card_categories.setText(LawyerCategory);
        card_education.setText(LawyerEducation);
        card_consult.setText(LawyerSpeciality);
        card_available.setText("Available From " + LawyerDate);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+LawyerPhone));

                    startActivity(callIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
