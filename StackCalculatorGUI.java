/**
 * Assignment 08
 * Kyle Bueche
 * Section 3
 */

package edu.ics211.h08;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * GUI front end for a StackCalculator.
 * 
 * calculator allows user to choose between 3 different types of stacks:
 * java.util.Stack<Double>, ArrayStack<Double> and LinkedStack<Double>
 * 
 * Adding support for other StackInterface<Double> objects only requires editing
 * this class
 * 
 * Calculator functions include
 * 
 * @author kyleb
 *
 */
public class StackCalculatorGUI {
	GraphicsEnvironment displayInfo;
	Rectangle displaySettings;
	JButton button9;
	JButton button8;
	JButton button7;
	JButton button6;
	JButton button5;
	JButton button4;
	JButton button3;
	JButton button2;
	JButton button1;
	JButton button0;
	JButton plus;
	JButton minus;
	JButton multiply;
	JButton divide;
	JButton modulo;
	JButton exponent;
	JButton signChange;
	JButton decimal;
	JButton enter;
	JButton backSpace;
	int windowWidth;
	int windowHeight;
	int windowX;
	int windowY;
	JFrame frame;
	JPanel containerPanel;
	JPanel textPanel;
	JPanel buttonPanel;
	JTextArea textArea;
	JTextField textField;
	JScrollPane scrollPane;
	JScrollBar scrollBar;
	NumBuilder numBuilder;
	StackCalculator stackCalc;
	int stackType;

	/**
	 * class that chains digits together in order to build a decimal number in a
	 * JTextField
	 * 
	 * can add an unlimited number of digits, but not recommended for extremely
	 * precise calculations
	 * 
	 * the accuracy of this class for decimal numbers depends on java's
	 * interpretation of the double 0.100000000000000000000000000000000000000000,
	 * when multiplied against itself, as well as other doubles which represent
	 * integers
	 * 
	 * @author kyleb
	 *
	 */
	private class NumBuilder {
		private boolean decimalEntered;
		private boolean positiveNum;
		private LinkedNode intBuilder;
		private LinkedNode decBuilder;
		private double decPlace;
		private double tenthConstant;

		/**
		 * simple LinkedNode for chaining digits
		 * 
		 * @author kyleb
		 *
		 */
		private class LinkedNode {
			public double value;
			public LinkedNode next;

			LinkedNode(double value) {
				this.value = value;
				next = null;
			}

			LinkedNode(double value, LinkedNode next) {
				this.value = value;
				this.next = next;
			}
		}

		/**
		 * sets initial values tenthConstant still doesn't eliminate approximations, but
		 * is much better than using 0.10
		 */
		NumBuilder() {
			textField.setText("   ");
			decimalEntered = false;
			positiveNum = true;
			intBuilder = null;
			decBuilder = null;
			// approximations get sloppy without the extra zeros
			tenthConstant = 0.100000000000000000000000000000000000000000;
			decPlace = tenthConstant;
			stackType = 0;
		}

		/**
		 * if a decimal has not been entered, adds digits to a LinkedList, the head of
		 * which represents the ones place
		 * 
		 * if a decimal has been entered, adds digits instead to a seperate LinkedList
		 * whose head represents the final digit, and whose tail represents the tenths
		 * place
		 * 
		 * @param digit       the digit to add
		 * @param digitString the String representation of the digit for the user
		 */
		public void addDigit(int digit, String digitString) {
			if (!decimalEntered) {
				if (intBuilder != null) {
					intBuilder = new LinkedNode(digit, intBuilder);
				} else {
					intBuilder = new LinkedNode(digit);
				}
			} else {
				if (decBuilder != null) {
					decBuilder = new LinkedNode(digit, decBuilder);
					decPlace = (decPlace * tenthConstant);
				} else {
					decBuilder = new LinkedNode(digit);
					decPlace = tenthConstant;
				}
			}
			textField.setText(textField.getText() + digitString);
		}

		/**
		 * adds a decimal digit, all digits added will be added to the right of this
		 * decimal and the numbers which follow it.
		 * 
		 * @throws BadInputException if a decimal has already been entered
		 */
		public void addDecimal() throws BadInputException {
			if (decimalEntered) {
				throw new BadInputException();
			}
			if (intBuilder == null) {
				intBuilder = new LinkedNode(0);
				textField.setText(textField.getText() + Character.toString(0));
			}
			decimalEntered = true;
			textField.setText(textField.getText() + ".");
		}

		/**
		 * removes the rightmost digit, the period counts as a digit in this case
		 * 
		 * @throws BadInputException if there are no digits to remove
		 */
		public void removeDigit() throws BadInputException {
			if (intBuilder == null) {
				throw new BadInputException();
			}
			if (decBuilder == null) {
				if (decimalEntered) {
					decimalEntered = false;
				} else {
					intBuilder = intBuilder.next;
				}
			} else {
				decBuilder = decBuilder.next;
				decPlace = (decPlace * 10);
			}
			int lastIndex = (textField.getText().length() - 1);
			textField.setText(textField.getText().substring(0, lastIndex));
		}

