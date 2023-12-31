/*
 * Student:   Andrew Blackwell
 * Professor: Dr. Salimi
 * Course: 	  CSCI4200: Programming Languages
 * Date:      16 November 2023
 */
 
import java.util.*;
import java.io.*;

// Custom exception to handle errors
class CustomException extends Exception {
	public CustomException(String errorMessage) {
		 super(errorMessage);
	}
}

public class Lex {

	static String lexeme;   
	static String[] lexemeList;
	static int currLexListIndex;
	static Token nextToken;
	static FileWriter myOutput;

	//Tokens and categories
	enum Token {
		INT_LIT,
		IDENT,
		ASSIGN_OP,
		ADD_OP,
		SUB_OP,
		MULT_OP,
		DIV_OP,
		LEFT_PAREN,
		RIGHT_PAREN,
		LETTER,
		DIGIT,
		UNKNOWN,
		END_KEYWORD,
		PRINT_KEYWORD,
		PROGRAM_KEYWORD,
		SEMICOLON,
		END_OF_FILE,
		IF_KEYWORD,
		READ_KEYWORD,
		THEN_KEYWORD,
		ELSE_KEYWORD
	}
 
	// Driver method
	// @throws IOException
	public static void main(String[] args) throws IOException {

		String line;
		Scanner scan;
	    myOutput = new FileWriter("lexOutput.txt");

		// Print Header Info to Terminal and Output File
		String headerInfo = "Andrew Blackwell, CSCI4200, Fall 2023, Lexical Analyzer";
		String headerAsterisks = "*".repeat(80);
		System.out.print(headerInfo + "\n"); 
		myOutput.write(headerInfo + "\n");
		System.out.print(headerAsterisks + "\n");
		myOutput.write(headerAsterisks + "\n");

		// Open the file, and scan each line for lexical analysis 
		try {
			scan = new Scanner(new File("lexInput.txt"));

			// For each line, get each character 
			while (scan.hasNextLine()) {

				line = scan.nextLine().trim();         // Trims leading and trailing whitespace from each line

				if(!line.trim().isEmpty()){            // Skip blank lines in input file
					System.out.println(line);
					myOutput.write(line + "\n");

					// Take the line and add spaces where appropriate so that split function can be used.
					line = spaceOutLexemes(line);
					lexemeList = line.split(" ");
					currLexListIndex = 0;
					printTokensAndLexemes(lexemeList);
					myOutput.write("\n");
				}
			}

			// If there are no more lines, it must be the end of file 
			if (!scan.hasNext()) {
				System.out.printf("Next token is: %-18s Next lexeme is %s\n", "END_OF_FILE", "EOF");
				myOutput.write(String.format("Next token is: %-18s Next lexeme is %s\n", "END_OF_FILE", "EOF"));
				System.out.print("Lexical analysis of the program is complete!\n");
				myOutput.write("Lexical analysis of the program is complete!");
			}
			
			scan.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Cannot find file OR Problem with output file creation.");
			myOutput.write("Cannot find file OR Problem with output file creation." + "\n");
			System.out.println(e.toString());
			myOutput.write(e.toString());
		}

		catch (Exception e) {
			e.printStackTrace();
			StringWriter stringWriter = new StringWriter();
			e.printStackTrace(new PrintWriter(stringWriter));
			String eStackTrace = stringWriter.toString();
			myOutput.write(eStackTrace);
		}
		
		myOutput.close();
	}

	
	/*
	 * This method takes a line and applies spaces where needed to separate each lexeme.
	 * Then clears any extra spaces and replaces with 1 space.
	 */
	public static String spaceOutLexemes(String string) {
		
		String spacedOutString = "";
		
		for (int currentChar = 0; currentChar < string.length(); ++currentChar) {
			if (string.charAt(currentChar) == '=' ||
				string.charAt(currentChar) == '+' ||
				string.charAt(currentChar) == '-' ||
				string.charAt(currentChar) == '*' ||
				string.charAt(currentChar) == '/' ||
				string.charAt(currentChar) == '(' ||
				string.charAt(currentChar) == ')' ||
				string.charAt(currentChar) == ';') {

				spacedOutString = spacedOutString.concat(" " + String.valueOf(string.charAt(currentChar)) + " ");
			} else {
				spacedOutString = spacedOutString.concat(String.valueOf(string.charAt(currentChar)));
			}
		}
		spacedOutString = spacedOutString.replaceAll("\\s+", " ");
		return spacedOutString;
	}

	
	/*
	 * This method takes the array of strings that represent individual lexemes.
	 * Then matches each lexeme to a token
	 */
	public static void matchLexemeToToken(String lexeme) {
		
		if (lexeme.matches("[0-9]+")) {
			nextToken = Token.INT_LIT;
		} 
		else if (lexeme.matches("=")) {
			nextToken = Token.ASSIGN_OP;
		} 
		else if (lexeme.matches("\\+")) {
			nextToken = Token.ADD_OP;
		}
		else if (lexeme.matches("\\-")) {
			nextToken = Token.SUB_OP;
		}
		else if (lexeme.matches("\\*")) {
			nextToken = Token.MULT_OP;
		}
		else if (lexeme.matches("\\/")) {
			nextToken = Token.DIV_OP;
		}
		else if (lexeme.matches("\\(")) {
			nextToken = Token.LEFT_PAREN;
		}
		else if (lexeme.matches("\\)")) {
			nextToken = Token.RIGHT_PAREN;
		}
		else if (lexeme.matches("END")) {
			nextToken = Token.END_KEYWORD;
		}
		else if (lexeme.matches("print")) {
			nextToken = Token.PRINT_KEYWORD;
		}
		else if (lexeme.matches("PROGRAM")) {
			nextToken = Token.PROGRAM_KEYWORD;
		}
		else if (lexeme.matches(";")) {
			nextToken = Token.SEMICOLON;
		}
		else if (lexeme.matches("if")) {
			nextToken = Token.IF_KEYWORD;
		}
		else if (lexeme.matches("read")) {
			nextToken = Token.READ_KEYWORD;
		}
		else if (lexeme.matches("then")) {
			nextToken = Token.THEN_KEYWORD;
		}
		else if (lexeme.matches("else")) {
			nextToken = Token.ELSE_KEYWORD;
		}
		//The IDENT Token is at the end, so we can check for if, then, end, print, program and read keywords first
		else if (lexeme.matches("[a-zA-Z]+")) {
			nextToken = Token.IDENT;
		} 
		else 
		{
			nextToken = Token.UNKNOWN;
		}
	}
	
	
	/*
	 * Prints out the token and lexeme value
	 */
	public static void printTokensAndLexemes(String[] lexList) throws IOException {
		
		for (int i = 0; i < lexList.length; ++i) {
			lex();
			
			System.out.printf("Next token is: %-18s Next lexeme is %s\n", String.valueOf(nextToken), String.valueOf(lexeme));
			myOutput.write(String.format("Next token is: %-18s Next lexeme is %s\n", String.valueOf(nextToken), String.valueOf(lexeme)));
		}
		System.out.println();
	}
	
	
	/*
	 * NEW lex function. 
	 * Gets the lexeme at the current index and matches it to a token. 
	 * Sets nextToken accordingly
	 * Increments current lexeme index
	 */
	private static void lex() {
		if (currLexListIndex < lexemeList.length) {
			lexeme = lexemeList[currLexListIndex];
			matchLexemeToToken(lexeme);
			currLexListIndex++;
		}
	}
}