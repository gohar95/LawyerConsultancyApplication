package paklegal2020.pl.paklegal.Models;

public class Chat {
    private String sender, reciever, message;

    public Chat(String sender, String reciever, String message) {
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;
    }

    public Chat(){

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
