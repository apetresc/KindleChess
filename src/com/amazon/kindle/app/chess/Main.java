package com.amazon.kindle.app.chess;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.apache.log4j.Logger;

import com.amazon.kindle.app.chess.ui.KChessBoardComponent;
import com.amazon.kindle.app.chess.ui.KCommentArea;
import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.ui.KButton;
import com.amazon.kindle.kindlet.ui.KLabel;
import com.amazon.kindle.kindlet.ui.KLabelMultiline;
import com.amazon.kindle.kindlet.ui.KPanel;
import com.amazon.kindle.kindlet.ui.KMenu;
import com.amazon.kindle.kindlet.ui.KMenuItem;

public class Main extends AbstractKindlet {

  public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
  /** The directory containing the preloaded pgns */
  private static final String PGN_DIR = "/pgn/";

  private Container root;

  private KLabel titleLabel;
  private KLabel descriptionLabel;
  private KChessBoardComponent boardComponent;
  private KLabelMultiline commentComponent;

  private ChessBoard board;
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
    final KPanel mainPanel = new KPanel(new GridBagLayout());

    board = new ChessBoard();
    board.init();

    GridBagConstraints gc = new GridBagConstraints();

    titleLabel = new KLabel();
    titleLabel.setFont(new Font(null, Font.BOLD, 25));
    gc.gridx = 0;
    gc.gridy = 0;
    gc.weighty = 0.0;
    gc.anchor = GridBagConstraints.NORTH;
    mainPanel.add(titleLabel, gc);
    
    descriptionLabel = new KLabel();
    descriptionLabel.setFont(new Font(null, Font.BOLD, 18));
    gc.gridy = 1;
    mainPanel.add(descriptionLabel, gc);
    
    boardComponent = new KChessBoardComponent(context, board, 75, true, false, false);
    boardComponent.setFocusable(true);
    gc.gridy = 2;
    gc.insets = new Insets(0, 10, 0, 0);
    mainPanel.add(boardComponent, gc);
    
    commentComponent = new KCommentArea(ChessBoard.SIZE * boardComponent.getSquareSize() + 4, 200);
    commentComponent.setFocusable(false);
    gc.gridy = 3;
    gc.weighty = 1.0;
    gc.fill = GridBagConstraints.BOTH;
    gc.insets = new Insets(0, 10, 0, 10);
    mainPanel.add(commentComponent);

    // commentTextArea intended for pgn comments. Currently just shows the move
    commentComponent = new KLabelMultiline();
    // menu to allow board resizing for testing
    final KMenu menu = new KMenu();
    class BoardResizeMenuItem extends KMenuItem {
      public BoardResizeMenuItem(final int percent) {
        super(percent + "%");
        addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            boardComponent.setSquareSize(percent);
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
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (com.codethesis.pgnparse.PGNParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    KButton button = new KButton("Next Move");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
          //commentComponent.setText(move);
          commentComponent.repaint();
          boardController.nextMove();
          boardComponent.repaint();
      }
    });
    gc.gridy = gc.gridy + 1;
    gc.fill = GridBagConstraints.HORIZONTAL;
    mainPanel.add(button, gc);

    root.add(mainPanel);
  }

  public BoardController getBoardController() {
    return boardController;
  }
  public boolean boardHasFocus() {
    return true;
  }
}
