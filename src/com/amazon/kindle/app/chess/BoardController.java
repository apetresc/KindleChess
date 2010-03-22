package com.amazon.kindle.app.chess;

public class BoardController {

	private ChessBoard board;
	
	public BoardController(ChessBoard board) {
		this.board = board;
	}
	
	public void applyMove(String move) throws IllegalMoveException {
		int[][] moveCoords = null;
		switch(move.length()) {
		case 2:
			moveCoords = pawnSimpleMove(move);
			break;
		case 3:
			if (move.equals("O-O")) {
				moveCoords = shortCastle();
			} else {
				moveCoords = pieceSimpleMove(move);
			}
			break;
		case 4:
			if (move.charAt(1) == 'x') {
				moveCoords = pieceSimpleCapture(move);
			} else {
				moveCoords = pieceAmbiguousMove(move);
			}
		}
		
		if (moveCoords != null) {
			board.move(moveCoords[0][0], moveCoords[0][1], moveCoords[1][0], moveCoords[1][1]);
			board.toggleColorToMove();
		}
	}
	
	private int[][] shortCastle() {
		switch(board.getColorToMove()) {
		case ChessBoard.WHITE:
			board.move(4, 0, 6, 0);
			board.move(7, 0, 5, 0);
			board.toggleColorToMove();
			break;
		case ChessBoard.BLACK:
			board.move(4, 7, 6, 7);
			board.move(7, 7, 5, 7);
			board.toggleColorToMove();
			break;
		}
		return null;
	}

	private int[][] pieceSimpleMove(String move) throws IllegalMoveException {
		int piece = ((Integer) ChessConstants.pieceTextMap.get(move.substring(0,1))).intValue();
		int[] moveCoords = ChessBoard.convertAlgebraicToCoordinate(move.substring(1));
		if (board.getSquare(moveCoords[0], moveCoords[1]) != ChessBoard.BLANK) {
			throw new IllegalMoveException(move, "Should be a capture move");
		}
		
		switch(piece) {
		case ChessBoard.WHITE_ROOK:
			return rookSimpleMove(move);
		case ChessBoard.WHITE_KNIGHT:
			return knightSimpleMove(move);
		case ChessBoard.WHITE_BISHOP:
			return bishopSimpleMove(move);
		case ChessBoard.WHITE_QUEEN:
			return queenSimpleMove(move);
		case ChessBoard.WHITE_KING:
			return kingSimpleMove(move);
		default:
			throw new IllegalMoveException(move, "Not a valid piece move!");
		}
	}

	private int[][] pawnSimpleMove(String move) throws IllegalMoveException {
		int colorToMove = board.getColorToMove();
		int[] moveCoords = ChessBoard.convertAlgebraicToCoordinate(move);
		int x = moveCoords[0] ; int y = moveCoords[1];
		switch (colorToMove) {
		case ChessBoard.WHITE:
			switch (y) {
			case 3:
				// Might be a double pawn move
				if (board.getSquare(x, 1) == ChessBoard.WHITE_PAWN)
					return new int[][] {new int[]{x, 1}, new int[]{x, y}};
			default:
				if (board.getSquare(x, y - 1) == ChessBoard.WHITE_PAWN)
					return new int[][] {new int[]{x, y-1}, new int[]{x, y}};
			}
			break;
		case ChessBoard.BLACK:
			switch (y) {
			case 4:
				// Might be a double pawn move
				if (board.getSquare(x, 6) == ChessBoard.BLACK_PAWN)
					return new int[][] {new int[]{x, 6}, new int[]{x, y}};
			default:
				if (board.getSquare(x, y+1) == ChessBoard.BLACK_PAWN)
					return new int[][] {new int[]{x, y+1}, new int[]{x, y}};
			}
		}
		throw new IllegalMoveException(move, "There's no pawn to move there!");
	}
	
