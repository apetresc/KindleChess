/*
 * This file is part of PGNParse.
 *
 * PGNParse is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PGNParse is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PGNParse.  If not, see <http://www.gnu.org/licenses/>. 
 */
package com.codethesis.pgnparse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * 
 * @author Deyan Rizov
 * 
 */
public class PGNSource {

  private String source;

  public PGNSource(String pgn) {
    if (pgn == null) {
      throw new NullPointerException("PGN data is null");
    }

    this.source = pgn;
  }

  public PGNSource(File file) throws IOException {
    this(new FileInputStream(file));
  }

  public PGNSource(URL url) throws IOException {
    this(url.openStream());
  }

  public PGNSource(InputStream inputStream) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    StringBuffer buffer = new StringBuffer();

    while ((line = br.readLine()) != null) {
      buffer.append(line + "\r\n");
    }

    br.close();
    this.source = buffer.toString();
  }

  public String toString() {
    return source;
  }

  public List listGames() throws PGNParseException, IOException, NullPointerException,
          MalformedMoveException {
    return PGNParser.parse(source);
  }

  public List listGames(boolean force) throws PGNParseException, IOException, NullPointerException,
          MalformedMoveException {
    return PGNParser.parse(source, force);
  }

  public List listGameStubs() throws PGNParseException, IOException {
    return PGNParser.parseStubs(source);
  }

  public List listGameStubs(boolean force) throws PGNParseException, IOException {
    return PGNParser.parseStubs(source, force);
  }

  public PGNGame getGameFromStub(PGNGameStub stub)
      throws PGNParseException, IOException, MalformedMoveException {

    return PGNParser.parseSingleGame(source, stub.getOffset());
  }
}
