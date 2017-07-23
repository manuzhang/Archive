package com.yilidi.core.webservice.wish.response;

import com.yilidi.core.webservice.BaseResponse;

import java.util.List;

public class WishListResponse extends BaseResponse {
  private List<WishItem> content;

  public List<WishItem> getContent() {
    return this.content;
  }

  public void setContent(List<WishItem> content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "WishListResponse{" +
            "contents=" + content + "," +
            super.toString() +
            '}';
  }
}
