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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.ui.KButton;
import com.amazon.kindle.kindlet.ui.KImage;
import com.amazon.kindle.kindlet.ui.KindletUIResources;
import com.amazon.kindle.kindlet.ui.KindletUIResources.KColorName;
import com.amazon.kindle.kindlet.ui.image.ImageUtil;

public class Main extends AbstractKindlet {

	private static final String  IMG_DIR  = "/img/";
	private static final String  IMG_EXT  = ".png";
	private static final int     SQUARE_SIZE = 100;
	private static final boolean USE_IMG = false;
	private static boolean       SHOW_COORDINATES = true;
	private static Map pieceSetMap;
	private static Map pieceTextMap;
	
	private KindletContext context;
	private ChessBoard board;
	private Container root;
	private BufferedImage boardImage;
	private KImage boardComponent;
		
	private final Logger log = Logger.getLogger(Main.class);
	
	public void create(KindletContext context) {
		this.context = context;
		board = new ChessBoard();
		board.init();

		root = context.getRootContainer();
		boardImage = ImageUtil.createCompatibleImage(ChessBoard.SIZE * SQUARE_SIZE + 1, ChessBoard.SIZE * SQUARE_SIZE + 1, Transparency.TRANSLUCENT);
		Graphics2D g = boardImage.createGraphics();
		g.setColor(context.getUIResources().getBackgroundColor(KindletUIResources.KColorName.WHITE));
		g.fillRect(0, 0, ChessBoard.SIZE * SQUARE_SIZE, ChessBoard.SIZE * SQUARE_SIZE);
		
		KButton button = new KButton("Refresh");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				board.move("e2", "e4");
				drawBoard();
				boardComponent.setImage(boardImage);
				boardComponent.repaint();
				log.info("Repainting!!");
			}
			
		});
		
		root.setLayout(new BorderLayout());
		boardComponent = new KImage(boardImage);
		root.add(boardComponent, BorderLayout.NORTH);
		root.add(button, BorderLayout.SOUTH);

		initResources();
		drawBoard();
	}
	
	private void initResources() {
		pieceSetMap = new HashMap();
		pieceTextMap = new HashMap();

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
		
		pieceTextMap.put(new Integer(ChessBoard.WHITE_PAWN),   "P");
		pieceTextMap.put(new Integer(ChessBoard.WHITE_ROOK),   "R");
		pieceTextMap.put(new Integer(ChessBoard.WHITE_KNIGHT), "N");
		pieceTextMap.put(new Integer(ChessBoard.WHITE_BISHOP), "B");
		pieceTextMap.put(new Integer(ChessBoard.WHITE_QUEEN),  "Q");
		pieceTextMap.put(new Integer(ChessBoard.WHITE_KING),   "K");

		pieceTextMap.put(new Integer(ChessBoard.BLACK_PAWN),   "p");
		pieceTextMap.put(new Integer(ChessBoard.BLACK_ROOK),   "r");
		pieceTextMap.put(new Integer(ChessBoard.BLACK_KNIGHT), "n");
		pieceTextMap.put(new Integer(ChessBoard.BLACK_BISHOP), "b");
		pieceTextMap.put(new Integer(ChessBoard.BLACK_QUEEN),  "q");
		pieceTextMap.put(new Integer(ChessBoard.BLACK_KING),   "k");
	}
	
	private void drawBoard() {
		log.info("Drawing board!\n\n" + board);
		Graphics2D g = boardImage.createGraphics();
		for (int y = 0; y < ChessBoard.SIZE; y++) {
			for (int x = 0; x < ChessBoard.SIZE; x++) {
				int square = board.getSquare(x, y);
				Color foregroundColor = null, backgroundColor = null;
				switch (board.getColor(x, y)) {
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
					g.drawString(x + "," + y, x * SQUARE_SIZE + 5, (ChessBoard.SIZE - y - 1) * SQUARE_SIZE + 15);
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
						g.setFont(new Font(null, 0, 30));
						g.drawString((String) pieceTextMap.get(new Integer(square)), x * SQUARE_SIZE + ((SQUARE_SIZE / 2) - 10 ), (ChessBoard.SIZE - y - 1) * SQUARE_SIZE + (SQUARE_SIZE - 10));
					}
				}
			}
		}
	}

}
