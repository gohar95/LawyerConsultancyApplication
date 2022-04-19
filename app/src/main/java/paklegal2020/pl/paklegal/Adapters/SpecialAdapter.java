package paklegal2020.pl.paklegal.Adapters;


import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import paklegal2020.pl.paklegal.ClientSide.ShowLawyers;
import paklegal2020.pl.paklegal.Models.SpecialModel;
import paklegal2020.pl.paklegal.R;

public class SpecialAdapter extends RecyclerView.Adapter<SpecialAdapter.Holder>{
    private Context ctx;
    private ArrayList<SpecialModel> list;

    public SpecialAdapter(Context ctx, ArrayList<SpecialModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.speciality_item, viewGroup, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        SpecialModel model = list.get(i);
        holder.title.setText(model.getTitle());
        holder.des.setText(model.getDescription());
        holder.imageView.setImageBitmap(model.getImage());
    }

    //filterlist
    public void filterList(ArrayList<SpecialModel> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView title, des;


        public Holder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.special_icon);
            title = itemView.findViewById(R.id.special_title);
            des = itemView.findViewById(R.id.special_des);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SpecialModel model = list.get(getAdapterPosition());
                    String title = model.getTitle();
                    Intent i = new Intent(ctx, ShowLawyers.class);
                    i.putExtra("TITLE", title);
                    ctx.startActivity(i);
                }
            });
        }
    }
}
