package paklegal2020.pl.paklegal.Models;

public class AppointmentModel {
    private String AppintKey,LawyerName, LawyerPhone, CaseTitle, DateTime, LawyerImage, ClientID, LawyerID;

    public AppointmentModel(String appintKey, String lawyerName, String lawyerPhone, String caseTitle, String dateTime, String lawyerImage, String clientID, String lawyerID) {
        AppintKey = appintKey;
        LawyerName = lawyerName;
        LawyerPhone = lawyerPhone;
        CaseTitle = caseTitle;
        DateTime = dateTime;
        LawyerImage = lawyerImage;
        ClientID = clientID;
        LawyerID = lawyerID;
    }

    public String getAppintKey() {
        return AppintKey;
    }

    public void setAppintKey(String appintKey) {
        AppintKey = appintKey;
    }

    public String getLawyerName() {
        return LawyerName;
    }

    public void setLawyerName(String lawyerName) {
        LawyerName = lawyerName;
    }

    public String getLawyerPhone() {
        return LawyerPhone;
    }

    public void setLawyerPhone(String lawyerPhone) {
        LawyerPhone = lawyerPhone;
    }

    public String getCaseTitle() {
        return CaseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        CaseTitle = caseTitle;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getLawyerImage() {
        return LawyerImage;
    }

    public void setLawyerImage(String lawyerImage) {
        LawyerImage = lawyerImage;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getLawyerID() {
        return LawyerID;
    }

    public void setLawyerID(String lawyerID) {
        LawyerID = lawyerID;
    }
}
