package com.example.boyko.mike.groceries.db.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.ColumnInfo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A Category is a grouping on a list.
 * It could refer to a store name, or a section of
 * a store, or ... anything else, really.
 */

@Entity(indices = {@Index("name")})
public class Category implements Parcelable {

    public static final String TAG = "Category";
    public static final String UNCATEGORIZED = "Uncategorized";

    public static final String DEFAULT_COLOR="#e2e2e2";

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "color")
    public String color;


    @Ignore
    public Category() {}


    @Ignore
    public Category(String name) {
        this.name = name;
        this.color = DEFAULT_COLOR;
    }

    @Ignore
    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    // This is the constructor that Room will use.
    public Category(int id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public String toString() {
        return name;
    }


    /**
     * A required method for Parcelable
     * @return an int. This happens to be 0.
     */
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.id);
        out.writeString(this.name);
        out.writeString(this.color);
    }

    private Category(Parcel in) {
        id = in.readInt();
        name = in.readString();
        color = in.readString();
    }

    /**
     * A required method to regenerate this object.
     * All Parcelables must have a CREATOR that implements these two methods.
     */
    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>( ) {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[ size ];
        }
    };

}
