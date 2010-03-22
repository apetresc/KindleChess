package com.amazon.kindle.app.chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.ui.KButton;
import com.amazon.kindle.kindlet.ui.KImage;
import com.amazon.kindle.kindlet.ui.KindletUIResources;
import com.amazon.kindle.kindlet.ui.KindletUIResources.KColorName;
import com.amazon.kindle.kindlet.ui.image.ImageUtil;

public class Main extends AbstractKindlet {

	/** The directory containing the piece set images. */
	private static final String  IMG_DIR = "/img/";
	/** The directory containing the preloaded pgns */
	private static final String  PGN_DIR = "/pgn/" ;
	/** The image format of the piece set images. */
	private static final String  IMG_EXT  = ".png";
	/** The size of each individual square on the displayed board. */
	private static final int     SQUARE_SIZE = 100;
	/** Sets whether a graphical or textual representation of the chess pieces is used. */
	private static final boolean USE_IMG = false;
	/** Sets whether to display the coordinates of each square in the top-right corner */
	private static boolean       SHOW_COORDINATES = true;
	/** Sets whether the coordinates are displayed as algebraic or numeric coordinates */
	private static boolean		 ALGEBRAIC_COORDINATES = false;
	
	/** A mapping from piece Strings to Images. */
	private static Map pieceSetMap;
	
	private KindletContext context;
	/** The currently-displayed ChessBoard */
	private ChessBoard board;
	private Container root;
	/** An image of the current position on the ChessBoard */
	private BufferedImage boardImage;
	/** The KImage component containing <code>boardImage</code> */
	private KImage boardComponent;
		
	private final Logger log = Logger.getLogger(Main.class);
	
