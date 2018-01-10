package com.example.boyko.mike.groceries.db.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A Quantity Type is used to describe the grouping
 * of Items. For example, a dozen eggs, or a package of
 * dried pasta, or a can of tuna.
 */

@Entity(tableName = "quantity_type")
public class QuantityType implements Parcelable {

    public static final String TAG = "QuantityType";

    public static final String NONE = "none";


    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "single")
    public String single;

    @ColumnInfo(name = "plural")
    public String plural;


    @Ignore
    public QuantityType() {}


    // This is the constructor that Room will use.
    public QuantityType(int id, String single, String plural) {
        this.id = id;
        this.single = single;
        this.plural = plural;
    }


    public String toString() {
        return single;
    }

    public String getPlural() {
        return plural;
    }


    public String getSingle() {
        return single;
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
        out.writeString(this.single);
        out.writeString(this.plural);
    }

    private QuantityType(Parcel in) {
        id = in.readInt();
        single = in.readString();
        plural = in.readString();
    }

    /**
     * A required method to regenerate this object.
     * All Parcelables must have a CREATOR that implements these two methods.
     */
    public static final Parcelable.Creator<QuantityType> CREATOR = new Parcelable.Creator<QuantityType>( ) {
        public QuantityType createFromParcel(Parcel in) {
            return new QuantityType(in);
        }

        public QuantityType[] newArray(int size) {
            return new QuantityType[ size ];
        }
    };

}
