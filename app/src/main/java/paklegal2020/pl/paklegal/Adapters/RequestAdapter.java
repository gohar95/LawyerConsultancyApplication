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
import paklegal2020.pl.paklegal.Models.RequestModel;
import paklegal2020.pl.paklegal.R;
import paklegal2020.pl.paklegal.RequestDetails;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.Holder>{
    private Context ctx;
    private ArrayList<RequestModel> list;

    public RequestAdapter(Context ctx, ArrayList<RequestModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.request_item, viewGroup, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        RequestModel model = list.get(i);
        Glide.with(ctx).load(model.getImage()).into(holder.lw_image);
        holder.case_title.setText(model.getRCase());
        holder.lw_name.setText("Client: "+model.getName());
        holder.lw_phone.setText("Phone: "+model.getPhone());
        holder.app_date.setText("Request Date: "+model.getDateTime());
    }

    //filterlist
    public void filterList(ArrayList<RequestModel> filteredList){
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

            lw_image = itemView.findViewById(R.id.cl_image);
            case_title = itemView.findViewById(R.id.case_title);
            lw_name = itemView.findViewById(R.id.cl_name);
            lw_phone = itemView.findViewById(R.id.cl_phone);
            app_date = itemView.findViewById(R.id.case_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestModel model = list.get(getAdapterPosition());
                    Intent i = new Intent(ctx, RequestDetails.class);
                    i.putExtra("NAME",model.getName());
                    i.putExtra("EMAIL",model.getEmail());
                    i.putExtra("RCASE",model.getRCase());
                    i.putExtra("IMAGE",model.getImage());
                    i.putExtra("PHONE",model.getPhone());
                    i.putExtra("CKEY",model.getClientKey());
                    i.putExtra("DATETIME",model.getDateTime());
                    i.putExtra("STATUS",model.getStatus());
                    i.putExtra("CASEKEY",model.getCaseKey());
                    ctx.startActivity(i);
                }
            });
        }
    }
}
