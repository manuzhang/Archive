package com.yilidi.core.webservice.user.response;

import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile implements Parcelable {
  private String userId;
  private String userPwd;
  private String userName;
  private String userNickName;
  private String userRealName;
  private String userPortraitUrl;
  private String userLikeCount;
  private boolean userVerified;
  private String userMobile;
  private String userEmail;
  private String userType;

  private UserProfile(Parcel source) {
    userId = source.readString();
    userPwd = source.readString();
    userName = source.readString();
    userNickName = source.readString();
    userRealName = source.readString();
    userPortraitUrl = source.readString();
    userLikeCount = source.readString();
    userVerified = source.readByte() == (byte) 1;
    userMobile = source.readString();
    userEmail = source.readString();
    userType = source.readString();
  }

  public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {

    @Override
    public UserProfile createFromParcel(Parcel source) {
      return new UserProfile(source);
    }

    @Override
    public UserProfile[] newArray(int size) {
      return new UserProfile[0];
    }
  };

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
    dest.writeString(userRealName);
    dest.writeString(userPortraitUrl);
    dest.writeString(userLikeCount);
    dest.writeByte((byte) (userVerified ? 1 : 0));
    dest.writeString(userMobile);
    dest.writeString(userEmail);
    dest.writeString(userType);
  }

  public String getUserRealName() {
    return userRealName;
  }

  public void setUserRealName(String userRealName) {
    this.userRealName = userRealName;
  }

  public String getUserMobile() {
    return userMobile;
  }

  public void setUserMobile(String userMobile) {
    this.userMobile = userMobile;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
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

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
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

  public String getUserLikeCount() {
    return userLikeCount;
  }

  public void setUserLikeCount(String userLikeCount) {
    this.userLikeCount = userLikeCount;
  }

  public boolean isUserVerified() {
    return userVerified;
  }

  public void setUserVerified(boolean userVerified) {
    this.userVerified = userVerified;
  }

  @Override
  public String toString() {
    return "UserContent{" +
            "userId='" + userId + '\'' +
            ", userPwd='" + userPwd + '\'' +
            ", userName='" + userName + '\'' +
            ", userNickName='" + userNickName + '\'' +
            ", userPortraitUrl='" + userPortraitUrl + '\'' +
            ", userLikeCount='" + userLikeCount + '\'' +
            ", userVerified=" + userVerified +
            '}';
  }
}
