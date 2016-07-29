package com.caseykelso.smartsheet;
import com.smartsheet.api.*;
import com.smartsheet.api.models.*;
import com.smartsheet.api.models.enums.SourceInclusion;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.oauth.*;
import java.util.EnumSet;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Properties;
import org.apache.commons.cli.Options;  //apache commons cli examples https://commons.apache.org/proper/commons-cli/usage.html
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.DataNode;
import org.jsoup.parser.Tag;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.caseykelso.smartsheet.RESTClientModdedCDN;


public class SmartsheetCLI
{



   public static Options setupCommandLine()
   {
       Options options = new Options();
       options.addOption("h", "help", false, "show help");  
//       options.addOption("as", "allsheets", false, "show sheets");
       options.addOption("s", "sheet", true, "show sheet");
       return options;
   }

   
   public static Properties getProperties() throws IOException
   {

       Properties prop   = new Properties();
       InputStream input = new FileInputStream("smartsheet.properties");
       prop.load(input);

       return prop; 
   }

   public static void showHelp(Options options)
   {
       HelpFormatter formatter = new HelpFormatter();
       formatter.printHelp( "target", options);
   }

    public static void main(String args[])
    {
        Properties          props           = null;
        Options             options         = null;
        CommandLine         cl              = null;
        RESTClientModdedCDN client          = new RESTClientModdedCDN();

        options = setupCommandLine();
        CommandLineParser clparser = new DefaultParser();
        try
        {
            cl = clparser.parse(options, args);
        }
        catch (ParseException e)
        {
            SmartsheetCLI.showHelp(options);
            System.exit(3); // change this to show help
        }

        try
        {
           props = getProperties();
        }
        catch (IOException e)
        { 
          System.err.println("getProperties Exception: " + e.getMessage());
          System.exit(1);
        }        

/*        if (cl.hasOption("allsheets"))
        {   
		try 
		{ 
		   client.renderSheets(client.getSheets(props.getProperty("apitoken")));
		}
		catch (Exception e)
		{
		   System.err.println("SmartSheetException: " + e.getMessage()); 
		   System.exit(2);
		}
        }  */
        if (cl.hasOption("sheet") && (null != cl.getOptionValue("sheet")))
        {   
		try 
		{ 
                   String html = "";
                   List<String> columns = new ArrayList<String>();
                   columns.add("all");
		   html = client.renderSheetHTML(client.getSheet(props.getProperty("apitoken"), Long.valueOf(cl.getOptionValue("sheet"))),"",columns);
                   System.out.println(html);
		}
		catch (Exception e)
		{
		   System.err.println("SmartSheetException: " + e.getMessage()); 
		   System.exit(2);
		}
        } 
 
        else if (cl.hasOption("help"))
        {
            SmartsheetCLI.showHelp(options);
        }


    }

};

