package com.amazon.kindle.app.chess;

import java.io.File;
import java.io.FilenameFilter;

public class PgnFilenameFilter implements FilenameFilter {

  public boolean accept(File dir, String name) {
    return name.endsWith(".pgn");
  }

}
