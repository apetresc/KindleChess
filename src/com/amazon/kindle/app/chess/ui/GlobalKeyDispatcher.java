package com.amazon.kindle.app.chess.ui;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

import com.amazon.kindle.app.chess.Main;
import com.amazon.kindle.kindlet.event.KindleKeyCodes;
import com.codethesis.pgnparse.PGNMove;

public class GlobalKeyDispatcher implements KeyEventDispatcher {

  private final Main main;
  private final MainPanel mainPanel;
  
  public GlobalKeyDispatcher(Main main) {
    this.main = main;
    mainPanel = main.getMainPanel();
  }

  public boolean dispatchKeyEvent(final KeyEvent event) {
    if (event.isConsumed() || event.getID() == KeyEvent.KEY_RELEASED) return false;
    if (!main.boardHasFocus()) return false;
    
    switch (event.getKeyCode()) {
    case KindleKeyCodes.VK_FIVE_WAY_RIGHT:
      if (main.getBoardController() != null) {
        event.consume();
        PGNMove move = main.getBoardController().nextMove();
        if (move != null) {
          mainPanel.getCommentArea().setText(
                  move.getComment() == null ? move.getFullMove() : move.getComment());
          mainPanel.getProgressBar().incrementTick();
          
          mainPanel.getProgressBar().repaint();
          mainPanel.getChessBoardComponent().repaint();
          mainPanel.getCommentArea().repaint();
        }
      }
      return true;
    case KindleKeyCodes.VK_FIVE_WAY_LEFT:
      if (main.getBoardController() != null) {
        event.consume();
        PGNMove move = main.getBoardController().previousMove();
        if (move != null) {
          mainPanel.getCommentArea().setText("");
          mainPanel.getProgressBar().decrementTick();
          
          mainPanel.getProgressBar().repaint();
          mainPanel.getChessBoardComponent().repaint();
          mainPanel.getCommentArea().repaint();
        }
      }
    }
    return false;
  }

}
