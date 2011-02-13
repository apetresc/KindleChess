package com.amazon.kindle.app.chess;

import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import com.amazon.kindle.app.chess.ui.GlobalKeyDispatcher;
import com.amazon.kindle.app.chess.ui.MainPanel;
import com.amazon.kindle.app.chess.ui.Menu;
import com.amazon.kindle.app.chess.ui.PgnSelectionPanel;
import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;
import com.codethesis.pgnparse.PGNParseException;

public class Main extends AbstractKindlet {

  public static final int MAIN_PANEL = 0;
  public static final int PGN_SELECTION_PANEL = 1;
  public static final int GAME_SELECTION_PANEL = 2;

  public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
  /** The directory containing the preloaded pgns */
  private static final String PGN_DIR = "/pgn/";

  private KindletContext context;
  private ChessBoard board;
  private MainPanel mainPanel;
  private PgnSelectionPanel pgnSelectionPanel;
  private BoardController boardController;
  
  private static final Logger log = Logger.getLogger(Main.class);

  /**
   * Initializes the ChessBoard and the UI, and draws them to the screen.
   * 
   * @param context
   *          Provides access to the environment the Kindlet is running in.
   */
  public void create(final KindletContext context) {
    this.context = context;

    board = new ChessBoard();

    mainPanel = new MainPanel(context, board);
    context.setMenu(new Menu(this));

    boardController = new BoardController(board);
    log.info("About to begin parsing test.pgn");
    try {
      loadPgn(getClass().getResourceAsStream(PGN_DIR + "test.pgn"));
    } catch (IOException e) {
      log.error(e);
      e.printStackTrace();
    } catch (PGNParseException e) {
      log.error(e);
    }
    log.info("Done parsing test.pgn");

    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
        new GlobalKeyDispatcher(this));

    context.getRootContainer().add(mainPanel);
  }

  public void loadPgn(InputStream pgn) throws IOException, PGNParseException {
    board.init();
    boardController.loadPGN(pgn);
    mainPanel.getProgressBar().setCurrentTick(0);
    mainPanel.getProgressBar().setTotalTicks(boardController.getCurrentMainBranchLength());
  }

  public void setActivePanel(int panel) {
    switch (panel) {
    case MAIN_PANEL:
      if (context.getRootContainer().getComponent(0) != mainPanel) {
        context.getRootContainer().remove(0);
        context.getRootContainer().add(mainPanel);
      }
      break;
    case PGN_SELECTION_PANEL:
      if (context.getRootContainer().getComponent(0) != pgnSelectionPanel) {
        if (pgnSelectionPanel == null) {
          instantiatePgnSelectionPanel();
        }
        context.getRootContainer().remove(0);
        context.getRootContainer().add(pgnSelectionPanel);
      }
      break;
    }
    context.getRootContainer().invalidate();
    context.getRootContainer().repaint();
  }

  private void instantiatePgnSelectionPanel() {
    if (pgnSelectionPanel == null) {
      File pgnDir = new File(context.getHomeDirectory(), "pgn");
      if (!pgnDir.exists()) {
        pgnDir.mkdir();
      }
      final File[] pgnFiles = pgnDir.listFiles(new PgnFilenameFilter());
      String[] pgnList = new String[pgnFiles.length];

      for (int i = 0; i < pgnFiles.length; i++) {
        pgnList[i] = pgnFiles[i].getName();
      }
      log.info("Found " + pgnFiles.length + " PGNs");
      pgnSelectionPanel = new PgnSelectionPanel(this, pgnFiles, pgnList);
    }
  }

  public MainPanel getMainPanel() {
    return mainPanel;
  }

  public BoardController getBoardController() {
    return boardController;
  }

  public boolean boardHasFocus() {
    return context.getRootContainer().getComponent(0) == mainPanel;
  }
}
