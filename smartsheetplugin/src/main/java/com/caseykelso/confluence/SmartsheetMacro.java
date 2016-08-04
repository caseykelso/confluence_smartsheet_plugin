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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.caseykelso.RESTClient;



public class SmartsheetMacro implements Macro 
{

	private List<Integer> activeColumnIndexes; //TODO: refactor, use a class instance per macro execute
        private com.caseykelso.RESTClient client;

	public SmartsheetMacro() 
        {

	}


        @Override
        public OutputType getOutputType()
        {
             return OutputType.BLOCK;
        }

	@Override
	public BodyType getBodyType() 
        {
                return BodyType.NONE;
	}

        @Override
        public String execute(Map<String, String> parameters, String bodyContent, ConversionContext conversionContext) throws MacroExecutionException
        {
                final List<MacroDefinition> macros              = new ArrayList<MacroDefinition>();
                String                      body                = conversionContext.getEntity().getBodyAsString();
                List<String>                activeColumnNames   = new ArrayList<String>();
                boolean                     isHideColumnHeaders = false;
                String                      html                = "";
                client                                          = new RESTClient();


/* this isn't scalable. Switch from bools to a list of String names, this will be more flexible */
                 if (parameters.containsKey("tags-column") && parameters.get("tags-column").equals("true"))
                 {
		     activeColumnNames.add("Tags");
                     html += ("************tags-column");
                 }

                 if (parameters.containsKey("status-column") && parameters.get("status-column").equals("true"))
                 {
		     activeColumnNames.add("Status");
                     html += ("************status-column");
 
                 }

                 if (parameters.containsKey("task-column") && parameters.get("task-column").equals("true"))
                 {
		     activeColumnNames.add("Task Name");
                     html += ("************task-column");
 
                 }

                 if (parameters.containsKey("ryg-column") && parameters.get("ryg-column").equals("true"))
                 {
		     activeColumnNames.add("RYG");
                     html += ("************ryg-column");
 
                 }

                 if (parameters.containsKey("comments-column") && parameters.get("comments-column").equals("true"))
                 {
		     activeColumnNames.add("Comments");
                     html += ("************comments-column");
 
                 }

                 if (parameters.containsKey("start-column") && parameters.get("start-column").equals("true"))
                 {
		     activeColumnNames.add("Start");
                     html += ("************start-column");
                 }

                 if (parameters.containsKey("finish-column") && parameters.get("finish-column").equals("true"))
                 {
		     activeColumnNames.add("Finish");
                     html += ("************finish-column");
                 }

                 if (parameters.containsKey("assigned-column") && parameters.get("assigned-column").equals("true"))
                 {
                     html += ("************assigned-column");
		     activeColumnNames.add("Assigned To");
                 }

                 if (parameters.containsKey("all-column") && parameters.get("all-column").equals("true"))
                 {
		     activeColumnNames.add("all");
                     html += ("************all-columns");
 
                 }


                 if (parameters.containsKey("hide-headers") && parameters.get("hide-headers").equals("true"))
                 {
                     isHideColumnHeaders = true;
                     html += ("************hide-headers");
                 }


		try 
		{ 
                   String sheetID   = parameters.get("sheet-id");
                   String tagFilter = "";
  
                   if (parameters.containsKey("tag-filter"))
                   {
                       tagFilter = parameters.get("tag-filter");
                       html += ("************tag-filter: "+ tagFilter);
                   }

		   html += client.renderSheetHTML(client.getSheet(parameters.get("api-token"), Long.valueOf(sheetID)), tagFilter, activeColumnNames, isHideColumnHeaders);
		}
		catch (Exception e)
		{
		   System.err.println("SmartSheetException: " + e.getMessage()); 
                   html += ("SmartSheetException: " + e.getMessage());
		} 

                return html;

        }

}

