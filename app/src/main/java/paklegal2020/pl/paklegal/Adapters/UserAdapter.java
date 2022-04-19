package paklegal2020.pl.paklegal.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import paklegal2020.pl.paklegal.LawyerChat;
import paklegal2020.pl.paklegal.Models.LawyerModel;
import paklegal2020.pl.paklegal.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Holder> {
    private Context ctx;
    private List<LawyerModel> list;

    public UserAdapter(Context ctx, List<LawyerModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.user_card,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LawyerModel model = list.get(position);
        Glide.with(ctx).load(model.getImgURL()).into(holder.headerImage);
        holder.header_name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        CircleImageView headerImage;
        TextView header_name;

        public Holder(@NonNull View itemView) {
            super(itemView);

            headerImage = itemView.findViewById(R.id.headerImage);
            header_name = itemView.findViewById(R.id.header_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LawyerModel model = list.get(getAdapterPosition());
                        Intent intent = new Intent(ctx, LawyerChat.class);
                        intent.putExtra("name", model.getName());
                        intent.putExtra("image", model.getImgURL());
                        intent.putExtra("phone", model.getPhone());
                        intent.putExtra("ClientID",model.getKey());
                        ctx.startActivity(intent);
                }
            });
        }
    }
}
