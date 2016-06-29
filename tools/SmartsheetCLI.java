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
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.cli.Options;  //apache commons cli examples https://commons.apache.org/proper/commons-cli/usage.html

public class SmartsheetCLI 
{
    public static void getSheet(String smartsheetToken) throws SmartsheetException
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
        for(Folder folder : homeFolders){
            System.out.println("folder:"+folder.getName());
        }

        //List Sheets with Source Inclusion parameters and null Pagination parameters
        PagedResult<Sheet> homeSheets = smartsheet.sheetResources().listSheets(EnumSet.of(SourceInclusion.SOURCE), null);
        for(Sheet sheet : homeSheets.getData()){
            System.out.println("sheet: " + sheet.getName());
        }

   }

   public static Options setupCommandLine()
   {
       Options options = new Options();
       options.addOption("h", false, "show help");  
       return options;
   }
   
   public static Properties getProperties() throws IOException
   {

       Properties prop   = new Properties();
       InputStream input = new FileInputStream("smartsheet.properties");
       prop.load(input);

       return prop; 
   }

    public static void main(String args[])
    {
        Properties props   = null;
        Options    options = null;

        options = setupCommandLine();

        try
        {
           props = getProperties();
        }
        catch (IOException e)
        { 
          System.err.println("getProperties Exception: " + e.getMessage());
          System.exit(1);
        }        
   
        try 
        { 
           SmartsheetCLI.getSheet(props.getProperty("apitoken")); 
        }
        catch (Exception e)
        {
           System.err.println("SmartSheetException: " + e.getMessage()); 
           System.exit(2);
        }
    }

};

