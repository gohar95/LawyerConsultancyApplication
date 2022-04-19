package paklegal2020.pl.paklegal.Models;

public class RequestModel {
    private String caseKey, Name, Email, RCase, Image, Phone, ClientKey, LawyerKey, DateTime, Status;

    public RequestModel(String caseKey, String name, String email, String RCase, String image, String phone, String clientKey, String lawyerKey, String dateTime, String status) {
        this.caseKey = caseKey;
        Name = name;
        Email = email;
        this.RCase = RCase;
        Image = image;
        Phone = phone;
        ClientKey = clientKey;
        LawyerKey = lawyerKey;
        DateTime = dateTime;
        Status = status;
    }

    public String getCaseKey() {
        return caseKey;
    }

    public void setCaseKey(String caseKey) {
        this.caseKey = caseKey;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getRCase() {
        return RCase;
    }

    public void setRCase(String RCase) {
        this.RCase = RCase;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getClientKey() {
        return ClientKey;
    }

    public void setClientKey(String clientKey) {
        ClientKey = clientKey;
    }

    public String getLawyerKey() {
        return LawyerKey;
    }

    public void setLawyerKey(String lawyerKey) {
        LawyerKey = lawyerKey;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
