package io.mutex;

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

   private String extractGanttRow(Row smartsheetRow, String tagFilter)
   {
     
      String row = ""; 

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
         if (true) //activeColumnNames.contains("all") || activeColumnIndexes.contains(new Integer(i)))
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
                     image.attr("src", "https://s3.amazonaws.com/caseykelso-smartsheetplugin/green_rev2.png");
                     image.attr("width", "25");
                     column.appendChild(image);
//                     column.appendChild(link);
//TODO: render link from confluence back to the smartsheet row
                 }
                 else if (smartsheetCell.getValue().toString().equals("Yellow"))
                 {
                     Element image = new Element(Tag.valueOf("img"), "");
                     image.attr("src", "https://s3.amazonaws.com/caseykelso-smartsheetplugin/yellow_rev2.png");
                     image.attr("width", "25");
                     column.appendChild(image);
                 }
                 else if (smartsheetCell.getValue().toString().equals("Red"))
                 { 
                     Element image = new Element(Tag.valueOf("img"), "");
                     image.attr("src", "https://s3.amazonaws.com/caseykelso-smartsheetplugin/red_rev2.png");
                     image.attr("width", "25");
                     column.appendChild(image);
                 } 
                 else if (smartsheetCell.getValue().toString().equals("Blue"))
                 { 
                     Element image = new Element(Tag.valueOf("img"), "");
                     image.attr("src", "https://s3.amazonaws.com/caseykelso-smartsheetplugin/blue_rev2.png");
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
                    row += " duration:"+smartsheetCell.getValue().toString();
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


      	    //row.appendChild(column);       // add column to row
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
                     image.attr("src", "https://s3.amazonaws.com/caseykelso-smartsheetplugin/green_rev2.png");
                     image.attr("width", "25");
                     column.appendChild(image);
//                     column.appendChild(link);
//TODO: render link from confluence back to the smartsheet row
                 }
                 else if (smartsheetCell.getValue().toString().equals("Yellow"))
                 {
                     Element image = new Element(Tag.valueOf("img"), "");
                     image.attr("src", "https://s3.amazonaws.com/caseykelso-smartsheetplugin/yellow_rev2.png");
                     image.attr("width", "25");
                     column.appendChild(image);
                 }
                 else if (smartsheetCell.getValue().toString().equals("Red"))
                 { 
                     Element image = new Element(Tag.valueOf("img"), "");
                     image.attr("src", "https://s3.amazonaws.com/caseykelso-smartsheetplugin/red_rev2.png");
                     image.attr("width", "25");
                     column.appendChild(image);
                 } 
                 else if (smartsheetCell.getValue().toString().equals("Blue"))
                 { 
                     Element image = new Element(Tag.valueOf("img"), "");
                     image.attr("src", "https://s3.amazonaws.com/caseykelso-smartsheetplugin/blue_rev2.png");
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



   private Element renderTableHeader(Sheet s, List<String> activeColumnNames, boolean isHideColumnHeaders)
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

                 if (!isHideColumnHeaders)
                 {
			 tableHeader.appendChild(column);
                 }

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
      
      table.appendChild(renderTableHeader(s, activeColumnNames, isHideColumnHeaders));

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

   public String renderGanttRow(Row r, int rowID, String tagFilter)
   {
     String result = "";

     if (1 != rowID) // if not first row, add a comma delimeter to separate from the previous row
     {
        result += ",";
     }

     String extracted = extractGanttRow(r, tagFilter);

     result += "{id:"+rowID+", text:\"blah\", start_date:\"01-04-2013\", "+extracted+", order:10, progress:0.5, open: true}\n";
     
/*
                {id:1, text:"Project #2", start_date:"01-04-2013", duration:18,order:10,
                    progress:0.4, open: true},
                {id:2, text:"Task #1", 	  start_date:"02-04-2013", duration:8, order:10,
                    progress:0.6, parent:1},
                {id:3, text:"Task #2",    start_date:"11-04-2013", duration:8, order:20,
                    progress:0.6, parent:1}
*/
     return result;
   }

   public String renderGantt(Sheet s, String tagFilter)
   {

      String html = "";

        html += "<body>\n";
        html += "<div id=\"gantt_here\" style='width:100%; height:100%;'></div>\n";

        // start of script
	html += "<script type=\"text/javascript\">\n";

        // start of tasks json container
        html += "var tasks =  {"
        + "    data:[\n";

      List<Row> rows = s.getRows();

      int rowID = 1;

      for (Row r : rows)
      {
         html += renderGanttRow(r, rowID, tagFilter);  
         ++rowID;
      }

       // end of tasks json container
       html += "     ]\n"
       + "};\n";

      html += "$(function() {";
      html += "console.log( \"ready!\" );";
      html += "gantt.init(\"gantt_here\");\n";
      html += "gantt.parse(tasks);\n"; 
      html += "});";

      //end of script tag
      html += "</script>\n";
  
      html += "</body>";

      return html;
   }

   public String renderGanttCDNs()
   { 
      String cdns = ""
       + "<script   src=\"https://code.jquery.com/jquery-2.2.4.min.js\"   integrity=\"sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=\"   crossorigin=\"anonymous\"></script>\n"
       + "<!-- Latest compiled and minified CSS -->\n"
      + "<link rel=\"stylesheet\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/dhtmlxgantt4.0.0/dhtmlxgantt.css\" type=\"text/css\" media=\"screen\" title=\"no title\" charset=\"utf-8\" >\n"
      + "<script charset=\"utf-8\" type=\"text/javascript\" src=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/dhtmlxgantt4.0.0/dhtmlxgantt.js\" >\n</script>\n"
      + "<style type=\"text/css\">\n"
      + "html, body{ height:100%; padding:0px; margin:0px; overflow: hidden;}\n"
      + "</style>\n";

      return cdns;
   }

   public String renderCDNs()
   {
       String cdns = "<head>"
       + "<!-- Latest compiled and minified CSS -->"
       + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">\n"
       + "<!-- Optional theme -->\n"
       + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\" integrity=\"sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r\" crossorigin=\"anonymous\">\n"
       + "<link rel=\"stylesheet\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqx.base.css\" crossorigin=\"anonymous\">\n"

       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxcore.js\" crossorigin=\"anonymous\"></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxdata.js\" crossorigin=\"anonymous\"></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxbuttons.js\" crossorigin=\"anonymous\"></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxscrollbar.js\" crossorigin=\"anonymous\"></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxmenu.js\" crossorigin=\"anonymous\"></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxgrid.js\" crossorigin=\"anonymous\"></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxgrid.selection.js\" crossorigin=\"anonymous\"></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxgrid.filter.js\" crossorigin=\"anonymous\"></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxgrid.sort.js\" crossorigin=\"anonymous\"></script></head>";
       return cdns;
   }


   public String renderSheetGanttHTML(Sheet s, String tagFilter)
   {
       String html = "<html>";
       html += renderGanttCDNs();
       html += renderGantt(s, tagFilter);
       html += "</html>";

       return html;
   }
  
   public String renderSheetTableHTML(Sheet s, String tagFilter, List<String> activeColumnNames)
   {  
       return renderSheetTableHTML(s, tagFilter, activeColumnNames, true);
   }
   
   public String renderSheetTableHTML(Sheet s, String tagFilter, List<String> activeColumnNames, boolean isHideColumnHeaders)
   {
        Document doc       = Document.createShell("");
        
        DataNode script    = new DataNode("script", "");
        script.setWholeData(renderCDNs());
        doc.empty();
        doc.appendChild(script);
//System.out.println("*****************renderSheetHTML");

//        Element  headline  = doc.appendElement("h1").text(s.getName());
        
        doc.appendChild(renderTable(s, tagFilter, activeColumnNames, isHideColumnHeaders)); // add table to html body

        return doc.html();
   }

	public RESTClient() 
        {
		// TODO Auto-generated constructor stub
	}


}