	private int[][] kingSimpleMove(String move) throws IllegalMoveException {
		int[] moveCoords = ChessBoard.convertAlgebraicToCoordinate(move.substring(1));
		int king = ChessBoard.WHITE_KING + board.getColorToMove();
		
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				if (board.getSquare(moveCoords[0] + dx, moveCoords[1] + dy) == king) {
					return new int[][] { 
							new int[] {moveCoords[0] + dx , moveCoords[1] + dy},
							new int[] {moveCoords[0], moveCoords[1]}
					};
				}
			}
		}
		
		throw new IllegalMoveException(move, "Not a legal King move");
	}

	private int[][] queenSimpleMove(String move) throws IllegalMoveException {
		int[] moveCoords = ChessBoard.convertAlgebraicToCoordinate(move.substring(1));
		int queen = ChessBoard.WHITE_QUEEN + board.getColorToMove();
		int n;
		
		for (int dx = -1; dx <= 1; dx += 2) {
			for (int dy = -1; dy <= 1; dy += 2) {
				n = 1;
				while (board.getSquare(moveCoords[0] + n*dx, moveCoords[1] + n*dy) == ChessBoard.BLANK)
					n++;
				if (board.getSquare(moveCoords[0] + n*dx, moveCoords[1] + n*dy) == queen) {
					return new int[][] {
						new int[] {moveCoords[0] + n*dx, moveCoords[1] + n*dy},
						new int[] {moveCoords[0], moveCoords[1]}
					};
				}
			}
		}
		
		for (n = 1; board.getSquare(moveCoords[0] + n, moveCoords[1]) == ChessBoard.BLANK; n++);
		if (board.getSquare(moveCoords[0] + n, moveCoords[1]) == queen) {
			return new int[][] {
					new int[] {moveCoords[0] + n, moveCoords[1]},
					new int[] {moveCoords[0], moveCoords[1]}
			};
		}
		
		for (n = 1; board.getSquare(moveCoords[0] - n, moveCoords[1]) == ChessBoard.BLANK; n++);
		if (board.getSquare(moveCoords[0] - n, moveCoords[1]) == queen) {
			return new int[][] {
					new int[] {moveCoords[0] - n, moveCoords[1]},
					new int[] {moveCoords[0], moveCoords[1]}
			};
		}
		
		for (n = 1; board.getSquare(moveCoords[0], moveCoords[1] + n) == ChessBoard.BLANK; n++);
		if (board.getSquare(moveCoords[0], moveCoords[1] + n) == queen) {
			return new int[][] {
					new int[] {moveCoords[0], moveCoords[1] + n},
					new int[] {moveCoords[0], moveCoords[1]}
			};
		}
		
		for (n = 1; board.getSquare(moveCoords[0], moveCoords[1] - n) == ChessBoard.BLANK; n++);
		if (board.getSquare(moveCoords[0], moveCoords[1] + n) == queen) {
			return new int[][] {
					new int[] {moveCoords[0], moveCoords[1] - n},
					new int[] {moveCoords[0], moveCoords[1]}
			};
		}

		throw new IllegalMoveException(move, "Not a legal queen move");
	}

	private int[][] bishopSimpleMove(String move) throws IllegalMoveException {
		int[] moveCoords = ChessBoard.convertAlgebraicToCoordinate(move.substring(1));
		int bishop = ChessBoard.WHITE_BISHOP + board.getColorToMove();
		
		for (int dx = -1; dx <= 1; dx += 2) {
			for (int dy = -1; dy <= 1; dy += 2) {
				int n = 1;
				while (board.getSquare(moveCoords[0] + n*dx, moveCoords[1] + n*dy) == ChessBoard.BLANK)
					n++;
				if (board.getSquare(moveCoords[0] + n*dx, moveCoords[1] + n*dy) == bishop) {
					return new int[][] {
						new int[] {moveCoords[0] + n*dx, moveCoords[1] + n*dy},
						new int[] {moveCoords[0], moveCoords[1]}
					};
				}
			}
		}
		
		throw new IllegalMoveException(move, "Not a legal bishop move");
	}

	private int[][] knightSimpleMove(String move) throws IllegalMoveException {
		int[] moveCoords = ChessBoard.convertAlgebraicToCoordinate(move.substring(1));
		int knight = ChessBoard.WHITE_KNIGHT + board.getColorToMove();
		
		int[][] targetSquares = new int[][] {
			new int[] {moveCoords[0] + 1, moveCoords[1] + 2 },
			new int[] {moveCoords[0] - 1, moveCoords[1] + 2 },
			new int[] {moveCoords[0] + 1, moveCoords[1] - 2 },
			new int[] {moveCoords[0] - 1, moveCoords[1] - 2 },
			new int[] {moveCoords[0] + 2, moveCoords[1] + 1 },
			new int[] {moveCoords[0] - 2, moveCoords[1] + 1 },
			new int[] {moveCoords[0] + 2, moveCoords[1] - 1 },
			new int[] {moveCoords[0] - 2, moveCoords[1] - 1 },
		};
		
		for (int i = 0; i < targetSquares.length; i++) {
			if (board.getSquare(targetSquares[i][0], targetSquares[i][1]) == knight) {
				return new int[][] {
					new int[] {targetSquares[i][0], targetSquares[i][1]},
					new int[] {moveCoords[0],       moveCoords[1]}
				};
			}
		}

		throw new IllegalMoveException(move, "Not a legal knight move");
	}

	private int[][] rookSimpleMove(String move) throws IllegalMoveException {
		int[] moveCoords = ChessBoard.convertAlgebraicToCoordinate(move.substring(1));
		int rook = ChessBoard.WHITE_ROOK + board.getColorToMove();
		int n;
		
		for (n = 1; board.getSquare(moveCoords[0] + n, moveCoords[1]) == ChessBoard.BLANK; n++);
		if (board.getSquare(moveCoords[0] + n, moveCoords[1]) == rook) {
			return new int[][] {
					new int[] {moveCoords[0] + n, moveCoords[1]},
					new int[] {moveCoords[0], moveCoords[1]}
			};
		}
		
		for (n = 1; board.getSquare(moveCoords[0] - n, moveCoords[1]) == ChessBoard.BLANK; n++);
		if (board.getSquare(moveCoords[0] - n, moveCoords[1]) == rook) {
			return new int[][] {
					new int[] {moveCoords[0] - n, moveCoords[1]},
					new int[] {moveCoords[0], moveCoords[1]}
			};
		}
		
		for (n = 1; board.getSquare(moveCoords[0], moveCoords[1] + n) == ChessBoard.BLANK; n++);
		if (board.getSquare(moveCoords[0], moveCoords[1] + n) == rook) {
			return new int[][] {
					new int[] {moveCoords[0], moveCoords[1] + n},
					new int[] {moveCoords[0], moveCoords[1]}
			};
		}
		
		for (n = 1; board.getSquare(moveCoords[0], moveCoords[1] - n) == ChessBoard.BLANK; n++);
		if (board.getSquare(moveCoords[0], moveCoords[1] + n) == rook) {
			return new int[][] {
					new int[] {moveCoords[0], moveCoords[1] - n},
					new int[] {moveCoords[0], moveCoords[1]}
			};
		}
		
		throw new IllegalMoveException(move, "Not a legal rook move");
	}

	private int[][] pieceSimpleCapture(String move) throws IllegalMoveException {
		if (move.charAt(0) >= 'a' && move.charAt(0) <= 'h') {
			return pawnSimpleCapture(move);
		} else {
			int[] moveCoords = ChessBoard.convertAlgebraicToCoordinate(move.substring(2));
			int piece = ((Integer) ChessConstants.pieceTextMap.get(move.substring(0,1))).intValue();
			if (board.getPieceColor(moveCoords[0], moveCoords[1]) == board.getColorToMove() ||
				board.getPieceColor(moveCoords[0], moveCoords[1]) == ChessBoard.BLANK) {
				throw new IllegalMoveException(move, "Not a valid capture");
			}
			String simpleMove = "" + move.charAt(0) + move.charAt(2) + move.charAt(3);
			
			switch (piece) {
			case ChessBoard.WHITE_ROOK:
				return rookSimpleMove(simpleMove);
			case ChessBoard.WHITE_KNIGHT:
				return knightSimpleMove(simpleMove);
			case ChessBoard.WHITE_BISHOP:
				return bishopSimpleMove(simpleMove);
			case ChessBoard.WHITE_QUEEN:
				return queenSimpleMove(simpleMove);
			case ChessBoard.WHITE_KING:
				return kingSimpleMove(simpleMove);
			default:
				throw new IllegalMoveException(move, "Not a valid capture");
			}
		}
	}

	private int[][] pawnSimpleCapture(String move) throws IllegalMoveException {
		int[] moveCoords = ChessBoard.convertAlgebraicToCoordinate(move.substring(2));
		int pawn = ChessBoard.WHITE_PAWN + board.getColorToMove();
		int[] pawnLocation = ChessBoard.convertAlgebraicToCoordinate("" + move.charAt(0) + (move.charAt(3) + 
				(board.getColorToMove() == ChessBoard.WHITE ? (+1) : (-1))));
		if (board.getSquare(pawnLocation[0], pawnLocation[1]) != pawn) {
			throw new IllegalMoveException(move, "Not a valid pawn capture move -- no pawn on " + pawnLocation);
		}
		
		return new int[][] {
			new int[] {pawnLocation[0], pawnLocation[1]},
			new int[] {moveCoords[0], moveCoords[1]}
		};
	}

	private int[][] pieceAmbiguousMove(String move) throws IllegalMoveException {
		// TODO Auto-generated method stub
		return null;
	}
}
