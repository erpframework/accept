package org.calculator;

import java.util.LinkedList;
import java.util.List;

public class Calculator {

	List<Integer> numbers = new LinkedList<Integer>();
	String showing = ""; 
	
	public void enter(int number) {
		numbers.add(number);
		showing = number + "";		
	}

	public void add() {
		int total = 0;
		for(Integer i:numbers) {
			total += i;
		}
		showing = total + "";
	}

	public String showing() {
		return showing;
	}

	public void subtract() {
		if (numbers.size() == 0) {
			return;
		}
		if (numbers.size() == 1) {
			showing = numbers.get(0) + "";
			return;
		}
		int total = numbers.get(0);
		for(int i = 1; i < numbers.size(); i++ ) {
			total -= numbers.get(i);
		}
		showing = total + "";
	}
}