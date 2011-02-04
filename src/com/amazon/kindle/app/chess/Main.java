package com.amazon.kindle.app.chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.MediaTracker;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.apache.log4j.FileAppender;
import org.apache.log4j.PatternLayout;

import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.ui.KButton;
import com.amazon.kindle.kindlet.ui.KImage;
import com.amazon.kindle.kindlet.ui.KindletUIResources;
import com.amazon.kindle.kindlet.ui.KindletUIResources.KColorName;
import com.amazon.kindle.kindlet.ui.image.ImageUtil;
import com.amazon.kindle.kindlet.ui.KTextArea;
import com.amazon.kindle.kindlet.ui.KMenu;
import com.amazon.kindle.kindlet.ui.KMenuItem;


public class Main extends AbstractKindlet {

	/** The directory containing the piece set images. */
	private static final String  IMG_DIR = "/img/";
	/** The directory containing the preloaded pgns */
	private static final String  PGN_DIR = "/pgn/" ;
	/** The image format of the piece set images. */
	private static final String  IMG_EXT  = ".png";
	/** The size of each individual square on the displayed board. */
	private static int     square_size;
	/** Sets whether a graphical or textual representation of the chess pieces is used. */
	private static final boolean USE_IMG = true;
	/** Sets whether to display the coordinates of each square in the top-right corner */
	private static boolean       SHOW_COORDINATES = false;
	/** Sets whether the coordinates are displayed as algebraic or numeric coordinates */
	private static boolean		 ALGEBRAIC_COORDINATES = false;
	
	/** A mapping from piece Strings to Images. */
	private static Map pieceSetMap;
	/** List of pieces. */
	private static int[] pieces = {
		  ChessBoard.WHITE_PAWN, ChessBoard.WHITE_ROOK, ChessBoard.WHITE_KNIGHT, ChessBoard.WHITE_BISHOP,
			ChessBoard.WHITE_QUEEN, ChessBoard.WHITE_KING, ChessBoard.BLACK_PAWN, ChessBoard.BLACK_ROOK,
			ChessBoard.BLACK_KNIGHT, ChessBoard.BLACK_BISHOP, ChessBoard.BLACK_QUEEN, ChessBoard.BLACK_KING};
	
	private KindletContext context;
	/** The currently-displayed ChessBoard */
	private ChessBoard board;
	private Container root;
	private KTextArea ktext = new KTextArea(3, 50);
	/** An image of the current position on the ChessBoard */
	private BufferedImage boardImage;
	/** The KImage component containing <code>boardImage</code> */
	private KImage boardComponent;
		
	static Logger log = Logger.getLogger(Main.class);

