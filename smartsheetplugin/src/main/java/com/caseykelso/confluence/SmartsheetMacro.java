package com.caseykelso.confluence;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.xhtml.api.MacroDefinition;
import com.atlassian.confluence.xhtml.api.MacroDefinitionHandler;
import com.atlassian.confluence.xhtml.api.XhtmlContent;
import com.atlassian.confluence.content.render.xhtml.XhtmlException;
import com.smartsheet.api.*;
import com.smartsheet.api.models.*;
import com.smartsheet.api.models.enums.SourceInclusion;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.oauth.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.DataNode;
import org.jsoup.parser.Tag;

import java.util.EnumSet;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

public class SmartsheetMacro implements Macro 
{


   static private List<Integer> activeColumnIndexes;
   static private int           tagsColumn;

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

   private static Element renderRow(Row smartsheetRow, String tagFilter)
   {
      
      Element  row                = new Element(Tag.valueOf("tr"), "");  
      row.attr("scope", "row");

//      Element  row_number         = new Element(Tag.valueOf("th"), "");
//      row_number.appendText(smartsheetRow.getRowNumber().toString());
/*
      if (null != smartsheetRow.getParentId()) 
      {
        row_number.appendText(","+smartsheetRow.getParentId().toString());
        row_number.attr("class", "level-1");
      }
      else
      {
        row_number.attr("class", "level-0");
      }

      row.appendChild(ronumber);
*/
      try {
      List<Cell> smartsheetCells  = smartsheetRow.getCells();

      int i = 0;

      for (Cell smartsheetCell : smartsheetCells)
      {
          
            // filter row on tag column
            if ((i == tagsColumn) && !tagFilter.equals(""))
            {
               if (null == smartsheetCell.getValue().toString())
               {
                  return null; //TODO: Refactor to remove multiple returns
               }
               else if (-1 == smartsheetCell.getValue().toString().indexOf(tagFilter))
               {
                  return null; //TODO: Refactor to remove multiple returns
               }

           }

         // render active columns
         if (activeColumnIndexes.contains(new Integer(i)))
         {
 
             Element  column    = new Element(Tag.valueOf("td"), "");

	     if (null != smartsheetCell.getValue())
	     {
                 if (smartsheetCell.getValue().toString().equals("Green"))
                 {
                     Element image = new Element(Tag.valueOf("img"), "");
                     image.attr("src", "https://s3.amazonaws.com/caseykelso-smartsheetplugin/green.png");
                     image.attr("width", "25");
                     column.appendChild(image);
                 }
                 else if (smartsheetCell.getValue().toString().equals("Yellow"))
                 {
                     Element image = new Element(Tag.valueOf("img"), "");
                     image.attr("src", "https://s3.amazonaws.com/caseykelso-smartsheetplugin/yellow.png");
                     image.attr("width", "25");
                     column.appendChild(image);
                 }
                 else if (smartsheetCell.getValue().toString().equals("Red"))
                 { 
                     Element image = new Element(Tag.valueOf("img"), "");
                     image.attr("src", "https://s3.amazonaws.com/caseykelso-smartsheetplugin/red.png");
                     image.attr("width", "25");
                     column.appendChild(image);
                 }  
                 else  
                 {
		     column.appendText(smartsheetCell.getValue().toString());
                 }
	     }
	     else
	     {
	       column.html("&nbsp;");
	     }


      	    row.appendChild(column);       // add column to row
      }
           i++;
 
      } 
      }
      catch(Exception e)
      {
          row = null;
      }
 
      return row; 
   }

   private static Element renderTableHeader(Sheet s, List<String> activeColumnNames)
   {

      activeColumnIndexes = new ArrayList<Integer>();

      Element tableHeader = new Element(Tag.valueOf("thead"), "");
      Element row         = new Element(Tag.valueOf("tr"), "");
      
      List<Column> columns = s.getColumns();
/*         
      Element rownumberColumn = new Element(Tag.valueOf("th"), "");
      rownumberColumn.appendText("Row Number");
      tableHeader.appendChild(rownumberColumn);    
*/
      tagsColumn = 0;
      int i      = 0;
 
      for (Column c : columns)
      {
         if (c.getTitle().equals("Tags"))
         {
            tagsColumn = i;
         } 
         
         if ((activeColumnNames.contains(c.getTitle().toString())))
         {
		 Element column = new Element(Tag.valueOf("th"), "");
		 column.appendText(c.getTitle().toString());
		 tableHeader.appendChild(column);
                 activeColumnIndexes.add(new Integer(i));
         }

         ++i;
      }

      tableHeader.appendChild(row);
      return tableHeader;
   }

