/*
 * This file is part of PGNParse.
 *
 * PGNParse is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PGNParse is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PGNParse.  If not, see <http://www.gnu.org/licenses/>. 
 */
package com.codethesis.pgnparse;

/**
 * 
 * @author Deyan Rizov
 * 
 */
public class PGNMove {

	private String move;

	private String fullMove;
	
	private String fromSquare;
	
	private String toSquare;
	
	private String piece;
	
	private Color color;
	
	private String comment;

	private boolean checked;
	
	private boolean checkMated;

	private boolean captured;

	private boolean promoted;

	private String promotion;

	private boolean endGameMarked;

	private String endGameMark;
	
	private boolean kingSideCastle;
	
	private boolean queenSideCastle;
	
	private boolean enpassant;
	
	private boolean enpassantCapture;
	
	private String enpassantPieceSquare;
	
	/**
	 * @param fullMove
	 */
	PGNMove(String fullMove) throws MalformedMoveException {
		this(fullMove, "");
	}
	
	/**
	 * @param fullMove
	 * @param comment
	 * @throws MalformedMoveException 
	 */
	PGNMove(String fullMove, String comment) throws MalformedMoveException {
		super();
		this.fullMove = fullMove;
		this.comment = comment;
		parse();
	}
	
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the move
	 */
	public String getMove() {
		return move;
	}

	/**
	 * @return the fullMove
	 */
	public String getFullMove() {
		return fullMove;
	}

	/**
	 * @return the fromSquare
	 */
	public String getFromSquare() {
		return fromSquare;
	}

	/**
	 * @param fromSquare the fromSquare to set
	 */
	void setFromSquare(String fromSquare) {
		this.fromSquare = fromSquare;
	}

	/**
	 * @return the toSquare
	 */
	public String getToSquare() {
		return toSquare;
	}

	/**
	 * @param toSquare the toSquare to set
	 */
	void setToSquare(String toSquare) {
		this.toSquare = toSquare;
	}

	/**
	 * @return the piece
	 */
	public String getPiece() {
		return piece;
	}

	/**
	 * @param piece the piece to set
	 */
	void setPiece(String piece) {
		this.piece = piece;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @return the captured
	 */
	public boolean isCaptured() {
		return captured;
	}

	/**
	 * @return the promoted
	 */
	public boolean isPromoted() {
		return promoted;
	}

	/**
	 * @return the promotion
	 */
	public String getPromotion() {
		return promotion;
	}

	/**
	 * @return the endGameMarked
	 */
	public boolean isEndGameMarked() {
		return endGameMarked;
	}

	/**
	 * @return the endGameMark
	 */
	public String getEndGameMark() {
		return endGameMark;
	}

	/**
	 * @return the checkMated
	 */
	public boolean isCheckMated() {
		return checkMated;
	}

	/**
	 * @return the kingSideCastle
	 */
	public boolean isKingSideCastle() {
		return kingSideCastle;
	}

	/**
	 * @param kingSideCastle the kingSideCastle to set
	 */
	void setKingSideCastle(boolean kingSideCastle) {
		this.kingSideCastle = kingSideCastle;
	}

	/**
	 * @return the queenSideCastle
	 */
	public boolean isQueenSideCastle() {
		return queenSideCastle;
	}

	/**
	 * @param queenSideCastle the queenSideCastle to set
	 */
	void setQueenSideCastle(boolean queenSideCastle) {
		this.queenSideCastle = queenSideCastle;
	}
	
	/**
	 * @return
	 */
	public boolean isCastle() {
		return kingSideCastle || queenSideCastle;
	}

	/**
	 * @return the enpassant
	 */
	public boolean isEnpassant() {
		return enpassant;
	}

	/**
	 * @param enpassant the enpassant to set
	 */
	void setEnpassant(boolean enpassant) {
		this.enpassant = enpassant;
	}

	/**
	 * @return the enpassantCapture
	 */
	public boolean isEnpassantCapture() {
		return enpassantCapture;
	}

	/**
	 * @param enpassantCapture the enpassantCapture to set
	 */
	void setEnpassantCapture(boolean enpassantCapture) {
		this.enpassantCapture = enpassantCapture;
	}

	/**
	 * @return the enpassantPieceSquare
	 */
	public String getEnpassantPieceSquare() {
		return enpassantPieceSquare;
	}

	/**
	 * @param enpassantPieceSquare the enpassantPieceSquare to set
	 */
	void setEnpassantPieceSquare(String enpassantPieceSquare) {
		this.enpassantPieceSquare = enpassantPieceSquare;
	}

	/**
	 * 
	 * @throws MalformedMoveException
	 */
	private void parse() throws MalformedMoveException {
		if (fullMove == null) {
			throw new NullPointerException();
		}

		String move = fullMove;
		
		if (move.startsWith(PGNParser.PAWN)) {
			this.piece = PGNParser.PAWN;
		} else if (move.startsWith(PGNParser.KNIGHT)) {
			this.piece = PGNParser.KNIGHT;
		} else if (move.startsWith(PGNParser.BISHOP)) {
			this.piece = PGNParser.BISHOP;
		} else if (move.startsWith(PGNParser.ROOK)) {
			this.piece = PGNParser.ROOK;
		} else if (move.startsWith(PGNParser.QUEEN)) {
			this.piece = PGNParser.QUEEN;
		} else if (move.startsWith(PGNParser.KING)) {
			this.piece = PGNParser.KING;
		} else {
			this.piece = PGNParser.PAWN;
		}

		if (move.contains("x")) {
			this.captured = true;
			move = move.replace("x", "");
		}

		if (move.contains("+")) {
			this.checked = true;
			move = move.replace("+", "");
		}
		
		if (move.contains("#")) {
			this.checkMated = true;
			move = move.replace("#", "");
		}

		if (move.contains("=")) {
			try {
				String promotedPiece = move.substring(move.indexOf('=') + 1);
				
				if (promotedPiece.equals(PGNParser.PAWN)
						|| promotedPiece.equals(PGNParser.KNIGHT)
						|| promotedPiece.equals(PGNParser.BISHOP)
						|| promotedPiece.equals(PGNParser.ROOK)
						|| promotedPiece.equals(PGNParser.QUEEN)
						|| promotedPiece.equals(PGNParser.KING))
				{
					move = move.substring(0, move.indexOf('='));
					this.promoted = true;
					this.promotion = promotedPiece;
				}
				else
				{
					throw new MalformedMoveException("Wrong piece abr [" + promotedPiece + "]");
				}
			} catch (IndexOutOfBoundsException e) {
				throw new MalformedMoveException(e);
			}
		}
		
		if (move.equals("0-0") || move.equals("O-O")) {
			kingSideCastle = true;
		} else if (move.equals("0-0-0") || move.equals("O-O-O")) {
			queenSideCastle = true;
		} else if (move.equals("1-0") || move.equals("0-1") || move.equals("1/2-1/2") || move.equals("*")) {
			this.endGameMarked = true;
			this.endGameMark = move;
		}
		
		this.move = move;
	}

}
