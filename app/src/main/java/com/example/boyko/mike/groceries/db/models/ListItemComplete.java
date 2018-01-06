package com.example.boyko.mike.groceries.db.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.os.Parcel;
import android.os.Parcelable;

public class ListItemComplete implements Parcelable {

    public static final String TAG = "ListItemComplete";

    @Embedded(prefix="li_")
    public ListItem listItem;

    @Embedded(prefix="qt_")
    public QuantityType quantityType;

    @Embedded(prefix="cat_")
    public Category category;

    /**
     *  The listView needs this method to determine what to return.
     * @return A string representation of the listItem for a list.
     */
    public String toString() {
        String str = listItem.name;

        if (listItem.quantity > 1 || (listItem.quantity == 1 && quantityType != null)) {
            str = str + " (" + listItem.quantity;
            if (quantityType != null) {
                if (listItem.quantity > 1) {
                    str = str + " " + quantityType.plural;
                }
                else {
                    str = str + " " + quantityType.single;
                }
            }
            str = str + ")";
        }

        if (listItem.notes != null) {
            str = str + "\n" + listItem.notes;
        }

        return str;
    }


    public ListItemComplete() {
        this.listItem = null;
        this.quantityType = null;
        this.category = null;
    }


    public ListItemComplete(String name) {
        this.listItem = new ListItem(name);
        this.quantityType = null;
        this.category = null;
    }


    /**
     * A required method for Parcelable
     * @return an int. This happens to be 0.
     */
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.listItem, 0);
        out.writeParcelable(this.quantityType, 0);
        out.writeParcelable(this.category, 0);
    }


    private ListItemComplete(Parcel in) {
        listItem = in.readParcelable(ListItem.class.getClassLoader());
        quantityType = in.readParcelable(QuantityType.class.getClassLoader());
        category = in.readParcelable(Category.class.getClassLoader());
    }


    /**
     * A required method to regenerate this object.
     * All Parcelables must have a CREATOR that implements these two methods.
     */
    public static final Parcelable.Creator<ListItemComplete> CREATOR = new Parcelable.Creator<ListItemComplete>( ) {
        public ListItemComplete createFromParcel(Parcel in) {
            return new ListItemComplete(in);
        }

        public ListItemComplete[] newArray(int size) {
            return new ListItemComplete[ size ];
        }
    };

}
