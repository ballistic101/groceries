package com.example.boyko.mike.groceries;

import android.os.Parcelable;
import android.os.Parcel;

/**
 *
 * This is a Grocery List item.
 *
 * Created by mike on 11/6/17.
 */

public class Item implements Parcelable {

    public static final String TAG = "Item";

    public String name;           // The name of the Item
    public int quantity;          // How much of the Item to get
    public String quantityType;   // The unit that the Item comes in
    public String category;       // The category for the Item
    public boolean coupon;        // Whether or not there is a coupon
    public String notes;          // If there are any notes associated with the item

    /**
     *  The listView needs this method to determine what to return.
     * @return A string representation of the item for a list.
     */
    public String toString() {
        String str = this.name;

        if (quantity > 1) {
            str = str + " (" + quantity + ")";
        }

        if (notes != null) {
            str = str + "\n" + notes;
        }
        
        return str;
    }

    /**
     * A required method for Parcelable
     * @return an int. This happens to be 0.
     */
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
       out.writeString(this.name);
       out.writeInt(this.quantity);
       out.writeString(this.quantityType);
       out.writeString(this.category);
       out.writeByte((byte) (this.coupon ? 1 : 0));
       out.writeString(this.notes);
    }

    private Item(Parcel in) {
        name = in.readString();
        quantity = in.readInt();
        quantityType = in.readString();
        category = in.readString();
        coupon = (in.readByte() != 0);
        notes = in.readString();
    }

    public Item(String name) {
        this.name = name;
        quantity = 1;
        quantityType = null;
        category = null;
        coupon = false;
        notes = null;
    }

    /**
     * A required method to regenerate this object.
     * All Parcelables must have a CREATOR that implements these two methods.
     */
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>( ) {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[ size ];
        }
    };
}
