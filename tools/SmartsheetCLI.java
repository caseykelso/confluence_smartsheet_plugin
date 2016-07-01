import com.smartsheet.api.*;
import com.smartsheet.api.models.*;
import com.smartsheet.api.models.enums.SourceInclusion;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.oauth.*;
import java.util.EnumSet;
import java.util.List;
import java.util.Arrays;
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
import org.jsoup.parser.Tag;




public class SmartsheetCLI 
{
    public static PagedResult<Sheet> getSheets(String smartsheetToken) throws SmartsheetException
    {
        // Set the Access Token
        Token token = new Token();
        token.setAccessToken(smartsheetToken);

        // Use the Smartsheet Builder to create a Smartsheet
        Smartsheet smartsheet = new SmartsheetBuilder().setAccessToken(token.getAccessToken()).build();

        // Get home with Source Inclusion parameter
        Home home = smartsheet.homeResources().getHome(EnumSet.of(SourceInclusion.SOURCE));

        // List home folders
        List<Folder> homeFolders = home.getFolders();

        for(Folder folder : homeFolders)
        {
            System.out.println("folder:"+folder.getName());
        }

        //List Sheets with Source Inclusion parameters and null Pagination parameters
        PagedResult<Sheet> homeSheets = smartsheet.sheetResources().listSheets(EnumSet.of(SourceInclusion.SOURCE), null);

        return homeSheets;

   }

   public static void renderSheets(PagedResult<Sheet> homeSheets)
   {
        for(Sheet sheet : homeSheets.getData())
        {
            System.out.println("sheet: " + sheet.getName());
        }
   }

   public static Sheet getSheet(String smartsheetToken, long sheetID) throws SmartsheetException
   {
        // Set the Access Token
        Token token = new Token();
        token.setAccessToken(smartsheetToken);

        // Use the Smartsheet Builder to create a Smartsheet
        Smartsheet smartsheet = new SmartsheetBuilder().setAccessToken(token.getAccessToken()).build();

        // Get home with Source Inclusion parameter
        Home home = smartsheet.homeResources().getHome(EnumSet.of(SourceInclusion.SOURCE));

        return smartsheet.sheetResources().getSheet(sheetID, null, null, null, null, null, null, null);
       
   }

   public static void renderSheet(Sheet s)
   {
        if (null != s)
        {
           System.out.println("sheet name: " + s.getName());
        }

   }

   private static Element renderRow(Row smartsheetRow)
   {
      
      Element  row                = new Element(Tag.valueOf("tr"), "");  
      row.attr("scope", "row");
      Element  row_number         = new Element(Tag.valueOf("th"), "");
//      row_number.appendText(smartsheetRow.
      try {
      List<Cell> smartsheetCells  = smartsheetRow.getCells();

      for (Cell smartsheetCell : smartsheetCells)
      {
         Element  column    = new Element(Tag.valueOf("td"), "");
         column.appendText(smartsheetCell.getValue().toString());
         row.appendChild(column);       // add column to row
      }
      
      }
      catch(Exception e)
      {
//          System.err.println("renderRow Exception: " + e.getMessage());
      }
 
      return row; 
   }

   private static Element renderTable(Sheet s)
   {
      Element  table     = new Element(Tag.valueOf("table"), ""); 
      table.attr("class", "table");
      List<Row> rows     = s.getRows(); 

      for (Row r : rows)
      {
          table.appendChild(renderRow(r));
      }

      return table;
   }

   public static String renderCDNs()
   {
       String cdns = ""
       + "<!-- Latest compiled and minified CSS -->"
       + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">"
       + "<!-- Optional theme -->"
       + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\" integrity=\"sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r\" crossorigin=\"anonymous\">"
       + "<!-- Latest compiled and minified JavaScript -->"
       + "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\"></script>\");";
      
       return cdns;
   }
 
   public static String renderSheetHTML(Sheet s)
   {
        Document doc       = Document.createShell("");
        Element  headline  = doc.body().appendElement("h1").text(s.getName());

        doc.body().appendChild(renderTable(s)); // add table to html body

        return doc.html();
   }

   public static Options setupCommandLine()
   {
       Options options = new Options();
       options.addOption("h", "help", false, "show help");  
       options.addOption("as", "allsheets", false, "show sheets");
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
        Properties props           = null;
        Options    options         = null;
        CommandLine cl             = null;

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

        if (cl.hasOption("allsheets"))
        {   
		try 
		{ 
		   renderSheets(SmartsheetCLI.getSheets(props.getProperty("apitoken")));
		}
		catch (Exception e)
		{
		   System.err.println("SmartSheetException: " + e.getMessage()); 
		   System.exit(2);
		}
        } 
        else if (cl.hasOption("sheet") && (null != cl.getOptionValue("sheet")))
        {   
		try 
		{ 
                   String html = "";
		   html = renderSheetHTML(SmartsheetCLI.getSheet(props.getProperty("apitoken"), Long.valueOf(cl.getOptionValue("sheet"))));
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

