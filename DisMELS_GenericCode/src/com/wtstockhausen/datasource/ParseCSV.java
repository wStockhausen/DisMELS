// Simple class to break up comma delimited files into fields and records.

package com.wtstockhausen.datasource;

import java.util.*;
import java.io.*;

/**
* Class to split comma delimited text files into fields.
*
* Splits comma delimited data fields in a text file into an
* array of strings.  One unamed company calls them .CSV files.
* They are commonly the result of spreadsheet and database exports.
* <P>
* It follows these conventions:
* <UL>
* <LI>
* Fields are separated by commas only, no white space.
* Missing values are represented by nothing between commas.
* Records are separated by newline characters (\r, \n or any combination).
* </LI>
* <LI>
* If a field contains white space (\n, \n, \t, ' ') it must
* be enclosed in double-quotation marks ('"').  It will be refered
* to as a string field.
* </LI>
* If a string field contains double-quotes, they are escaped by placing
* two double-quotes together. E.g. <SAMP> "I said, ""Hello.""" </SAMP>
* </LI>
* A string field may contain newline characters and is always terminated
* by a double-quote.
* <LI>
* </UL>
* <P> A simple example:
* <SAMP> 23,"Hello World",9834.99,"15.7",,,"I said, ""Hello.""","Hi!" </SAMP>
* This will be returned as a string array:
* <BR> 23
* <BR> Hello World
* <BR> 9834.99
* <BR> 15.7
* <BR>
* <BR>
* <BR> I said, "Hello."
* <BR> Hi!
* <P>
* <P>
*
* This could be expanded to return the type of the field found; like string,
* integer, real, currency, etc.
*
* Copyright 1998, 1999 Michael Lecuyer, all rights reserved.
* <P>
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
* <P>
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* <P>
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* <P>
* @author Michael Lecuyer (mjl@theorem.com)
* @version 1.0 Oct 28, 1999
* @since JDK1.1
*/
public class ParseCSV
{
	BufferedReader br;    // Buffered input source.
        boolean open = false; //flag whether input source is open or closed

	// Encodings
	private final char dq  =   '\u0001';    // Embedded double quote
	private final char comma = '\u0002';    // Embedded comma in a field.

	/**
	* Test code.  Reads comma a comma delimited file and displays the fields.
	* The file name is taken from the command line.
	*/
	public static void main(String args[])
	{
		BufferedReader bri = null;
                
                javax.swing.JFileChooser jFC = new javax.swing.JFileChooser();
                int iR = jFC.showOpenDialog(null);
                if (iR != jFC.APPROVE_OPTION) return;
                
		try {
			bri = new BufferedReader(new FileReader(jFC.getSelectedFile()));
		} catch (FileNotFoundException fnfe)
		{
			System.err.println("File " + args[0] + " not found: " + fnfe);
			System.exit(0);
		}

		ParseCSV p = new ParseCSV(bri);
		String list[];

		while ((list = p.decodeLine()) != null)
		{
			System.out.println("Field list: ");
			for (int i = 0; i < list.length; i++)
				System.out.println(i + " [" + list[i] + "] ");
			System.out.println();
		}

	}

	/**
	* Parser constructor.
	*/
	public ParseCSV(BufferedReader br)
	{
		this.br = br;
                open = true;
	}

	/**
	* Decode a comma delimited line.
	* Uses the BufferedReader read and parse one lines of text.
	*
	* @return array of fields found in the line.
	*/
	public String [] decodeLine()
	{
		String field[];
		int count;

		String line = prep();
                //System.out.println("decodeLine: [" + line +"]");

		if (line == null)
			return null;

		field = split(line);
		count = field.length;

		for (int i = 0; i < count; i++)
			field[i] = field[i].replace(dq, '"').replace(comma, ',');
		return field;
	}

	/**
	* Prepare a CSV line for splitting along commas.
	* Special chars ("" and commas) are encoded to prevent mis-splitting.
	* They are repaird by decodeLine().
	*
	* @return string, or null if at eof.
	*/
	private String prep()
	{
		char c;         // Current character under examination.
		char nc;                // Next character (look-ahead).
		boolean inner = false;  // State - false if we're outside a field, true if we're within one.
		StringBuffer sb = new StringBuffer();   // Accumulate string.

		c = getc();	 // Get a char.
		nc = getc();	// This will be the lookahead character.

		while (nc != (char)-1)
		{
			switch (c)
			{
				case '\n':              // Ignore newlines and EOFs. Return what we have.
				case '\r':
				case (char)-1:
					if (sb.length() == 0)
						return null;	// EOF.
					return sb.toString();
//System.out.println("OUTER: Newline/EOF - returning: " + sb);

				case '"':               // Ignore quotes, but note that they start a string field.
					inner = true;
					break;

				case ',':               // Accumulate separator chars and anything else
				default:                        // in the line.
					sb.append(c);
//System.out.println("OUTER: appending: [" + c + "] " + sb);
					break;
			}

			// We're parsing a string field.
			while (inner == true)
			{
				c = nc;
				nc = getc();

				switch (c)
				{
					case '"':       // If there are two, we keep it (encoded).  Else we're outa here.
						if (nc != '"')
						{
							inner = false;
//System.out.println("INNER: single quote found, leaving INNER " + sb);
							break;
						}
						sb.append(dq);
						c = nc;         // get a char, 'cause we ate the nc.
						nc = getc();
//System.out.println("INNER: DQ found " + sb);
							break;
					case '\n':      // Ignore newlines - this should be optional.
					case '\r':
//System.out.println("INNER: NL found, ignoring it.");
						break;

					case ',':               // Encode embedded commas so they aren't valid sparators.
						sb.append(comma);
//System.out.println("INNER: comma found: specail append: " + sb);
						break;

					case (char)-1:  // eof's at this point are unexpected and unwelcome.
//System.out.println("INNER: EOF found: leaving INNER: " + sb);
						inner = false;
						break;

					default:                        // just a char, save it.
						sb.append(c);
//System.out.println("INNER: appending char: " + sb);
						break;

				}
			}

			// This bit at then keeps things sane for UNIX & DOS/Windows text files.
			c = nc;

			if (c == '\n')
			{
				nc = '\n';
				c = '\n';
			} else {
				nc = getc();
			}
		}

		if (sb.length() == 0)
			return null;

		return sb.toString();
	}
	
	/**
	* Split a line into an array using a comma as the delimiter.
	*
	* @param s  String to split
	* @return array of strings.
	*/
	static public String [] split(String s)
	{
		// Count the commas
		int commaCount = 0;
		int slen = s.length();
		for (int i = 0; i < slen; i++)
			if (s.charAt(i) == ',')
				commaCount++;

		String r[] = new String[commaCount + 1];
		
		// Assemble the fields.
		int start = 0;
		int stop;
		commaCount = 0;
		while ((stop = s.indexOf(',', start)) > -1)
		{
			r[commaCount++] = s.substring(start, stop);
			start = stop + 1;
		}

		r[commaCount] = s.substring(start);

		return r;
	}

	/**
	* Read a character.
	*
	* @return char read or -1 if EOF
	*/
	private char getc()
	{
		char c;
		try {
			return (char)br.read();
		} catch (IOException ioe) {
			return (char)-1;
		}
	}
        
        /**
         * Close the BufferedReader instance that maintains the file connection.
         * @throws java.io.IOException
         */
        public void close() throws IOException {
            br.close();
            open = false;
        }
        
        public boolean isOpen() {
            return open;
        }

}