		/**
		 * toggles a setting that will determine if the number is positive or negative
		 * when built, positive by default
		 */
		public void toggleSign() {
			if (positiveNum) {
				textField.setText(textField.getText().replace("   ", " - "));
				positiveNum = false;
			} else {
				textField.setText(textField.getText().replace(" - ", "   "));
				positiveNum = true;
			}

		}

		/**
		 * builds the number with each digit in its proper place
		 * 
		 * @return the built number
		 * @throws BadInputException if there are no digits entered, or if there is a
		 *                           decimal with no digits to the right of it
		 */
		public double buildNum() throws BadInputException {
			if ((intBuilder == null) || (decimalEntered & (decBuilder == null))) {
				throw new BadInputException();
			}
			LinkedNode intPlacer = intBuilder;
			LinkedNode decPlacer = decBuilder;
			double place = 1;
			double num = 0;
			while (intPlacer != null) {
				num += (place * intPlacer.value);
				place = (place * 10);
				intPlacer = intPlacer.next;
			}
			if (decimalEntered) {
				place = decPlace;
				while (decPlacer != null) {
					num += (place * decPlacer.value);
					place = (place * 10);
					decPlacer = decPlacer.next;
				}
			}
			textField.setText("   ");
			scrollBar.validate();
			scrollBar.setValue(scrollBar.getMaximum());
			if (positiveNum) {
				return num;
			} else {
				return (-1 * num);
			}
		}
	}

