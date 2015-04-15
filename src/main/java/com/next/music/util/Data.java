package com.next.music.util;

public class Data
{
  private boolean success;
  private Object data;

  private Data(boolean success, Object data)
  {
    this.success = success;
    this.data = data;
  }

  public static final Data success(Object data)
  {
    return new Data(true, data);
  }

  public static final Data failure(Object data)
  {
    return new Data(false, data);
  }

  public boolean isSuccess()
  {
    return this.success;
  }

  public void setSuccess(boolean success)
  {
    this.success = success;
  }

  public Object getData()
  {
    return this.data;
  }

  public void setData(Object data)
  {
    this.data = data;
  }
}