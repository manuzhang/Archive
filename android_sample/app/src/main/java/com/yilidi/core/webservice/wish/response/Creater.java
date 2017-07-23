package com.yilidi.core.webservice.wish.response;

import android.os.Parcel;
import android.os.Parcelable;

public class Creater implements Parcelable {
  private String userId;
  private String userPwd;
  private String userNickName;
  private String userPortraitUrl;
  private int userLikeCount;
  private boolean userVerified;
  private int userType;
  private String userName;

  private Creater(Parcel source) {
    userId = source.readString();
    userPwd = source.readString();
    userName = source.readString();
    userNickName = source.readString();
    userPortraitUrl = source.readString();
    userLikeCount = source.readInt();
    userVerified = source.readByte() == (byte) 1;
    userType = source.readInt();

  }

  public Creater(){
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(userId);
    dest.writeString(userPwd);
    dest.writeString(userName);
    dest.writeString(userNickName);
    dest.writeString(userPortraitUrl);
    dest.writeInt(userLikeCount);
    dest.writeByte((byte) (userVerified ? 1 : 0));
    dest.writeInt(userType);
  }

  public static final Parcelable.Creator<Creater> CREATOR = new Parcelable.Creator<Creater>() {

    @Override
    public Creater createFromParcel(Parcel source) {
      return new Creater(source);
    }

    @Override
    public Creater[] newArray(int size) {
      return new Creater[0];
    }
  };

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserPwd() {
    return userPwd;
  }

  public void setUserPwd(String userPwd) {
    this.userPwd = userPwd;
  }

  public String getUserNickName() {
    return userNickName;
  }

  public void setUserNickName(String userNickName) {
    this.userNickName = userNickName;
  }

  public String getUserPortraitUrl() {
    return userPortraitUrl;
  }

  public void setUserPortraitUrl(String userPortraitUrl) {
    this.userPortraitUrl = userPortraitUrl;
  }

  public int getUserLikeCount() {
    return userLikeCount;
  }

  public void setUserLikeCount(int userLikeCount) {
    this.userLikeCount = userLikeCount;
  }

  public boolean isUserVerified() {
    return userVerified;
  }

  public void setUserVerified(boolean userVerified) {
    this.userVerified = userVerified;
  }

  public int getUserType() {
    return userType;
  }

  public void setUserType(int userType) {
    this.userType = userType;
  }

  @Override
  public String toString() {
    return "Creater{" +
            "userId='" + userId + '\'' +
            ", userPwd='" + userPwd + '\'' +
            ", userNickName='" + userNickName + '\'' +
            ", userPortraitUrl='" + userPortraitUrl + '\'' +
            ", userLikeCount=" + userLikeCount +
            ", userVerified=" + userVerified +
            ", userType=" + userType +
            ", userName='" + userName + '\'' +
            '}';
  }


}
