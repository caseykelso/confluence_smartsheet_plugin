package com.caseykelso;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class RESTClient
{


   private List<Integer> activeColumnIndexes; //TODO: refactor, use a class instance per macro execute
   private int           tagsColumn; //TODO: same as above

   public Sheet getSheet(String smartsheetToken, long sheetID) throws SmartsheetException
   {
        // Set the Access Token
        Token token = new Token();
        token.setAccessToken(smartsheetToken);

        // Use the Smartsheet Builder to create a Smartsheet
        Smartsheet smartsheet = new SmartsheetBuilder().setAccessToken(token.getAccessToken()).build();

        // Get home with Source Inclusion parameter
        Home home = smartsheet.homeResources().getHome(EnumSet.of(SourceInclusion.SOURCE));

        return smartsheet.sheetResources().getSheet(sheetID, null, null, null, null, null, null, null);

//          return smartsheet.sheetResources().getSheet(sheetID, 
//TODO: http://stackoverflow.com/questions/35846003/cell-columntype-is-null-using-smartsheet-api?rq=1
//TODO: use jackson to serialize java objects to JSON: http://www.mkyong.com/java/jackson-2-convert-java-object-to-from-json/       
   }

   public void renderSheet(Sheet s)
   {
        if (null != s)
        {
           System.out.println("sheet name: " + s.getName());
        }

   }

   private String renderDate(Element e)
   {
       return "placeholder";
   }

//TODO: migrate to jqxTreeGrid for hierarchical grid
//http://www.jqwidgets.com/jquery-widgets-demo/mobiledemos/jqxtreegrid/index.htm#demos/jqxtreegrid/treegrid.htm
//free for opensource
   private Element renderRow(Row smartsheetRow, String tagFilter,  List<String> activeColumnNames)
   {
      
      Element  row                = new Element(Tag.valueOf("tr"), "");  
      row.attr("scope", "row");

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
         if (activeColumnNames.contains("all") || activeColumnIndexes.contains(new Integer(i)))
         {
             Element  column    = new Element(Tag.valueOf("td"), "");

	     if (null != smartsheetCell.getValue())
	     {
                 if (smartsheetCell.getValue().toString().equals("Green"))
                 {
                     Element link = new Element(Tag.valueOf("a"), "");
try {
//                     link.attr("href", smartsheetRow.getPermalink());
}
catch (Exception e)
{
    e.printStackTrace();
	
}
                     Element image = new Element(Tag.valueOf("img"), "");
                     image.attr("src", "https://s3.amazonaws.com/caseykelso-smartsheetplugin/green.png");
                     image.attr("width", "25");
                     column.appendChild(image);
//                     column.appendChild(link);
//TODO: render link from confluence back to the smartsheet row
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
                 else if (ColumnType.CHECKBOX              == smartsheetCell.getColumnType() || 
                          ColumnType.PICKLIST              == smartsheetCell.getColumnType() || 
                          ColumnType.TEXT_NUMBER           == smartsheetCell.getColumnType() || 
                          ColumnType.PREDECESSOR           == smartsheetCell.getColumnType() || 
                          ColumnType.CONTACT_LIST          == smartsheetCell.getColumnType() || 
                          ColumnType.DURATION              == smartsheetCell.getColumnType() )
                 {
                 }
 
                 else if (ColumnType.DATE              == smartsheetCell.getColumnType() || 
                          ColumnType.DATETIME          == smartsheetCell.getColumnType() || 
                          ColumnType.ABSTRACT_DATETIME == smartsheetCell.getColumnType() )
                 {

                        try
                        {

		            SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-DDTHH:mm:ss");
		            Date date = sourceDateFormat.parse(smartsheetCell.getValue().toString());
			    SimpleDateFormat targetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
   			    System.out.println(targetDateFormat.format(date));
   	                }
                        catch(ParseException e) 
                        {
			    e.printStackTrace();
			}
			   
                 }
                 else 
                 {
                     try 
                     {
		        column.appendText(smartsheetCell.getDisplayValue());
                     }
                     catch (Exception e)
                     {
        	        column.appendText(smartsheetCell.getValue().toString());
                     }

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



   private Element renderTableHeader(Sheet s, List<String> activeColumnNames)
   {

      activeColumnIndexes = new ArrayList<Integer>();

      Element tableHeader = new Element(Tag.valueOf("thead"), "");
      Element row         = new Element(Tag.valueOf("tr"), "");
      
      List<Column> columns = s.getColumns();

      tagsColumn = 0;
      int i      = 0;
 
      for (Column c : columns)
      {
         if (c.getTitle().equals("Tags"))
         {
            tagsColumn = i;
         } 
         
         if (activeColumnNames.contains("all") || (activeColumnNames.contains(c.getTitle().toString())))
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

   private Element renderTable(Sheet s, String tagFilter, List<String> activeColumnNames)
   {
       return renderTable(s, tagFilter, activeColumnNames, true);
   }
 
   private Element renderTable(Sheet s, String tagFilter, List<String> activeColumnNames, boolean isHideColumnHeaders)
   {
      Element  table     = new Element(Tag.valueOf("table"), ""); 
      table.attr("class", "table table-striped table-hover");
      
      if (!isHideColumnHeaders)
      {
         table.appendChild(renderTableHeader(s, activeColumnNames));
      }

      List<Row> rows     = s.getRows(); 

      for (Row r : rows)
      {
          Element e = renderRow(r,tagFilter, activeColumnNames);

          if (null != e)
          { 
	      table.appendChild(e);
          }
      }

      return table;
   }

   public String renderCDNs()
   {
       String cdns = ""
       + "<!-- Latest compiled and minified CSS -->"
       + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">"
       + "<!-- Optional theme -->"
       + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\" integrity=\"sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r\" crossorigin=\"anonymous\">"
       + "</script>";
       return cdns;
   }
  
   public String renderSheetHTML(Sheet s, String tagFilter, List<String> activeColumnNames)
   {  
       return renderSheetHTML(s, tagFilter, activeColumnNames, true);
   }
   
   public String renderSheetHTML(Sheet s, String tagFilter, List<String> activeColumnNames, boolean isHideColumnHeaders)
   {
        Document doc       = Document.createShell("");
        
        DataNode script    = new DataNode("script", "");
        script.setWholeData(renderCDNs());
        doc.empty();
        doc.appendChild(script);

//        Element  headline  = doc.appendElement("h1").text(s.getName());
        
        doc.appendChild(renderTable(s, tagFilter, activeColumnNames, isHideColumnHeaders)); // add table to html body

        return doc.html();
   }

	public RESTClient() 
        {
		// TODO Auto-generated constructor stub
	}


}

