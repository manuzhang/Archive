package com.yilidi.core.webservice;

public abstract class BaseResponse {

  private int status;
  private int size;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public String toString() {
    return "status=" + status + ","
            + "size=" + size;
  }
}
