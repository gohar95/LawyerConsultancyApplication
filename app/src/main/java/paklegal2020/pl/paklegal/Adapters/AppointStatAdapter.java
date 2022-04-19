package paklegal2020.pl.paklegal.Adapters;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import paklegal2020.pl.paklegal.ApoointmentComplete;
import paklegal2020.pl.paklegal.Models.AppointmentModel;
import paklegal2020.pl.paklegal.R;
import paklegal2020.pl.paklegal.RequestDetails;
import paklegal2020.pl.paklegal.SendNotificationPack.APIService;
import paklegal2020.pl.paklegal.SendNotificationPack.Client;
import paklegal2020.pl.paklegal.SendNotificationPack.Data;
import paklegal2020.pl.paklegal.SendNotificationPack.MyResponse;
import paklegal2020.pl.paklegal.SendNotificationPack.NotificationSender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointStatAdapter extends RecyclerView.Adapter<AppointStatAdapter.Holder>{
    private Context ctx;
    private ArrayList<AppointmentModel> list;

    public AppointStatAdapter(Context ctx, ArrayList<AppointmentModel> list) {
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


        public Holder(@NonNull final View itemView) {
            super(itemView);

            lw_image = itemView.findViewById(R.id.lw_image);
            case_title = itemView.findViewById(R.id.case_title);
            lw_name = itemView.findViewById(R.id.lw_name);
            lw_phone = itemView.findViewById(R.id.lw_phone);
            app_date = itemView.findViewById(R.id.app_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AppointmentModel model = list.get(getAdapterPosition());
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
                    builder1.setTitle("Submit Your Response");
                    builder1.setMessage("Appointment Finish/Canceled");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Finish",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    UpdateAppointStatus("Finish",model.getAppintKey(),model.getLawyerID(),model.getClientID(),model.getAppintKey());
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "Canceled",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    UpdateAppointStatus("Canceled",model.getAppintKey(),model.getLawyerID(),model.getClientID(),model.getAppintKey());
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });
        }
    }

    private void UpdateAppointStatus(final String sts, final String CASEKEY, final String LKEY, final String clientID, final String appintKey) {
        final ProgressDialog progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(sts+"...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("CaseRequests").child(CASEKEY);
        Map<String, Object> updates = new HashMap<String,Object>();
        updates.put("status", sts);
        ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();

                    sendNotification("Request "+sts,
                            "Your Request "+sts+" with the following Request ID: "+CASEKEY,LKEY,clientID,appintKey);
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(ctx, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendNotification(final String title, final String message, final String LKEY, String clientID, String appintKey) {
        final ProgressDialog progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Changing You Status...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Lawyer").child(LKEY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    String fcmToken = dataSnapshot.child("token").getValue().toString();

                    Data data = new Data(title, message);
                    NotificationSender sender = new NotificationSender(data, fcmToken);
                    apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success != 1) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ctx, title+"\n"+"Notification Failed", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(ctx, title+"\n"+"Notification Sent to Customer", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(ctx, title+"\n"+"Notification Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ctx, title+"\n"+"Notification Failed", Toast.LENGTH_SHORT).show();
            }
        });

        Intent i = new Intent(ctx, ApoointmentComplete.class);
        i.putExtra("LKEY",LKEY);
        i.putExtra("clientID",clientID);
        i.putExtra("appintKey",appintKey);
        ctx.startActivity(i);
    }
}
