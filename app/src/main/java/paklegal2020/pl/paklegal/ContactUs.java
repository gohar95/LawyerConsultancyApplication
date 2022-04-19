package paklegal2020.pl.paklegal;

import androidx.appcompat.app.AppCompatActivity;
import paklegal2020.pl.paklegal.ClientSide.ContactUsClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactUs extends AppCompatActivity {

    private RelativeLayout copy_em,copy_ad2;
    private ImageButton copy_p1, copy_p2;
    TextView ad2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        ad2 = findViewById(R.id.tvNumber6);

        //Copy Phone 1
        copy_p1 = findViewById(R.id.copyPhoneOne);
        copy_p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClipboard(ContactUs.this,"+923074241489");
                Toast.makeText(ContactUs.this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        //Copy Phone 2
        copy_p2 = findViewById(R.id.copyPhoneTwo);
        copy_p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClipboard(ContactUs.this,"+923084902959");
                Toast.makeText(ContactUs.this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        //Copy Email
        copy_em = findViewById(R.id.copyEmail);
        copy_em.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setClipboard(ContactUs.this,"paklegal.help@gmail.com");
                Toast.makeText(ContactUs.this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //Copy Address 2
        copy_ad2 = findViewById(R.id.addressTwoCopy);
        copy_ad2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setClipboard(ContactUs.this, ad2.getText().toString());
                Toast.makeText(ContactUs.this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    //Copy Phones Code
    @SuppressLint("ObsoleteSdkInt")
    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                clipboard.setText(text);
            }
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
            }
        }
    }
}