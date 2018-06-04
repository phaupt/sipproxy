package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import proxy.UDPDatagram;
import config.transformation.RegexRule;

public class MiscUtil {

    public String validateRegeExp( RegexRule rule, String input ) {
        Pattern p = Pattern.compile(rule.getRegex(), Pattern.MULTILINE);
        Matcher m = p.matcher(input);
        StringBuffer stb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(stb, rule.getReplacement());
        }
        m.appendTail(stb);
        return stb.toString();
    }

    public static String getCSeq( UDPDatagram sipMessage ) {
        String cSeq = "";
        String data = new String(sipMessage.getData());
        Pattern pattern = Pattern.compile("^(CSeq: )(.*)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            cSeq = matcher.group(2);
        }
        return cSeq;
    }

    public static boolean deleteDebugLog(File file) {
        if (file.exists())
            return file.delete();
        else
            return false;
    }

    public static void saveFile( File outputFile, File logger ) {
        FileReader fileReader;
        FileWriter fileWriter;
        try {
            fileReader = new FileReader(logger);
            fileWriter = new FileWriter(outputFile);
            int c;

            while ((c = fileReader.read()) != -1) {
                fileWriter.write(c);
            }

            fileWriter.flush();

            fileReader.close();
            fileWriter.close();
        }
        catch (IOException e1) {
            SimpleLogger.log(e1.getMessage(), SimpleLogger.ERROR_LOG);
        }
    }

    public static void saveTestCaseContent( File outputFile, String content ){
        FileWriter fw; 
        BufferedWriter bw;
            try {
                fw = new FileWriter(outputFile);
                bw = new BufferedWriter(fw); 
                bw.write(content); 
                bw.close(); 
            }
            catch (IOException e) {
                SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
            } 
    }
    
    public static void saveContent( File outputFile, byte[] content ) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(outputFile);
            fos.write(content);
        }
        catch (IOException e1) {
            SimpleLogger.log(e1.getMessage(), SimpleLogger.ERROR_LOG);
        }
    }
    
    public static String getTimestamp(String format){
        DateFormat sdf = new SimpleDateFormat(format);
        Date now = new Date();
        return sdf.format(now); 
    }
    
    public static String getTimestamp(String formatIn, String timestamp, String formatOut){
        DateFormat sdfIn = new SimpleDateFormat(formatIn);
        Date date = null;
        try {
            date = sdfIn.parse(timestamp);
        }
        catch (ParseException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
        }
        String res = timestamp;
        DateFormat sdfOut = new SimpleDateFormat(formatOut);        
        if(date != null){
            res = sdfOut.format(date);
        }
        return res;
    }
}