   private static Element renderTable(Sheet s, String tagFilter, List<String> activeColumnNames)
   {
      Element  table     = new Element(Tag.valueOf("table"), ""); 
      table.attr("class", "table");
      table.appendChild(renderTableHeader(s, activeColumnNames));

      List<Row> rows     = s.getRows(); 

      for (Row r : rows)
      {
          Element e = renderRow(r,tagFilter);

          if (null != e)
          { 
	      table.appendChild(e);
          }
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
       + "<script   src=\"https://code.jquery.com/jquery-2.2.4.min.js\"   integrity=\"sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=\"   crossorigin=\"anonymous\"></script>"
       + "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\">"
       + ".level-1 > th { padding-left: 1em; }"
       + "</script>";
      
       return cdns;
   }
  
   
   public static String renderSheetHTML(Sheet s, String tagFilter, List<String> activeColumnNames)
   {
        Document doc       = Document.createShell("");
        
        DataNode script    = new DataNode("script", "");
        script.setWholeData(renderCDNs());
        doc.head().appendChild(script);

        Element  headline  = doc.body().appendElement("h1").text(s.getName());
        
        doc.body().appendChild(renderTable(s, tagFilter, activeColumnNames)); // add table to html body

        return doc.html();
   }

	public SmartsheetMacro() 
        {
		// TODO Auto-generated constructor stub
	}


        @Override
        public String execute(Map<String, String> parameters, String bodyContent, ConversionContext conversionContext) throws MacroExecutionException
        {
                final List<MacroDefinition> macros = new ArrayList<MacroDefinition>();
                String body = conversionContext.getEntity().getBodyAsString();

                List<String> activeColumnNames = new ArrayList<String>();

/* this isn't scalable. Switch from bools to a list of String names, this will be more flexible */
                 if (parameters.containsKey("tags-column") && parameters.get("tags-column").equals("true"))
                 {
		     activeColumnNames.add("Tags");
                 }

                 if (parameters.containsKey("status-column") && parameters.get("status-column").equals("true"))
                 {
		     activeColumnNames.add("Status");
                 }

                 if (parameters.containsKey("task-column") && parameters.get("task-column").equals("true"))
                 {
		     activeColumnNames.add("Task Name");
                 }

                 if (parameters.containsKey("ryg-column") && parameters.get("ryg-column").equals("true"))
                 {
		     activeColumnNames.add("RYG");
                 }

                 if (parameters.containsKey("comments-column") && parameters.get("comments-column").equals("true"))
                 {
		     activeColumnNames.add("Comments");
                 }

                 if (parameters.containsKey("start-column") && parameters.get("start-column").equals("true"))
                 {
		     activeColumnNames.add("Start");
                 }

                 if (parameters.containsKey("finish-column") && parameters.get("finish-column").equals("true"))
                 {
		     activeColumnNames.add("Finish");
                 }

                 if (parameters.containsKey("assigned-column") && parameters.get("assigned-column").equals("true"))
                 {
		     activeColumnNames.add("Assigned To");
                 }

                String html = "";

		try 
		{ 
                   String sheetID   = parameters.get("sheet-id");
                   String tagFilter = "";
  
                   if (parameters.containsKey("tag-filter"))
                   {
                       tagFilter = parameters.get("tag-filter");
                   }

		   html = renderSheetHTML(this.getSheet(parameters.get("api-token"), Long.valueOf(sheetID)), tagFilter, activeColumnNames);
		}
		catch (Exception e)
		{
		   System.err.println("SmartSheetException: " + e.getMessage()); 
		} 
              return html;

        }


	@Override
	public BodyType getBodyType() 
        {
                return BodyType.NONE;
	}

	@Override
	public OutputType getOutputType() 
        {
                return OutputType.BLOCK;
	}
}

