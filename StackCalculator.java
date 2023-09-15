/**
 * Assignment 08
 * Kyle Bueche
 * Section 3
 */

package edu.ics211.h08;

import edu.ics211.h07.FullStackException;

/**
 * postfix calculator that either implements a java.util.Stack<Double>, or a
 * StackInterface<Double>
 * 
 * can push numbers onto the specified stack, and can add, subtract, multiply,
 * divide, modulo, or exponentiate the last 2 values pushed onto the stack
 * 
 * operations are applied in the order that numbers are entered
 * 
 * @author kyleb
 *
 */
public class StackCalculator {
	private java.util.Stack<Double> stack1;
	private StackInterface<Double> stack2;
	boolean javaStandardStack;
	private double operand1, operand2;

	/**
	 * sets up a StackCalculator that implements a java.util.Stack<Double>
	 * 
	 * @param stack the stack to use for calculations
	 */
	public StackCalculator(java.util.Stack<Double> stack) {
		this.stack1 = stack;
		javaStandardStack = true;
	}

	/**
	 * sets up a StackCalculator that implements a StackInterface<Double>
	 * 
	 * @param stack the stack to use for calculations
	 */
	public StackCalculator(StackInterface<Double> stack) {
		this.stack2 = stack;
		javaStandardStack = false;
	}

	/**
	 * tries to get the last two operands pushed onto the stack
	 * 
	 * if the number of values in the stack is less than 2, the stack is returned to
	 * the state it was in before this method was called
	 * 
	 * @throws NotEnoughOperandsException if the number of values in the stack is
	 *                                    less than 2
	 */
	private void assignOperands() throws NotEnoughOperandsException {
		if (javaStandardStack) {
			try {
				operand2 = stack1.pop();
			} catch (java.util.EmptyStackException e) { // no operands
				throw new NotEnoughOperandsException();
			}
			try {
				operand1 = stack1.pop();
			} catch (java.util.EmptyStackException e) { // only one operand
				stack1.push(operand2); // put the operand back into place
				throw new NotEnoughOperandsException();
			}
		} else {
			try {
				operand2 = stack2.pop();
			} catch (java.util.EmptyStackException e) { // no operands
				throw new NotEnoughOperandsException();
			}
			try {
				operand1 = stack2.pop();
			} catch (java.util.EmptyStackException e) { // only one operand
				stack2.push(operand2); // put the operand back into place
				throw new NotEnoughOperandsException();
			}
		}
	}
	
	/**
	 * pushes a value onto the stack, returns the value entered
	 * @param value the value to push onto the stack
	 * @return value the value pushed onto the stack
	 * @throws FullStackException if the stack is full and cannot be pushed
	 */
	public double enterNum(double value) throws FullStackException {
		if (javaStandardStack) {
			stack1.push(value);
		} else {
			stack2.push(value);
		}
		return value;
	}
	
	/**
	 * adds the last two values pushed onto the stack
	 * 
	 * pushes the addition of the values onto the stack
	 * @return the addition of the values
	 * @throws NotEnoughOperandsException if the number of values in the stack is
	 *                                    less than 2
	 */
	public double add() throws NotEnoughOperandsException {
		assignOperands();
		double addition = (operand1 + operand2);
		enterNum(addition);
		return addition;
	}

	/**
	 * subtracts the last two values pushed onto the stack
	 * 
	 * pushes the subtraction of the values onto the stack
	 * @return the subtraction of the values
	 * @throws NotEnoughOperandsException if the number of values in the stack is
	 *                                    less than 2
	 */
	public double subtract() throws NotEnoughOperandsException {
		assignOperands();
		double subtraction = (operand1 - operand2);
		enterNum(subtraction);
		return subtraction;
	}
	
	/**
	 * multiplies the last two values pushed onto the stack
	 * 
	 * pushes the multiple of the values onto the stack
	 * @return the multiple of the values
	 * @throws NotEnoughOperandsException if the number of values in the stack is
	 *                                    less than 2
	 */
	public double multiply() throws NotEnoughOperandsException {
		assignOperands();
		double multiple = (operand1 * operand2);
		enterNum(multiple);
		return multiple;
	}
	
	/**
	 * divides the last two values pushed onto the stack
	 * 
	 * pushes the quotient of the values onto the stack
	 * @return the quotient of the values
	 * @throws NotEnoughOperandsException if the number of values in the stack is
	 *                                    less than 2
	 */
	public double divide() throws NotEnoughOperandsException {
		assignOperands();
		double quotient = (operand1 / operand2);
		enterNum(quotient);
		return quotient;
	}
	
	/**
	 * finds the remainder of the division of the last two values pushed onto the stack
	 * 
	 * pushes the remainder of the division of the values onto the stack
	 * @return the remainder of the division of the values
	 * @throws NotEnoughOperandsException if the number of values in the stack is
	 *                                    less than 2
	 */
	public double remainder() throws NotEnoughOperandsException {
		assignOperands();
		double remainder = (operand1 % operand2);
		enterNum(remainder);
		return remainder;
	}
	
	/**
	 * exponentiates the last two values pushed onto the stack
	 * 
	 * pushes the exponentiation of the values onto the stack
	 * @return the exponentiation of the values
	 * @throws NotEnoughOperandsException if the number of values in the stack is
	 *                                    less than 2
	 */
	public double exponent() throws NotEnoughOperandsException {
		assignOperands();
		double exponentiation = (Math.pow(operand1, operand2));
		enterNum(exponentiation);
		return exponentiation;
	}
}
