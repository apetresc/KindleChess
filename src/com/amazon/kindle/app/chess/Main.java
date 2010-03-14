package com.amazon.kindle.app.chess;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
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
import com.amazon.kindle.kindlet.ui.KImage; 
import com.amazon.kindle.kindlet.ui.KPanel;

public class Main extends AbstractKindlet {

	private static final String IMG_DIR  = "/img/";
	private static final String IMG_EXT  = ".png";
	private static final int    IMG_SIZE = 64; 
	private static Map pieceSetMap;
	
	private ChessBoard board;
	private Container root;
	private KPanel boardPanel;
	
	private final Logger log = Logger.getLogger(Main.class);
	
	public void create(KindletContext context) {
		board = new ChessBoard();
		//board.init();

		root = context.getRootContainer();
		boardPanel = new KPanel(new GridLayout(ChessBoard.SIZE, ChessBoard.SIZE, 0, 0));
		boardPanel.setSize(ChessBoard.SIZE * IMG_SIZE, ChessBoard.SIZE * IMG_SIZE);
		
		root.setLayout(new BorderLayout());
		root.add(boardPanel, BorderLayout.CENTER);

		initResources();
		drawBoard();

		// Set up logging
		try { 
			final PatternLayout layout = new PatternLayout("%p - %m%n"); 
			final FileAppender appender = new FileAppender(layout, context.getHomeDirectory() + File.separator + "debug.log"); 
			BasicConfigurator.configure(appender);
			Logger.getRootLogger().setLevel(Level.ALL);
		} catch (IOException e) {
			// Handle configuration error
		}
	}
	
	public void start() {
		
	}
	
	
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
	
	private void drawBoard() {
		log.info("Drawing board!\n\n" + board);
		for (int x = 0; x < ChessBoard.SIZE; x++) {
			for (int y = 0; y < ChessBoard.SIZE; y++) {
				int square = board.getSquare(x, y);
				if (square == ChessBoard.BLANK) {
					boardPanel.add(new KImage((Image) pieceSetMap.get(new Integer(board.getColor(x, y)))));
					//boardPanel.add(new KImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource(IMG_DIR + board.getColor(x, y) + IMG_EXT))));
					log.info("Drawing " + board.getColor(x, y) + " square");
				} else {
					root.add(new KImage ((Image) pieceSetMap.get(new Integer(square))));
				}
			}
		}
	}

}
