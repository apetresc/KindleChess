package com.amazon.kindle.app.chess;

import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import com.amazon.kindle.app.chess.ui.GlobalKeyDispatcher;
import com.amazon.kindle.app.chess.ui.MainPanel;
import com.amazon.kindle.app.chess.ui.Menu;
import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;
import com.codethesis.pgnparse.PGNParseException;

public class Main extends AbstractKindlet {

  public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
  /** The directory containing the preloaded pgns */
  private static final String PGN_DIR = "/pgn/";

  private Container root;
  private ChessBoard board;
  private MainPanel mainPanel;
  private BoardController boardController;
  
  private static final Logger log = Logger.getLogger(Main.class);

  /**
   * Initializes the ChessBoard and the UI, and draws them to the screen.
   * 
   * @param context
   *          Provides access to the environment the Kindlet is running in.
   */
  public void create(final KindletContext context) {
    root = context.getRootContainer();

    board = new ChessBoard();

    mainPanel = new MainPanel(context, board);
    context.setMenu(new Menu(this));

    boardController = new BoardController(board);
    try {
      loadPgn(getClass().getResourceAsStream(PGN_DIR + "test.pgn"));
    } catch (IOException e) {
      log.error(e);
      e.printStackTrace();
    } catch (PGNParseException e) {
      log.error(e);
    }

    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
        new GlobalKeyDispatcher(this));

    root.add(mainPanel);
  }

  public void loadPgn(InputStream pgn) throws IOException, PGNParseException {
    board.init();
    boardController.loadPGN(pgn);
    mainPanel.getProgressBar().setTotalTicks(boardController.getCurrentMainBranchLength());
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
