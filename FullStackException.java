/**
 * Assignment 08
 * Kyle Bueche
 * Section 3
 */

package edu.ics211.h08;

public class FullStackException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9185380360090569409L;

	FullStackException(){
		super();
	}
	
	FullStackException(String s){
		super(s);
		System.out.println(s);
	}
}
