/**
 * Assignment 08
 * Kyle Bueche
 * Section 3
 */

package edu.ics211.h08;

public class BadInputException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4206144717512994594L;

	BadInputException() {
		super();
	}
	
	BadInputException(String s) {
		super(s);
		System.out.println(s);
	}
}
