package paklegal2020.pl.paklegal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import paklegal2020.pl.paklegal.ClientSide.ClientDashboard;

public class SplashScreen extends AppCompatActivity {

    ImageView mainContainer;
    FirebaseAuth auth;
    String UserStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseAuth.getInstance();
        if(auth != null){
            SharedPreferences shared = getSharedPreferences("USER_STATUS", Context.MODE_PRIVATE);
            UserStatus = shared.getString("STATUS","");
        }
        // call for animation
        mainContainer=findViewById(R.id.img);
        Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.splash_animation);
        mainContainer.setAnimation(myAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(auth.getCurrentUser() != null){
                    if(UserStatus.equals("Client")){
                        Intent i = new Intent(SplashScreen.this,ClientDashboard.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Intent i = new Intent(SplashScreen.this,Dashboard.class);
                        startActivity(i);
                        finish();
                    }
                }
                else{
                    Intent i = new Intent(SplashScreen.this,WelcomeActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        },3000);
    }
}
