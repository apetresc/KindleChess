package com.amazon.kindle.app.chess.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.amazon.kindle.app.chess.Main;
import com.amazon.kindle.kindlet.ui.KMenu;
import com.amazon.kindle.kindlet.ui.KMenuItem;

public class Menu extends KMenu {

  private final Main main;

  private class BoardResizeMenuItem extends KMenuItem {
    public BoardResizeMenuItem(final int percent) {
      super(percent + "%");
      addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          main.getMainPanel().getChessBoardComponent().setSquareSize(percent);
          main.getMainPanel().invalidate();
        }
      });
    }
  }

  public Menu(final Main main) {
    this.main = main;

    KMenuItem choosePgnMenuItem = new KMenuItem("Choose PGN...");
    choosePgnMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        main.setActivePanel(Main.PGN_SELECTION_PANEL);
      }
    });

    add(choosePgnMenuItem);
    add(new BoardResizeMenuItem(100));
    add(new BoardResizeMenuItem(75));
    add(new BoardResizeMenuItem(50));
    add(new BoardResizeMenuItem(25));
  }
}
