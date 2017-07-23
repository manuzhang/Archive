package com.yilidi.core.webservice.wish.response;

import com.yilidi.core.webservice.BaseResponse;

public class WishResponse extends BaseResponse {
  private WishItem content;

  public WishItem getContent() {
    return this.content;
  }

  public void setContent(WishItem content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "WishResponse{" +
            "content=" + content + "," +
            super.toString() +
            '}';
  }
}

