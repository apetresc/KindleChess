package com.amazon.kindle.app.chess.model.pgn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;


public class ChessRecord {

	private static List resultList;
	
	private Map tagPairs;
	private List moveList;
	private String result;
	
	private final Logger log = Logger.getLogger(ChessRecord.class);
	
	public ChessRecord() {
		tagPairs = new HashMap();
		moveList = new LinkedList();
	
		resultList = new LinkedList();
		resultList.add("1-0");
		resultList.add("0-1");
		resultList.add("1/2-1/2");
		resultList.add("*");
	}
	
	public void parsePGN(InputStream inStream) throws IOException, PGNParseException {
		BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
		String inLine;
		while ((inLine = in.readLine()) != null && inLine.trim().startsWith("[") && inLine.trim().endsWith("]")) {
			inLine = inLine.trim().substring(1, inLine.trim().length() - 1);
			try {
				tagPairs.put(inLine.substring(0, inLine.indexOf(" ")), inLine.substring(inLine.indexOf("\"") + 1, inLine.length() -1));
			} catch (IndexOutOfBoundsException ioobe) {
			  log.error("Could not parse tag: " + inLine);
				throw new PGNParseException("Could not parse tag: " + inLine);
			}
		}
		
		String moves = "";
		while ((inLine = in.readLine()) != null && !resultList.contains(inLine.trim())) {
			moves += inLine + " ";
		}
		
		StringTokenizer st = new StringTokenizer(moves);
		int moveNum = 1;
		while(st.hasMoreTokens()) {
			if (!st.nextToken().startsWith(moveNum +".")) {
			  log.error("Could not parse past move " + moveNum);
				throw new PGNParseException("Could not parse past move " + moveNum);
			}
			
			try {
				String whiteMove = st.nextToken();
				if (resultList.contains(whiteMove)) {
					result = whiteMove;
					break;
				}
				if (validateMove(whiteMove)) {
					moveList.add(whiteMove);
				} else {
				  log.error("Could not parse White " + moveNum + ": " + whiteMove + ".");
					throw new PGNParseException("Could not parse White " + moveNum + ": " + whiteMove + ".");
				}
				
				String blackMove = st.nextToken();
				if (resultList.contains(blackMove)) {
					result = blackMove;
					break;
				}
				if (validateMove(blackMove)) {
					moveList.add(blackMove);
				} else {
				  log.error("Could not parse Black " + moveNum + ": " + blackMove + ".");
					throw new PGNParseException("Could not parse Black " + moveNum + ": " + blackMove + ".");
				}
			} catch (NoSuchElementException nsee) {
			  log.error("Could not parse: Game ended unexpectedly on move " + moveNum);
				throw new PGNParseException("Could not parse: Game ended unexpectedly on move " + moveNum);
			}
			moveNum++;
		}
	}
	
	public String getTag(String key) {
		return (String) tagPairs.get(key);
	}
	
	public List getMoves() {
		return moveList;
	}
	
	private boolean validateMove(String move) {
		if (move.endsWith("!"))       move = move.substring(0, move.length() - 1);
		else if (move.endsWith("?"))  move = move.substring(0, move.length() - 1);
		else if (move.endsWith("?!")) move = move.substring(0, move.length() - 2);
		else if (move.endsWith("!?")) move = move.substring(0, move.length() - 2);
		if (move.endsWith("#"))       move = move.substring(0, move.length() - 1);
		else if (move.endsWith("+"))  move = move.substring(0, move.length() - 1);
		
		if (move.equals("O-O") || move.equals("O-O-O")) return true;
		
		switch (move.length()) {
		case 1: return false;
		case 2: {
			char l = move.charAt(0);
			char n = move.charAt(1);
			if (l >= 'a' && l <= 'h' && n >= '1' && n <= '8')
				return true;
		}
		break;
		case 3: {
			char p = move.charAt(0);
			char l = move.charAt(1);
			char n = move.charAt(2);
			if ("RNBQK".indexOf(p) >= 0 && l >= 'a' && l <= 'h' && n >= '1' && n <= '8')
				return true;
		}
		break;
		case 4: {
			if (move.charAt(1) == 'x') {
				// Capture
				char p = move.charAt(0);
				char l = move.charAt(2);
				char n = move.charAt(3);
				if (("RNBQK".indexOf(p) >= 0 || (p >= 'a' && p <= 'h')) &&
				    (l >= 'a' && l <= 'h' && n >= '1' && n <= '8')) {
					return true;
				}
			} else {
				char p  = move.charAt(0);
				char l1 = move.charAt(1);
				char l2 = move.charAt(2);
				char n  = move.charAt(3);
				if ("RNBQK".indexOf(p) >= 0 && l1 >= 'a' && l1 <= 'h' && l2 >= 'a' &&
				    l2 <= 'h' && n >= '1' && n <= '8') {
					return true;
				}
			}
		}
		break;
		default: break;
		}
		return false;
	}
	
	public String toString() {
		String res = "";
		Iterator it = tagPairs.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			res += "[" + key + " \"" + tagPairs.get(key) + "\"]\n";
		}
		res += "\n";
		it = moveList.iterator();
		int moveNum = 1;
		while (it.hasNext()) {
			res += moveNum + ". ";
			res += it.next() + " ";
			if (it.hasNext()) res += it.next() + "\n";
			moveNum++;
		}
		res += result + "\n";
		
		return res;
	}
}
