package paklegal2020.pl.paklegal.Models;



public class UserModel {
    private String token, Name, Email, Password, Phone, Address, Status, ImgURL, DateTime;


    public UserModel(String token, String name, String email, String password, String phone, String address, String status, String imgURL, String dateTime) {
        this.token = token;
        Name = name;
        Email = email;
        Password = password;
        Phone = phone;
        Address = address;
        Status = status;
        ImgURL = imgURL;
        DateTime = dateTime;

    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String imgURL) {
        ImgURL = imgURL;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }




}
