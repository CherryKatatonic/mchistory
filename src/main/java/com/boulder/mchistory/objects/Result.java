package com.boulder.mchistory.objects;

import java.util.List;

// [START example]
public class Result<K> {

  public String cursor;
  public List<K> result;

  public Result(List<K> result, String cursor) {
    this.result = result;
    this.cursor = cursor;
  }

  public Result(List<K> result) {
    this.result = result;
    this.cursor = null;
  }
}
// [END example]