	/**
	 * creates the GUI for the stackCalculator, which prompts the user to choose
	 * between 3 different types of stacks:
	 * 
	 * java.util.Stack<Double>, ArrayStack<Double>, and LinkedStack<Double>
	 * 
	 * once the stack type is chosen, the user can use the calculator freely until
	 * they close the window
	 */
	StackCalculatorGUI() {
		displayInfo = GraphicsEnvironment.getLocalGraphicsEnvironment();
		displaySettings = displayInfo.getMaximumWindowBounds();

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setMargin(new Insets(0, 10, 10, 10));

		scrollPane = new JScrollPane(textArea);
		scrollBar = scrollPane.getVerticalScrollBar();

		textField = new JTextField();
		textField.setEditable(false);
		textField.setMargin(new Insets(0, 5, 10, 5));
		textField.setBackground(Color.white);

		textPanel = new JPanel();
		textPanel.setLayout(new BorderLayout());
		textPanel.add(scrollPane, BorderLayout.CENTER);
		textPanel.add(textField, BorderLayout.SOUTH);

		button9 = new JButton("9");
		button8 = new JButton("8");
		button7 = new JButton("7");
		button6 = new JButton("6");
		button5 = new JButton("5");
		button4 = new JButton("4");
		button3 = new JButton("3");
		button2 = new JButton("2");
		button1 = new JButton("1");
		button0 = new JButton("0");
		plus = new JButton("+");
		minus = new JButton("-");
		multiply = new JButton("*");
		divide = new JButton("/");
		modulo = new JButton("%");
		exponent = new JButton("^");
		signChange = new JButton("+/-");
		decimal = new JButton(".");
		enter = new JButton("enter");
		backSpace = new JButton("<-");

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(5, 4));
		buttonPanel.add(plus);
		buttonPanel.add(minus);
		buttonPanel.add(multiply);
		buttonPanel.add(divide);
		buttonPanel.add(button7);
		buttonPanel.add(button8);
		buttonPanel.add(button9);
		buttonPanel.add(exponent);
		buttonPanel.add(button4);
		buttonPanel.add(button5);
		buttonPanel.add(button6);
		buttonPanel.add(modulo);
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);
		buttonPanel.add(backSpace);
		buttonPanel.add(signChange);
		buttonPanel.add(button0);
		buttonPanel.add(decimal);
		buttonPanel.add(enter);

		containerPanel = new JPanel();
		containerPanel.setLayout(new GridLayout(2, 1));
		containerPanel.add(textPanel);
		containerPanel.add(buttonPanel);

		frame = new JFrame("Stack Based Calculator");
		frame.getContentPane().add(containerPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		windowWidth = (int) displaySettings.width / 6; // width should be about half of the screen
		windowHeight = (int) (displaySettings.height / 2); // height should be about a 3rd of the screen
		windowX = (int) displaySettings.getCenterX() - (windowWidth / 2);
		windowY = (int) displaySettings.getCenterY() - (windowHeight / 2);
		frame.setBounds(windowX, windowY, windowWidth, windowHeight);

		numBuilder = new NumBuilder();
		chooseStackType();

		frame.setVisible(true);
	}

	/**
	 * Prompts the user to choose the stack type, and sets up the calculator
	 */
	private void chooseStackType() {
		setText("Stack Types:\n1 Java Standard Library\n2 Array Stack\n3 Linked Stack\n\n");
		append("Click 1, 2, or 3\n\n");

		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stackCalc = new StackCalculator(new java.util.Stack<Double>());
				append("Java Standard Library Stack Chosen\n\n");
				button1.removeActionListener(button1.getActionListeners()[0]);
				button2.removeActionListener(button2.getActionListeners()[0]);
				button3.removeActionListener(button3.getActionListeners()[0]);
				addActionListeners();
				scrollPane.validate();
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stackCalc = new StackCalculator(new ArrayStack<Double>());
				append("Array Stack Chosen\nStack can only hold 6 numbers at a time\n\n");
				button1.removeActionListener(button1.getActionListeners()[0]);
				button2.removeActionListener(button2.getActionListeners()[0]);
				button3.removeActionListener(button3.getActionListeners()[0]);
				addActionListeners();
				scrollPane.validate();
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stackCalc = new StackCalculator(new LinkedStack<Double>());
				append("Linked Stack Chosen\n\n");
				button1.removeActionListener(button1.getActionListeners()[0]);
				button2.removeActionListener(button2.getActionListeners()[0]);
				button3.removeActionListener(button3.getActionListeners()[0]);
				addActionListeners();
				scrollPane.validate();
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});
	}
	
	/**
	 * sets up the ActionListeners for each button
	 */
	private void addActionListeners() {
		button0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numBuilder.addDigit(0, "0");
			}
		});
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numBuilder.addDigit(1, "1");
			}
		});
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numBuilder.addDigit(2, "2");
			}
		});
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numBuilder.addDigit(3, "3");
			}
		});
		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numBuilder.addDigit(4, "4");
			}
		});
		button5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numBuilder.addDigit(5, "5");
			}
		});
		button6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numBuilder.addDigit(6, "6");
			}
		});
		button7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numBuilder.addDigit(7, "7");
			}
		});
		button8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numBuilder.addDigit(8, "8");
			}
		});
		button9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numBuilder.addDigit(9, "9");
			}
		});
		signChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numBuilder.toggleSign();
			}
		});
		decimal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					numBuilder.addDecimal();
				} catch (BadInputException e1) {
					// do nothing
				}
			}
		});
		backSpace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					numBuilder.removeDigit();
				} catch (BadInputException e1) {
					// do nothing
				}
			}
		});
		enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Double builtNum = null;
				boolean append = true;
				try {
					builtNum = numBuilder.buildNum();
				} catch (BadInputException e1) {
					append = false;
					// do nothing
				}
				if (append) {
					try {
						stackCalc.enterNum(builtNum);
						appendln(builtNum.toString());
					} catch (FullStackException e1) {
						appendln("Input Error: Stack is Full");
					}
					numBuilder = new NumBuilder();
				}
				scrollPane.validate();
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean append = true;
				Double value = 0.00;
				try {
					value = stackCalc.add();
				} catch (NotEnoughOperandsException e1) {
					append = false;
				}
				if (append) {
					appendln("+");
					appendln("	" + value.toString());
				}
				scrollPane.validate();
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean append = true;
				Double value = 0.00;
				try {
					value = stackCalc.subtract();
				} catch (NotEnoughOperandsException e1) {
					append = false;
				}
				if (append) {
					appendln("-");
					appendln("	" + value.toString());
				}
				scrollPane.validate();
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});
		multiply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean append = true;
				Double value = 0.00;
				try {
					value = stackCalc.multiply();
				} catch (NotEnoughOperandsException e1) {
					append = false;
				}
				if (append) {
					appendln("*");
					appendln("	" + value.toString());
				}
				scrollPane.validate();
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});
		divide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean append = true;
				Double value = 0.00;
				try {
					value = stackCalc.divide();
				} catch (NotEnoughOperandsException e1) {
					append = false;
				}
				if (append) {
					appendln("/");
					appendln("	" + value.toString());
				}
				scrollPane.validate();
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});
		modulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean append = true;
				Double value = 0.00;
				try {
					value = stackCalc.remainder();
				} catch (NotEnoughOperandsException e1) {
					append = false;
				}
				if (append) {
					appendln("%");
					appendln("	" + value.toString());
				}
				scrollPane.validate();
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});
		exponent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean append = true;
				Double value = 0.00;
				try {
					value = stackCalc.exponent();
				} catch (NotEnoughOperandsException e1) {
					append = false;
				}
				if (append) {
					appendln("^");
					appendln("	" + value.toString());
				}
				scrollPane.validate();
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});
	}
	
	// returns the current text
	public String getText() {
		return textArea.getText();
	}
	
	// sets the current text
	public void setText(String s) {
		textArea.setText(s);
	}
	
	// adds to the current text
	public void append(String s) {
		textArea.append(s);
	}
	
	// adds a newline to the current text
	public void newLine() {
		textArea.append("\n");
	}
	
	// adds to the current text, then adds a newline
	public void appendln(String s) {
		append(s);
		newLine();
	}
	
	// runs the calculator
	public static void main(String[] args) {
		StackCalculatorGUI calcGUI = new StackCalculatorGUI();
	}
}
