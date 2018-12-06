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
	
	
	public int readNextInt() throws EndOfLineException {
		int next = 0;
		skipUntilNumber();
		if (isEnd()) throw new EndOfLineException("Attempt to read int but not found");
		while (isNumber(current())) {
			next *= 10;
			next += current() - '0';
		}
		return next;
	}
	
	public String readNextWord() throws EndOfLineException {
		skipUntilAlphabet();
		if (isEnd()) throw new EndOfLineException("Attempt to read word but not found");
		int beginIndex = getIndex();
		while (isAlphabet(current())) {
			toNextIndex();
		}
		int endIndex = getIndex();
		return line.substring(beginIndex, endIndex - beginIndex);
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
		return c >= '0' && c <= '9' || c == '.';
	}
	
	public boolean isAlphabet(char c) {
		return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_';
	}

}