	/**
	 * Sets the board size as a percentage of min(screen width, screen height)
	 * @param percent Percentage of screen size
	 * @param redraw Redraw the board
	 */
	private void setSize(final int percent, boolean redraw) {
				final Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
				square_size = (screensize.width < screensize.height ? screensize.width : screensize.height)*percent/100/8;
				boardImage = ImageUtil.createCompatibleImage(ChessBoard.SIZE * square_size + 1, ChessBoard.SIZE * square_size + 1, Transparency.TRANSLUCENT);
				Graphics2D g = boardImage.createGraphics();
				g.setColor(context.getUIResources().getBackgroundColor(KindletUIResources.KColorName.WHITE));
				g.fillRect(0, 0, ChessBoard.SIZE * square_size, ChessBoard.SIZE * square_size);
				if (redraw) {
					initResources();
					boardComponent.setImage(boardImage);
					drawBoard();
					boardComponent.repaint();
				}
	}
	/**
	 * Initializes the ChessBoard and the UI, and draws them to the screen.
	 * @param context Provides access to the environment the Kindlet is running in.
	 */
	public void create(final KindletContext context) {
		final Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		this.context = context;
		board = new ChessBoard();
		board.init();

		// can't seem to get any file appender to work via log4j.properties. get a security violation no matter what
		PropertyConfigurator.configure(getClass().getResource("log4j.properties"));
		// so using this instead
		try {
			log.addAppender(new FileAppender(new PatternLayout("%m%n"),new File(context.getHomeDirectory(), "log.txt").getAbsolutePath()));
		} catch (Throwable t) {
		}

		root = context.getRootContainer();
		// ktext intended for pgn comments. Currently just shows the move
		ktext.setEditable(false);
		ktext.setFocusable(false);
		// menu to allow board resizing for testing
		final KMenu menu = new KMenu();
		final KMenuItem pct100 = new KMenuItem("100%");
		pct100.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setSize(100, true);
			}
		});
		final KMenuItem pct50 = new KMenuItem("50%");
		pct50.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setSize(50, true);
			}
		});
		final KMenuItem pct25 = new KMenuItem("25%");
		pct25.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setSize(25, true);
			}
		});
		menu.add(pct100);
		menu.add(pct50);
		menu.add(pct25);
		context.setMenu(menu);

		setSize(30, false);

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
						String move = (String)it.next();
						log.info("move: "+move+"\n");
						ktext.setText(move);
						ktext.repaint();
						bc.applyMove(move);
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
		root.add(ktext);

		initResources();
		drawBoard();
	}
	
	/**
	 * Loads the required resources from the application JAR, such as the piece set images.
	 */
	private void initResources() {
		pieceSetMap = new HashMap();
		MediaTracker mediatracker = new MediaTracker(boardComponent);
		for (int i = 0; i < pieces.length; i++) {
			Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + pieces[i]+ IMG_EXT)).getScaledInstance(square_size, square_size, Image.SCALE_FAST);
			pieceSetMap.put(new Integer(pieces[i]), image);
			mediatracker.addImage(image, 0);
		}
		try {
			mediatracker.waitForAll();
		} catch (final InterruptedException e) {
			log.error("Error occured in Image loading thread.", e);
		}

	}
	
	/** Draws the current state of the ChessBoard to the <code>boardImage</code> instance. */
	private void drawBoard() {
		KColorName colWhite = KColorName.WHITE;
		KColorName colBlack = KColorName.GRAY_09;
		log.info("Drawing board!\n\n" + board);
		Graphics2D g = boardImage.createGraphics();
		for (int y = 0; y < ChessBoard.SIZE; y++) {
			for (int x = 0; x < ChessBoard.SIZE; x++) {
				int square = board.getSquare(x, y);
				log.info("square ("+x+","+y+") -> "+square+" (col "+board.getSquareColor(x, y)+")\n");
				Color foregroundColor = null, backgroundColor = null;
				switch (board.getSquareColor(x, y)) {
				case ChessBoard.BLACK:
					foregroundColor = context.getUIResources().getBackgroundColor(colBlack);
					backgroundColor = context.getUIResources().getBackgroundColor(colWhite);
					break;
				case ChessBoard.WHITE:
					foregroundColor = context.getUIResources().getBackgroundColor(colWhite);
					backgroundColor = context.getUIResources().getBackgroundColor(colBlack);
					break;
				}

				/* Draw the empty square (optionally with coordinates) */
				g.setColor(foregroundColor);
				g.fillRect(x * square_size, (ChessBoard.SIZE - y - 1) * square_size, square_size, square_size);

				g.setColor(context.getUIResources().getBackgroundColor(colBlack));
				g.drawRect(x * square_size, (ChessBoard.SIZE - y - 1) * square_size, square_size, square_size);
				if (SHOW_COORDINATES ) {
					g.setColor(backgroundColor);
					g.setFont(new Font(null, 0, 15));
					if (ALGEBRAIC_COORDINATES)
						g.drawString(ChessBoard.convertCoordinateToAlgebraic(x, y), x * square_size + 5, (ChessBoard.SIZE - y - 1) * square_size + 15);
					else
						g.drawString("(" + x + "," + y + ")", x * square_size + 5, (ChessBoard.SIZE - y - 1) * square_size + 15);
				}
				
				/* Draw the piece on the square */
				if (square != ChessBoard.BLANK) {
					if (USE_IMG) {
					log.info("image "+square+","+board.getSquareColor(x,y)+","+new Integer(square+board.getSquareColor(x,y)*10+100)+"\n");
					  
						g.drawImage((Image) pieceSetMap.get(new Integer(square)), x * square_size, (ChessBoard.SIZE - y - 1) * square_size, new ImageObserver() {				
							public boolean imageUpdate(Image img, int infoflags, int x, int y,
									int width, int height) {
									log.info("got an imageUpdate\n");
								boardComponent.repaint();
								return true;
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
						g.drawString(piece, x * square_size, (ChessBoard.SIZE - y - 1) * square_size + (square_size - 15));
					}
				}
			}
		}
	}

}
