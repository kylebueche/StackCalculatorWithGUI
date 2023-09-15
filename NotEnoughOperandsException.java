/**
 * Assignment 08
 * Kyle Bueche
 * Section 3
 */

package edu.ics211.h08;

public class NotEnoughOperandsException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5751904630539280840L;
	
	NotEnoughOperandsException() {
		super();
	}
	
	NotEnoughOperandsException(String s) {
		super(s);
		System.out.println(s);
	}
}
