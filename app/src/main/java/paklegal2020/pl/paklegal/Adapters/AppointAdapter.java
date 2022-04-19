package paklegal2020.pl.paklegal.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import paklegal2020.pl.paklegal.Models.AppointmentModel;
import paklegal2020.pl.paklegal.R;

public class AppointAdapter extends RecyclerView.Adapter<AppointAdapter.Holder>{
    private Context ctx;
    private ArrayList<AppointmentModel> list;

    public AppointAdapter(Context ctx, ArrayList<AppointmentModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.appoint_item, viewGroup, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        AppointmentModel model = list.get(i);
        Glide.with(ctx).load(model.getLawyerImage()).into(holder.lw_image);
        holder.case_title.setText(model.getCaseTitle());
        holder.lw_name.setText("Lawyer: "+model.getLawyerName());
        holder.lw_phone.setText("Phone: "+model.getLawyerPhone());
        holder.app_date.setText("Appointment Date: "+model.getDateTime());
    }

    //filterlist
    public void filterList(ArrayList<AppointmentModel> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        CircleImageView lw_image;
        TextView case_title, lw_name, lw_phone, app_date;


        public Holder(@NonNull View itemView) {
            super(itemView);

            lw_image = itemView.findViewById(R.id.lw_image);
            case_title = itemView.findViewById(R.id.case_title);
            lw_name = itemView.findViewById(R.id.lw_name);
            lw_phone = itemView.findViewById(R.id.lw_phone);
            app_date = itemView.findViewById(R.id.app_date);
        }
    }
}
