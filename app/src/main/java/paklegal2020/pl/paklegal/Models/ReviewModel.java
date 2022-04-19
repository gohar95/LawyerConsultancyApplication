package paklegal2020.pl.paklegal.Models;

public class ReviewModel {
    private String reviewText, rating, AppointID, clientID, lawyerID;

    public ReviewModel(String reviewText, String rating, String appointID, String clientID, String lawyerID) {
        this.reviewText = reviewText;
        this.rating = rating;
        AppointID = appointID;
        this.clientID = clientID;
        this.lawyerID = lawyerID;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAppointID() {
        return AppointID;
    }

    public void setAppointID(String appointID) {
        AppointID = appointID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getLawyerID() {
        return lawyerID;
    }

    public void setLawyerID(String lawyerID) {
        this.lawyerID = lawyerID;
    }
}
