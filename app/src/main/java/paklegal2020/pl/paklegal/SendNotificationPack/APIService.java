package paklegal2020.pl.paklegal.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAbiJO9n0:APA91bFueVPG69Tx7izfKtO9w7UjnbyTR1JUkIMlkt8CoZBWLYWI1e-88WKgv8lagaP4kKZwaGLs7UvRxhTyVw55CZGO0oiR4t-9PSGXtM1UAOMWdd8C4qI0T5Gt-NZrUmCzKTNyHbxi"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}