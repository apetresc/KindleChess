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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deyan Rizov
 *
 */
public class PGNGame {

	private Map tags;
	
	private List moves;
	
	private String pgn;
	
	PGNGame() {
		tags = new HashMap();
		moves = new LinkedList();
	}
	
	PGNGame(String pgn) {
		this();
		this.pgn = pgn;
	}
	
	public String toString() {
		return pgn == null ? "" : pgn;
	}
	
	void addTag(String key, String value) {
		tags.put(key, value);
	}
	
	void removeTag(String key) {
		tags.remove(key);
	}
	
	void addMove(PGNMove move) {
		moves.add(move);
	}
	
	void removeMove(PGNMove move) {
		moves.remove(move);
	}
	
	void removeMove(int index) {
		moves.remove(index);
	}
	
	public String getTag(String key) {
		return (String) tags.get(key);
	}
	
	public Iterator getTagKeysIterator() {
		return tags.keySet().iterator();
	}
	
	public boolean containsTagKey(String key) {
		return tags.containsKey(key);
	}
	
	public int getTagsCount() {
		return tags.size();
	}
	
	public PGNMove getMove(int index) {
		return (PGNMove) moves.get(index);
	}
	
	public Iterator getMovesIterator() {
		return moves.iterator();
	}
	
	public int getMovesCount() {
		return moves.size();
	}
	
	public int getMovePairsCount() {
		return moves.size() / 2;
	}
	
}
