package paklegal2020.pl.paklegal.Models;


import android.graphics.Bitmap;

public class SpecialModel {
    String title, description;
    Bitmap Image;

    public SpecialModel(String title, String description, Bitmap image) {
        this.title = title;
        this.description = description;
        Image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }
}
