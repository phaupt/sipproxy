package util;

import java.util.Vector;

public class CharSetParser implements ICharSetParser {

    Vector<Character> charVec;

    public char[] getCharArray( String charSetString ) {
        charVec = new Vector<Character>();

        if (charSetString.length() >= 4 && charSetString.subSequence(0, 2).equals("0x")) {
            // Char set notation contains hex values
            extractHexValues(charSetString);
        }
        else if (charSetString.length() >= 1){
            // Char set notation contains characters
            extractCharValues(charSetString);
        }
        else{
        	// Char set empty
        	throw new IllegalArgumentException("Invalid CharSet !");
        }

        // Finally convert vector to array
        if (charVec.size() > 0){
            char[] charSet = new char[charVec.size()];
            for (int i = 0; i < charSet.length; i++) {
                charSet[i] = charVec.get(i);
            }
            return charSet;
        }
        
        return null;        
    }

    private void extractCharValues( String value ) {
        if (value.length() > 0) {
            int endPos = value.indexOf(',');
            if (endPos > -1) {
                // ',' has been found
                if (endPos == 1) {
                    if (value.charAt(0) == '\\') {
                        // \, (tail) or \,,
                        charVec.add(value.charAt(1));
                        // in case it is \,, (which is not tail)
                        if (value.length() > 2) {
                            extractCharValues(value.substring(3));
                        }
                    }
                    else {
                        // F,
                        charVec.add(value.charAt(0));
                        extractCharValues(value.substring(2));
                    }
                }
                else if (endPos == 3) {
                    // A-F,
                	if(value.charAt(1) == '-'){
                		fillUpCharRange(value.charAt(0), value.charAt(2));
                        extractCharValues(value.substring(4));
                	} else {
                		throw new IllegalArgumentException("Invalid CharSet !");
                	}
                }
                else if (endPos == 2) {
                    // \\,
                    if (value.charAt(0) == '\\') {
                        charVec.add(value.charAt(1));
                        extractCharValues(value.substring(3));
                    }
                    else{
                    	throw new IllegalArgumentException("Invalid CharSet !");
                    }
                }
                else {
                	// endPos == 0 or endPos >= 4
                	throw new IllegalArgumentException("Invalid CharSet !");
                }
            }
            else {
                // ',' has not been found
                if (value.length() == 1) {
                    // A (tail)
                	if (value.charAt(0) == '\\' ){
                		// \ (single escape character, tail)
                		throw new IllegalArgumentException("Invalid CharSet !");
                	}                		
                    charVec.add(value.charAt(0));
                }
                else if (value.length() == 3) {
                    // A-F (tail)
                	if (value.charAt(1) == '-')
                		fillUpCharRange(value.charAt(0), value.charAt(2));
                	else
                		throw new IllegalArgumentException("Invalid CharSet !");
                }
                else if (value.length() == 2) {
                    // \\ (tail)
                    if (value.charAt(0) == '\\') {
                        charVec.add(value.charAt(1));
                    } else {
                    	throw new IllegalArgumentException("Invalid CharSet !");
                    }
                }
                else {
                	//value.length >= 4
                	throw new IllegalArgumentException("Invalid CharSet !");
                }
            }
        }
    }

    private void extractHexValues( String value ) {
        if (value.length() > 0) {
            int endPos = value.indexOf(',');
            if (endPos > -1) {
                // ',' has been found
                if (endPos == 4) {
                    // 0xFF,
                    charVec.add(hexToChar(value.substring(0, 4)));
                    extractHexValues(value.substring(5));
                }
                else if (endPos == 9) {
                    // 0xAA-0xFF,
                	if (value.charAt(4) == '-'){
                		fillUpCharRange(hexToChar(value.substring(0, 4)),
                                hexToChar(value.substring(5, 9)));
                extractHexValues(value.substring(10));
                	} else {
                		throw new IllegalArgumentException("Invalid CharSet !");
                	}       
                }
                else {
                	throw new IllegalArgumentException("Invalid CharSet !");
                }
            }
            else {
                // ',' has not been found
                if (value.length() == 4) {
                    // 0xFF (tail)
                    charVec.add(hexToChar(value.substring(0, 4)));
                }
                else if (value.length() == 9) {
                    // 0xAA-0xFF (tail)
                	if (value.charAt(4) == '-'){
                		fillUpCharRange(hexToChar(value.substring(0, 4)),
                                hexToChar(value.substring(5, 9)));
                	} else {
                		throw new IllegalArgumentException("Invalid CharSet !");
                	}                    
                }
                else {
                	throw new IllegalArgumentException("Invalid CharSet !");
                }
            }
        }
    }

    private char hexToChar( String hexValue ) {
        return (char) Integer.parseInt(hexValue.substring(2), 16);
    }

    private void fillUpCharRange( char startChar, char endChar ) {
        if (startChar < endChar) {
            for (char c = startChar; c <= endChar; c++) {
                charVec.add(c);
            }
        }
    }

}
