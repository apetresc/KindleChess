package com.amazon.kindle.app.chess.ui;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

import com.amazon.kindle.app.chess.Main;
import com.amazon.kindle.kindlet.event.KindleKeyCodes;

public class GlobalKeyDispatcher implements KeyEventDispatcher {

  private final Main main;
  
  public GlobalKeyDispatcher(Main main) {
    this.main = main;
  }

  public boolean dispatchKeyEvent(final KeyEvent event) {
    if (event.isConsumed() || event.getID() == KeyEvent.KEY_RELEASED) return false;
    if (!main.boardHasFocus()) return false;
    
    switch (event.getKeyCode()) {
    case KindleKeyCodes.VK_FIVE_WAY_RIGHT:
      if (main.getBoardController() != null) {
        event.consume();
      }
    }
    return false;
  }

}
