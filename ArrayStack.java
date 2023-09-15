/**
 * Assignment 08
 * Kyle Bueche
 * Section 3
 */

package edu.ics211.h08;

/* 
 * a stack implemented using an array
 * @author	Biagioni, Edoardo
 * @assignment	lecture 7
 * @date	February 4, 2008
 */

import java.util.EmptyStackException;

public class ArrayStack<E> implements StackInterface<E> {
	/*
	 * fields to store the stack elements and the location of the top of the stack.
	 * the values are in array locations 0..top if top >= 0 for an empty array, top
	 * is -1
	 */
	private int top;
	private E[] array;

	/**
	 * checks assertion
	 * 
	 * @throws java.lang.AssertionError if the assertion is not true
	 */
	private void verify(boolean mustBeTrue) {
		if (!mustBeTrue) {
			throw new java.lang.AssertionError("assertion error");
		}
	}

	/**
	 * checks class invariants
	 * 
	 * @throws java.lang.AssertionError if the invariant is violated
	 */
	private void checkInvariants() {
		@SuppressWarnings("unused")
		E value;
		// uncomment the next line to skip the checks:
		// return;

		// array length is always 6, and must not be modified
		verify(array.length == 6);
		// top is always between -1 and the last index of the array, inclusive
		verify((top >= -1) & (top <= array.length - 1));
		// empty stacks must always have top equal to -1
		// this means that if a call to array[top] throws an exception,
		// top must be equal to -1
		try {
			value = array[top];
		} catch (java.lang.IndexOutOfBoundsException error) {
			verify(top == -1);
		}
		// full stacks must always have top equal to the last index of the array
		// this means that if a call to array[top + 1] throws an exception,
		// top must be equal to the last index of the array
		try {
			value = array[top + 1];
		} catch (java.lang.IndexOutOfBoundsException error) {
			verify(top == array.length - 1);
		}
	}

	/* no-arguments default constructor creates an empty stack */
	@SuppressWarnings("unchecked")
	public ArrayStack() {
		top = -1; // empty stack
		array = (E[]) (new Object[6]); // make room for at most 6 items
		checkInvariants();
	}

	/* @return whether the stack is empty */
	public boolean empty() {
		checkInvariants();
		return (top == -1);
	}

	/* @return whether the stack is empty */
	public boolean full() {
		checkInvariants();
		return (top == array.length - 1);
	}

	/* @param value to push onto the stack */
	public E push(E value) {
		checkInvariants();
		try {
			top++;
			array[top] = value;
			checkInvariants();
			return array[top];
		} catch (java.lang.ArrayIndexOutOfBoundsException error) {
			top = array.length - 1; // just to be sure
			checkInvariants();
			throw new FullStackException();
		}
	}

	/*
	 * different implementation of pop, does exactly the same.
	 * 
	 * @return the top value on the stack
	 */
	public E pop() throws EmptyStackException {
		checkInvariants();
		try {
			checkInvariants();
			return array[top--];
		} catch (java.lang.ArrayIndexOutOfBoundsException error) {
			top = -1; // just to be sure
			checkInvariants();
			throw new EmptyStackException();
		}
	}

	/* @return the top value on the stack */
	public E peek() throws EmptyStackException {
		checkInvariants();
		try {
			checkInvariants();
			return array[top];
		} catch (java.lang.ArrayIndexOutOfBoundsException error) {
			checkInvariants();
			throw new EmptyStackException();
		}
	}

	/*
	 * convert the stack to a printable string
	 * 
	 * @return a string representing the stack
	 */
	public String toString() {
		checkInvariants();
		if (empty()) {
			checkInvariants();
			return "Empty Stack";
		} else {
			checkInvariants();
			return recursiveToString(0);
		}
	}

	/*
	 * recursive method to print a non-empty stack
	 * 
	 * @param the starting index in the array
	 * 
	 * @return a string representing the stack
	 */
	private String recursiveToString(int startPos) {
		checkInvariants();
		if (startPos > top) {
			checkInvariants();
			return "";
		}
		String separator = "";
		if (startPos > 0) {
			separator = " :: ";
		}
		checkInvariants();
		return separator + array[startPos] + recursiveToString(startPos + 1);
	}

	// simple test
	public static void main(String[] args) {
		StackInterface<String> s = new ArrayStack<String>();
		System.out.println("before pushing anything, " + s);
		try {
			s.pop();
			System.out.println("error: empty stack popped, nothing to pop!");
		} catch (EmptyStackException error) {
			System.out.println("popping throws an EmptyStackException, as expected");
		}
		s.push("hello");
		s.push("world");
		System.out.println("after pushing hello and world, " + s);
		System.out.println("pop returns " + s.pop());
		System.out.println("after popping, " + s);
		StackInterface<Integer> si = new ArrayStack<Integer>();
		// push 100 values
		for (int i = 0; i < 6; i++) {
			si.push(i);
		}
		// now pop them and make sure the same values are returned
		// in LIFO order
		for (int i = 5; i >= 0; i--) {
			Integer returned = si.pop();
			if (!returned.equals(i)) {
				System.out.println("error: pop returns " + returned + ", expected " + i);
			}
		}
		s.push("a");
		s.push("beautiful");
		s.push("day");
		s.push("is");
		s.push("here");
		System.out.println("after pushing 'a beautiful day is here', " + s);
		try {
			s.push("tomorrow");
			System.out.println("error: stack overflowing, maximum 6 stacks allowed!");
		} catch (FullStackException error) {
			System.out.println("pushing 'tomorrow' throws a FullStackException, as expected");
		}
		System.out.println("pop returns " + s.pop());
		System.out.println("pop returns " + s.pop());
		System.out.println("pop returns " + s.pop());
		System.out.println("pop returns " + s.pop());
		System.out.println("pop returns " + s.pop());
		System.out.println("pop returns " + s.pop());
		System.out.println("after popping, " + s);

		try {
			s.pop();
			System.out.println("error: empty stack popped, nothing to pop!");
		} catch (EmptyStackException error) {
			System.out.println("popping throws an EmptyStackException, as expected");
		}
		/*
		 * expected output: before pushing anything, Empty Stack popping throws an
		 * EmptyStackException, as expected after pushing hello and world, hello ::
		 * world pop returns world after popping, hello after pushing 'a beautiful day
		 * is here', hello :: a :: beautiful :: day :: is :: here pushing 'tomorrow'
		 * throws a FullStackException, as expected pop returns here pop returns is pop
		 * returns day pop returns beautiful pop returns a pop returns hello after
		 * popping, Empty Stack popping throws an EmptyStackException, as expected
		 */

	}
}
