package com.amazon.kindle.app.chess.ui;

import java.util.Iterator;

import com.amazon.kindle.kindlet.ui.KLabelMultiline;
import com.codethesis.pgnparse.PGNGame;
import com.codethesis.pgnparse.PGNMove;

public class KChessMoveList extends KLabelMultiline {
  private static final long serialVersionUID = 6476691257817470682L;

  public KChessMoveList() {
    
  }

  public KChessMoveList(PGNGame game) {
    setGame(game);
  }

  public void setGame(PGNGame game) {
    Iterator it = game.getMovesIterator();
    String moveList = "";
    int move = 1;
    while (it.hasNext()) {
      moveList += move + ". " + ((PGNMove) it.next()).getFullMove();
      if (it.hasNext()) {
        moveList += " " + ((PGNMove) it.next()).getFullMove() + " ";
      }
      move++;
    }
    this.setText(moveList);
  }
}
