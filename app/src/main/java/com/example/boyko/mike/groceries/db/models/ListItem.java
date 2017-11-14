package com.example.boyko.mike.groceries.db.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcelable;
import android.os.Parcel;

/**
 *
 * This is a Grocery List listItem.
 *
 * Created by mike on 11/6/17.
 */

@Entity(foreignKeys = {
        @ForeignKey(
                entity = QuantityType.class,
                parentColumns = "id",
                childColumns = "quantityId",
                onDelete = ForeignKey.SET_NULL
        ),
        @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = ForeignKey.SET_NULL
        )
})
public class ListItem implements Parcelable {

    public static final String TAG = "ListItem";

    @PrimaryKey(autoGenerate = true)
    public Long id;                // The internal database id

    @ColumnInfo(name = "name")
    public String name;           // The name of the ListItem

    @ColumnInfo(name = "quantity")
    public int quantity;          // How much of the ListItem to get

    @ColumnInfo(name = "quantity_type_id")
    public Long quantityTypeId;

    @Ignore
    public QuantityType quantityType;   // The unit that the ListItem comes in

    @ColumnInfo(name="category_id")
    public Long categoryId;

    @Ignore
    public Category category;       // The category for the ListItem

    @ColumnInfo(name = "coupon")
    public boolean coupon;        // Whether or not there is a coupon

    @ColumnInfo(name = "notes")
    public String notes;          // If there are any notes associated with the listItem

    @ColumnInfo(name = "checked")
    public boolean checked;       // Whether or not the ListItem has been checked off


    /**
     *  The listView needs this method to determine what to return.
     * @return A string representation of the listItem for a list.
     */
    public String toString() {
        String str = this.name;

        if (quantity > 1 || (quantity == 1 && quantityType != null)) {
            str = str + " (" + quantity;
            if (quantityType != null) {
                if (quantity > 1) {
                    str = str + " " + quantityType.plural;
                }
                else {
                    str = str + " " + quantityType.single;
                }
            }
            str = str + ")";
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
       out.writeLong(this.quantityTypeId);
       out.writeParcelable(this.quantityType, 0);
       out.writeLong(this.categoryId);
       out.writeParcelable(this.category, 0);
       out.writeByte((byte) (this.coupon ? 1 : 0));
       out.writeString(this.notes);
       out.writeByte((byte) (this.checked ? 1 : 0));
    }


    @Ignore
    private ListItem(Parcel in) {
        name = in.readString();
        quantity = in.readInt();
        quantityTypeId = in.readLong();
        quantityType = in.readParcelable(QuantityType.class.getClassLoader());
        categoryId = in.readLong();
        category = in.readParcelable(Category.class.getClassLoader());
        coupon = (in.readByte() != 0);
        notes = in.readString();
        checked = (in.readByte() != 0);
    }


    @Ignore
    public ListItem(String name) {
        this.name = name;
        quantity = 1;
        quantityTypeId = null;
        quantityType = null;
        categoryId = null;
        category = null;
        coupon = false;
        notes = null;
        checked = false;
    }


    // This is the constructor that Room will use.
    public ListItem(Long id, String name, int quantity, Long quantityTypeId, Long categoryId, boolean coupon, String notes, boolean checked) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.quantityTypeId = quantityTypeId;
        this.categoryId = categoryId;
        this.coupon = coupon;
        this.notes = notes;
        this.checked = checked;
    }

    /**
     * A required method to regenerate this object.
     * All Parcelables must have a CREATOR that implements these two methods.
     */
    public static final Parcelable.Creator<ListItem> CREATOR = new Parcelable.Creator<ListItem>( ) {
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        public ListItem[] newArray(int size) {
            return new ListItem[ size ];
        }
    };
}
