package com.amazon.kindle.app.chess;

import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.apache.log4j.Logger;
import com.amazon.kindle.app.chess.ui.GlobalKeyDispatcher;
import com.amazon.kindle.app.chess.ui.MainPanel;
import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.ui.KMenu;
import com.amazon.kindle.kindlet.ui.KMenuItem;

public class Main extends AbstractKindlet {

  public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
  /** The directory containing the preloaded pgns */
  private static final String PGN_DIR = "/pgn/";

  private Container root;
  private ChessBoard board;
  private MainPanel mainPanel;
  private BoardController boardController;
  
  static Logger log = Logger.getLogger(Main.class);

  /**
   * Initializes the ChessBoard and the UI, and draws them to the screen.
   * 
   * @param context
   *          Provides access to the environment the Kindlet is running in.
   */
  public void create(final KindletContext context) {
    root = context.getRootContainer();

    board = new ChessBoard();
    board.init();

    mainPanel = new MainPanel(context, board);

    // menu to allow board resizing for testing
    final KMenu menu = new KMenu();
    class BoardResizeMenuItem extends KMenuItem {
      public BoardResizeMenuItem(final int percent) {
        super(percent + "%");
        addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            mainPanel.getChessBoardComponent().setSquareSize(percent);
            root.invalidate();
          }
        });
      }
    }

    menu.add(new BoardResizeMenuItem(100));
    menu.add(new BoardResizeMenuItem(75));
    menu.add(new BoardResizeMenuItem(50));
    menu.add(new BoardResizeMenuItem(25));
    context.setMenu(menu);

    boardController = new BoardController(board);
    try {
      boardController.loadPGN(getClass().getResourceAsStream(PGN_DIR + "test.pgn"));
      mainPanel.getProgressBar().setTotalTicks(boardController.getCurrentMainBranchLength());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (com.codethesis.pgnparse.PGNParseException e) {
      e.printStackTrace();
    }

    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
        new GlobalKeyDispatcher(this));

    root.add(mainPanel);
  }

  public MainPanel getMainPanel() {
    return mainPanel;
  }

  public BoardController getBoardController() {
    return boardController;
  }

  public boolean boardHasFocus() {
    return true;
  }
}
