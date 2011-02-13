package com.codethesis.pgnparse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PGNGameStub {
  private final Map tags;
  private final int offset;

  PGNGameStub(int offset) {
    this.offset = offset;
    tags = new HashMap();
  }

  void addTag(String key, String value) {
    tags.put(key, value);
  }

  void removeTag(String key) {
    tags.remove(key);
  }

  public String getTag(String key) {
    return (String) tags.get(key);
  }

  public Iterator getTagKeysIterator() {
    return tags.keySet().iterator();
  }

  public boolean containsTagKey(String key) {
    return tags.containsKey(key);
  }

  public int getTagsCount() {
    return tags.size();
  }

  public int getOffset() {
    return offset;
  }
}
