package application;

import javafx.geometry.Point2D;

public class LineProcesser {
	
	private String line;
	private int index;
	
	public LineProcesser(String line) {
		this.line = line;
		this.index = 0;
	}
	public LineProcesser(String line, int index) {
		this.line = line;
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		if (index < 0) index = 0;
		if (index > length()) index = length();
		this.index = index;
	}
	public void toNextIndex() {
		if (index < length()) index++;
		else index = length();
	}
	
	public boolean isEnd() {
		return index == length();
	}
	
	public int length() {
		return line.length();
	}
	
	public char current() {
		return line.charAt(index);
	}
	
	
	public int findNextInt() {
		int next = 0;
		skipUntilNumber();
		while (isNumber(current())) {
			next *= 10;
			next += current() - '0';
		}
		return next;
	}
	
	public String findNextWord() {
		skipUntilAlphabet();
		int beginIndex = getIndex();
		while (isAlphabet(current())) {
			toNextIndex();
		}
		int endIndex = getIndex();
		return line.substring(beginIndex, endIndex - beginIndex);
	}
	
	public Point2D findNextPoint2D() {
		skipUntil('(');
		int x = findNextInt();
		skipUntil(',');
		int y = findNextInt();
		skipUntil(')');
		return new Point2D(x, y);
	}
	
	
	public void skipUntil(char c) {
		while (current() != c) {
			toNextIndex();
			if (isEnd()) break;
		}
	}
	
	public void skipUntilNumber() {
		while (!isNumber(current())) {
			toNextIndex();
			if (isEnd()) break;
		}
	}
	
	public void skipUntilAlphabet() {
		while (!isAlphabet(current())) {
			toNextIndex();
			if (isEnd()) break;
		}
	}
	
	public boolean isNumber(char c) {
		return c >= '0' && c <= '9';
	}
	
	public boolean isAlphabet(char c) {
		return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_';
	}

}
