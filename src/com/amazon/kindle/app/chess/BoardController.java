package com.amazon.kindle.app.chess;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import com.codethesis.pgnparse.MalformedMoveException;
import com.codethesis.pgnparse.PGNGame;
import com.codethesis.pgnparse.PGNMove;
import com.codethesis.pgnparse.PGNParseException;
import com.codethesis.pgnparse.PGNSource;

public class BoardController {

	private ChessBoard board;
	private PGNGame    pgnGame;
	private Iterator   pgnIterator;
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

	public PGNMove nextMove() {
	  if (pgnIterator == null || !pgnIterator.hasNext()) {
	    return null;
	  }
	  PGNMove move = (PGNMove) pgnIterator.next();
	  board.move(move);
	  currentMoveNumber++;
	  return move;
	}

	public int getCurrentMoveNumber() {
	  return currentMoveNumber;
	}
}
