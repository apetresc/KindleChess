package com.amazon.kindle.app.chess.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazon.kindle.app.chess.ChessBoard;
import com.amazon.kindle.app.chess.ChessConstants;
import com.amazon.kindle.app.chess.Main;
import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.ui.KComponent;
import com.amazon.kindle.kindlet.ui.KindletUIResources.KColorName;

public class KChessBoardComponent extends KComponent {
  private static final long serialVersionUID = -8744250914120489025L;

  /** The directory containing the piece set images. */
  private static final String IMG_DIR = "/img/";
  /** The image format of the piece set images. */
  private static final String IMG_EXT = ".png";
  private static Color darkSquareColor;
  private static Color lightSquareColor;

  /** List of pieces. */
  private static int[] pieces = { ChessBoard.WHITE_PAWN, ChessBoard.WHITE_ROOK,
      ChessBoard.WHITE_KNIGHT, ChessBoard.WHITE_BISHOP, ChessBoard.WHITE_QUEEN,
      ChessBoard.WHITE_KING, ChessBoard.BLACK_PAWN, ChessBoard.BLACK_ROOK,
      ChessBoard.BLACK_KNIGHT, ChessBoard.BLACK_BISHOP, ChessBoard.BLACK_QUEEN,
      ChessBoard.BLACK_KING };
  /** A mapping from piece Strings to Images */
  private static Map pieceSetMap;

  private ChessBoard board;

  private int squareSize;
  /**
   * Sets whether a graphical or textual representation of the chess pieces is
   * used
   */
  private boolean useImage;
  /**
   * Sets whether to display the coordinates of each square in the top-right
   * corner
   */
  private boolean showCoordinates;
  /**
   * Sets whether the coordinates are displayed as algebraic or numeric
   * coordinates
   */
  private boolean useAlgebraicCoordinates;

  private static Logger log = Logger.getLogger(KChessBoardComponent.class);

  public KChessBoardComponent(KindletContext context, ChessBoard board, int percent,
      boolean useImage, boolean showCoordinates, boolean useAlgebraicCoordinates) {

    this.board = board;
    this.useImage = useImage;
    this.showCoordinates = showCoordinates;
    this.useAlgebraicCoordinates = useAlgebraicCoordinates;

    setSquareSize(percent);
    lightSquareColor = context.getUIResources().getBackgroundColor(KColorName.WHITE);
    darkSquareColor = context.getUIResources().getBackgroundColor(KColorName.GRAY_09);
  }

  /**
   * Loads the required resources from the application JAR, such as the piece
   * set images.
   */
  private void initResources() {
    pieceSetMap = new HashMap();
    MediaTracker mediatracker = new MediaTracker(this);
    for (int i = 0; i < pieces.length; i++) {
      Image image = Toolkit.getDefaultToolkit()
          .createImage(getClass().getResource(IMG_DIR + pieces[i] + IMG_EXT))
          .getScaledInstance(squareSize, squareSize, Image.SCALE_FAST);
      pieceSetMap.put(new Integer(pieces[i]), image);
      mediatracker.addImage(image, 0);
    }

    try {
      mediatracker.waitForAll();
    } catch (final InterruptedException e) {
      log.error("Error occured in Image loading thread.", e);
    }

  }
  
  public Dimension getPreferredSize() {
    return new Dimension(ChessBoard.SIZE * squareSize + 2, ChessBoard.SIZE * squareSize + 2);
  }

  public Dimension getMinimumSize() {
    return getPreferredSize();
  }
  
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }

  public int getSquareSize() {
    return squareSize;
  }

  public void setSquareSize(int percent) {
    int oldSquareSize = squareSize;
    squareSize = (Main.SCREEN_SIZE.width < Main.SCREEN_SIZE.height ?
        Main.SCREEN_SIZE.width : Main.SCREEN_SIZE.height) * percent / 100 / ChessBoard.SIZE;
    if (oldSquareSize != squareSize) {
      initResources();
    }
  }

  public void paint(Graphics g) {
    for (int y = 0; y < ChessBoard.SIZE; y++) {
      for (int x = 0; x < ChessBoard.SIZE; x++) {
        byte square = board.getSquare(x, y);
        Color foregroundColor = null, backgroundColor = null;
        switch (board.getSquareColor(x, y)) {
        case ChessBoard.BLACK:
          foregroundColor = darkSquareColor;
          backgroundColor = lightSquareColor;
          break;
        case ChessBoard.WHITE:
          foregroundColor = lightSquareColor;
          backgroundColor = darkSquareColor;
          break;
        }

        /* Draw the empty square (optionally with coordinates) */
        g.setColor(foregroundColor);
        g.fillRect(x * squareSize, (ChessBoard.SIZE - y - 1) * squareSize, squareSize, squareSize);

        g.setColor(darkSquareColor);
        g.drawRect(x * squareSize, (ChessBoard.SIZE - y - 1) * squareSize, squareSize, squareSize);
        if (showCoordinates) {
          g.setColor(backgroundColor);
          g.setFont(new Font(null, 0, 15));
          g.drawString(
              useAlgebraicCoordinates ?
                  ChessBoard.convertCoordinateToAlgebraic(x, y) :  "(" + x + "," + y + ")",
              x * squareSize + 5,
              (ChessBoard.SIZE - y - 1) * squareSize + 15);
        }

        /* Draw the piece on the square */
        if (square != ChessBoard.BLANK) {
          if (useImage) {
            g.drawImage(
                (Image) pieceSetMap.get(new Integer(square)),
                x * squareSize,
                (ChessBoard.SIZE - y - 1) * squareSize,
                new ImageObserver() {
                  public boolean imageUpdate(
                      Image img, int infoflags, int x, int y, int width, int height) {
                      
                    repaint();
                    return true;
                  }
                });
          } else {
            g.setColor(backgroundColor);
            g.setFont(new Font(null, 0, 100));
            String piece = "";
            if (square > ChessBoard.BLACK && board.getSquareColor(x, y) == ChessBoard.BLACK
                || square < ChessBoard.BLACK && board.getSquareColor(x, y) == ChessBoard.WHITE) {

              piece = (String) ChessConstants.pieceTextIconMap.get(
                  new Integer(square > ChessBoard.BLACK ? square - ChessBoard.BLACK : square));
            } else {
              piece = (String) ChessConstants.pieceTextIconMap.get(
                  new Integer(square < ChessBoard.BLACK ? square + ChessBoard.BLACK : square));
            }
            g.drawString(
                piece, x * squareSize, (ChessBoard.SIZE - y - 1) * squareSize + (squareSize - 15));
          }
        }
      }
    }
  }
}
