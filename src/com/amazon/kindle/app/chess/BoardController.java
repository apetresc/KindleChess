package com.amazon.kindle.app.chess;

import java.io.IOException;
import java.io.InputStream;
import java.util.ListIterator;

import com.codethesis.pgnparse.MalformedMoveException;
import com.codethesis.pgnparse.PGNGame;
import com.codethesis.pgnparse.PGNGameStub;
import com.codethesis.pgnparse.PGNMove;
import com.codethesis.pgnparse.PGNParseException;
import com.codethesis.pgnparse.PGNSource;

public class BoardController {

  private ChessBoard board;
  private PGNGame pgnGame;
  private ListIterator pgnIterator;
  private int currentMoveNumber;

  public BoardController(ChessBoard board) {
    this.board = board;
  }

  public void loadPGN(InputStream in) throws IOException, PGNParseException {
    PGNSource source = new PGNSource(in);
    try {
      pgnGame = (PGNGame) source.listGames().get(0);
      pgnIterator = pgnGame.getMovesIterator();
      currentMoveNumber = 0;
    } catch (MalformedMoveException e) {
      throw new PGNParseException(e);
    }
  }

  public void loadPGN(PGNSource pgnSource, PGNGameStub stub) throws IOException, PGNParseException {
    try {
      pgnGame = pgnSource.getGameFromStub(stub);
      pgnIterator = pgnGame.getMovesIterator();
      currentMoveNumber = 0;
    } catch (MalformedMoveException e) {
      throw new PGNParseException(e);
    }
  }

  public PGNMove nextMove() {
    if (pgnIterator == null || !pgnIterator.hasNext()) {
      return null;
    }
    PGNMove move = (PGNMove) pgnIterator.next();
    board.move(move);
    currentMoveNumber++;
    return move;
  }

  public PGNMove previousMove() {
    if (pgnIterator == null || !pgnIterator.hasPrevious()) {
      return null;
    }
    PGNMove move = (PGNMove) pgnIterator.previous();
    board.undoMove(move);
    currentMoveNumber--;
    return move;
  }

  public PGNMove peekNextMove() {
    if (pgnIterator == null || !pgnIterator.hasNext()) {
      return null;
    }
    PGNMove move = (PGNMove) pgnIterator.next();
    pgnIterator.previous();
    return move;
  }

  public PGNMove peekPreviousMove() {
    if (pgnIterator == null || !pgnIterator.hasPrevious()) {
      return null;
    }
    PGNMove move = (PGNMove) pgnIterator.previous();
    pgnIterator.next();
    return move;
  }

  public int getCurrentMoveNumber() {
    return currentMoveNumber;
  }

  public int getCurrentMainBranchLength() {
    return pgnGame.getMovesCount();
  }
}
