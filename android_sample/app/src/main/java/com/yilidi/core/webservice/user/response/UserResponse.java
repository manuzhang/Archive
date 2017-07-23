package com.yilidi.core.webservice.user.response;

import com.yilidi.core.webservice.BaseResponse;

public class UserResponse extends BaseResponse {
  private UserProfile content;

  public UserProfile getContent() {
    return content;
  }

  public void setContent(UserProfile content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "UserResponse{" +
            "content=" + content + "," +
            super.toString() +
            '}';
  }
}