	/**
	 * Initializes the ChessBoard and the UI, and draws them to the screen.
	 * @param context Provides access to the environment the Kindlet is running in.
	 */
	public void create(KindletContext context) {
		this.context = context;
		board = new ChessBoard();
		board.init();

		root = context.getRootContainer();
		boardImage = ImageUtil.createCompatibleImage(ChessBoard.SIZE * SQUARE_SIZE + 1, ChessBoard.SIZE * SQUARE_SIZE + 1, Transparency.TRANSLUCENT);
		Graphics2D g = boardImage.createGraphics();
		g.setColor(context.getUIResources().getBackgroundColor(KindletUIResources.KColorName.WHITE));
		g.fillRect(0, 0, ChessBoard.SIZE * SQUARE_SIZE, ChessBoard.SIZE * SQUARE_SIZE);
		
		InputStream pgn = getClass().getResourceAsStream(PGN_DIR + "test.pgn");
		ChessRecord testRecord = new ChessRecord();
		try {
			testRecord.parsePGN(pgn);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PGNParseException pgnpe) {
			log.info(pgnpe.getMessage());
		}
		log.info("Parse successful!");
		log.info(testRecord);
		
		final BoardController bc = new BoardController(board);
		final List moveList = testRecord.getMoves();
		final Iterator it = moveList.iterator();
		
		KButton button = new KButton("Next Move");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (it.hasNext()) {
					try {
						bc.applyMove((String) it.next());
						drawBoard();
						boardComponent.setImage(boardImage);
						boardComponent.repaint();
					} catch (IllegalMoveException ime) {
						log.info(ime.getMessage());
					}
				}
			}
			
		});
		
		root.setLayout(new BorderLayout());
		boardComponent = new KImage(boardImage);
		root.add(boardComponent, BorderLayout.NORTH);
		root.add(button, BorderLayout.SOUTH);

		initResources();
		drawBoard();
	}
	
	/**
	 * Loads the required resources from the application JAR, such as the piece set images.
	 */
	private void initResources() {
		pieceSetMap = new HashMap();

		pieceSetMap.put(new Integer(ChessBoard.WHITE_PAWN),   Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.WHITE_PAWN + IMG_EXT)));
		pieceSetMap.put(new Integer(ChessBoard.WHITE_ROOK),   Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.WHITE_ROOK + IMG_EXT)));
		pieceSetMap.put(new Integer(ChessBoard.WHITE_KNIGHT), Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.WHITE_KNIGHT + IMG_EXT)));
		pieceSetMap.put(new Integer(ChessBoard.WHITE_BISHOP), Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.WHITE_BISHOP + IMG_EXT)));
		pieceSetMap.put(new Integer(ChessBoard.WHITE_QUEEN),  Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.WHITE_QUEEN + IMG_EXT)));
		pieceSetMap.put(new Integer(ChessBoard.WHITE_KING),   Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.WHITE_KING + IMG_EXT)));

		pieceSetMap.put(new Integer(ChessBoard.BLACK_PAWN),   Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.BLACK_PAWN + IMG_EXT)));
		pieceSetMap.put(new Integer(ChessBoard.BLACK_ROOK),   Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.BLACK_ROOK + IMG_EXT)));
		pieceSetMap.put(new Integer(ChessBoard.BLACK_KNIGHT), Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.BLACK_KNIGHT + IMG_EXT)));
		pieceSetMap.put(new Integer(ChessBoard.BLACK_BISHOP), Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.BLACK_BISHOP + IMG_EXT)));
		pieceSetMap.put(new Integer(ChessBoard.BLACK_QUEEN),  Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.BLACK_QUEEN + IMG_EXT)));
		pieceSetMap.put(new Integer(ChessBoard.BLACK_KING),   Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.BLACK_KING + IMG_EXT)));

		pieceSetMap.put(new Integer(ChessBoard.WHITE),        Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.WHITE + IMG_EXT)));
		pieceSetMap.put(new Integer(ChessBoard.BLACK),        Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + ChessBoard.BLACK + IMG_EXT)));
	}
	
	/** Draws the current state of the ChessBoard to the <code>boardImage</code> instance. */
	private void drawBoard() {
		log.info("Drawing board!\n\n" + board);
		Graphics2D g = boardImage.createGraphics();
		for (int y = 0; y < ChessBoard.SIZE; y++) {
			for (int x = 0; x < ChessBoard.SIZE; x++) {
				int square = board.getSquare(x, y);
				Color foregroundColor = null, backgroundColor = null;
				switch (board.getSquareColor(x, y)) {
				case ChessBoard.BLACK:
					foregroundColor = context.getUIResources().getBackgroundColor(KColorName.BLACK);
					backgroundColor = context.getUIResources().getBackgroundColor(KColorName.WHITE);
					break;
				case ChessBoard.WHITE:
					foregroundColor = context.getUIResources().getBackgroundColor(KColorName.WHITE);
					backgroundColor = context.getUIResources().getBackgroundColor(KColorName.BLACK);
					break;
				}

				/* Draw the empty square (optionally with coordinates) */
				g.setColor(foregroundColor);
				g.fillRect(x * SQUARE_SIZE, (ChessBoard.SIZE - y - 1) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				g.setColor(context.getUIResources().getBackgroundColor(KColorName.BLACK));
				g.drawRect(x * SQUARE_SIZE, (ChessBoard.SIZE - y - 1) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				if (SHOW_COORDINATES ) {
					g.setColor(backgroundColor);
					g.setFont(new Font(null, 0, 15));
					if (ALGEBRAIC_COORDINATES)
						g.drawString(ChessBoard.convertCoordinateToAlgebraic(x, y), x * SQUARE_SIZE + 5, (ChessBoard.SIZE - y - 1) * SQUARE_SIZE + 15);
					else
						g.drawString("(" + x + "," + y + ")", x * SQUARE_SIZE + 5, (ChessBoard.SIZE - y - 1) * SQUARE_SIZE + 15);
				}
				
				/* Draw the piece on the square */
				if (square != ChessBoard.BLANK) {
					if (USE_IMG) {
						g.drawImage((Image) pieceSetMap.get(new Integer(square)), x * SQUARE_SIZE, y * SQUARE_SIZE, new ImageObserver() {				
							public boolean imageUpdate(Image img, int infoflags, int x, int y,
									int width, int height) {
								return false;
							}
						});
					} else {
						g.setColor(backgroundColor);
						g.setFont(new Font(null, 0, 100));
						String piece = "";
						if (square > ChessBoard.BLACK && board.getSquareColor(x,y) == ChessBoard.BLACK ||
							square < ChessBoard.BLACK && board.getSquareColor(x,y) == ChessBoard.WHITE) {
							piece = (String) ChessConstants.pieceTextIconMap.get(new Integer(square > ChessBoard.BLACK ? square - ChessBoard.BLACK : square));
						} else {
							piece = (String) ChessConstants.pieceTextIconMap.get(new Integer(square < ChessBoard.BLACK ? square + ChessBoard.BLACK : square));
						}
						g.drawString(piece, x * SQUARE_SIZE, (ChessBoard.SIZE - y - 1) * SQUARE_SIZE + (SQUARE_SIZE - 15));
					}
				}
			}
		}
	}

}
