package com.yilidi.core.webservice.wish.response;

import android.os.Parcel;
import android.os.Parcelable;


public class WishItem implements Parcelable {
  private Creater creater;
  private int wishPositiveCount;
  private int wishId;
  private String createrId;
  private String wishDesc;

  public static final Parcelable.Creator<WishItem> CREATOR =  new Parcelable.Creator<WishItem>() {

    @Override
    public WishItem createFromParcel(Parcel source) {
      return new WishItem(source);
    }

    @Override
    public WishItem[] newArray(int size) {
      return new WishItem[0];
    }
  };

  public WishItem(){
  }

  private WishItem(Parcel source) {
    creater = source.readParcelable(Creater.class.getClassLoader());
    wishPositiveCount = source.readInt();
    wishId = source.readInt();
    createrId = source.readString();
    wishDesc = source.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(creater, flags);
    dest.writeInt(wishPositiveCount);
    dest.writeInt(wishId);
    dest.writeString(createrId);
    dest.writeString(wishDesc);
  }


  public Creater getCreater() {
    return creater;
  }

  public void setCreater(Creater creater) {
    this.creater = creater;
  }

  public int getWishPositiveCount() {
    return wishPositiveCount;
  }

  public void setWishPositiveCount(int wishPositiveCount) {
    this.wishPositiveCount = wishPositiveCount;
  }

  public int getWishId() {
    return wishId;
  }

  public void setWishId(int wishId) {
    this.wishId = wishId;
  }

  public String getCreaterId() {
    return createrId;
  }

  public void setCreaterId(String createrId) {
    this.createrId = createrId;
  }

  public String getWishDesc() {
    return wishDesc;
  }

  public void setWishDesc(String wishDesc) {
    this.wishDesc = wishDesc;
  }

  @Override
  public String toString() {
    return "Content{" +
            "creater=" + creater +
            ", wishPositiveCount=" + wishPositiveCount +
            ", wishId=" + wishId +
            ", createrId='" + createrId + '\'' +
            ", wishDesc='" + wishDesc + '\'' +
            '}';
  }


}
