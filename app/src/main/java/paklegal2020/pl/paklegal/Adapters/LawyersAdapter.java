package paklegal2020.pl.paklegal.Adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import paklegal2020.pl.paklegal.ClientSide.LawyerBooking;
import paklegal2020.pl.paklegal.Models.QualificationModel;
import paklegal2020.pl.paklegal.R;


public class LawyersAdapter extends RecyclerView.Adapter<LawyersAdapter.Holder>{
    private Context ctx;
    private ArrayList<QualificationModel> list;

    public LawyersAdapter(Context ctx, ArrayList<QualificationModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(ctx).inflate(R.layout.lawyer_card, viewGroup, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        QualificationModel model = list.get(i);

        Glide.with(ctx).load(model.getLawerImage()).into(holder.card_image);
        holder.card_name.setText(model.getLawyerName());
        String rating = model.getLawyerRating();
        float rate = Float.valueOf(rating);
        holder.ratingBar.setRating(rate);
        holder.card_rating_count.setText("("+rating+"/5.0)");
        holder.card_categories.setText(model.getLawyerCategory());
        holder.card_education.setText(model.getLawyerEducation());
        holder.card_consult.setText(model.getLawyerSpeciality());
        holder.card_available.setText("Available From "+model.getLawyerDate());

        if(rate >= 4.0){
            holder.card_mostbooked.setText("Most Booked");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //filterlist
    public void filterList(ArrayList<QualificationModel> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder{

        Button card_book;
        RatingBar ratingBar;
        ImageView card_image;
        TextView card_name,card_rating_count,card_mostbooked,card_topreviwed,card_categories,card_education,card_consult,card_available;

        public Holder(@NonNull View itemView) {
            super(itemView);

            card_book = itemView.findViewById(R.id.card_book);
            card_image = itemView.findViewById(R.id.card_image);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            card_name = itemView.findViewById(R.id.card_name);
            card_rating_count = itemView.findViewById(R.id.card_rating_count);
            card_mostbooked = itemView.findViewById(R.id.card_mostbooked);
            card_topreviwed = itemView.findViewById(R.id.card_topreviwed);
            card_categories = itemView.findViewById(R.id.card_categories);
            card_education = itemView.findViewById(R.id.card_education);
            card_consult = itemView.findViewById(R.id.card_consult);
            card_available = itemView.findViewById(R.id.card_available);


            card_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QualificationModel model = list.get(getAdapterPosition());

                    Intent i = new Intent(ctx, LawyerBooking.class);
                    i.putExtra("LawyerKey",model.getLawyerKey());
                    i.putExtra("LawyerEducation",model.getLawyerEducation());
                    i.putExtra("LawyerCNIC",model.getLawyerCNIC());
                    i.putExtra("LawyerLicence",model.getLawyerLicence());
                    i.putExtra("LawyerLowerCrt",model.getLawyerLowerCrt());
                    i.putExtra("LawyerHigherCrt",model.getLawyerHigherCrt());
                    i.putExtra("VoterLowerCrt",model.getVoterLowerCrt());
                    i.putExtra("VoterHigherCrt",model.getVoterHigherCrt());
                    i.putExtra("LawyerLanguage",model.getLawyerLicence());
                    i.putExtra("LawyerCategory",model.getLawyerCategory());
                    i.putExtra("ChamberCity",model.getChamberCity());
                    i.putExtra("LawyerSpeciality",model.getLawyerSpeciality());
                    i.putExtra("LawerImage",model.getLawerImage());
                    i.putExtra("LawyerName",model.getLawyerName());
                    i.putExtra("LawyerPhone",model.getLawyerPhone());
                    i.putExtra("LawyerDate",model.getLawyerDate());
                    i.putExtra("LawyerRating",model.getLawyerRating());
                    ctx.startActivity(i);
                }
            });
        }
    }
}
