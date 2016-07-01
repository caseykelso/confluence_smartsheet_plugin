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

public class SmartsheetMacro implements Macro 
{

/*
	public SmartsheetMacro() 
        {
		// TODO Auto-generated constructor stub
	}
*/

        private final XhtmlContent xhtmlUtils;

	public SmartsheetMacro(XhtmlContent xhtmlUtils) 
	{
		this.xhtmlUtils = xhtmlUtils;	
	}

        @Override
        public String execute(Map<String, String> parameters, String bodyContent, ConversionContext conversionContext) throws MacroExecutionException
        {
                final List<MacroDefinition> macros = new ArrayList<MacroDefinition>();
                String body = conversionContext.getEntity().getBodyAsString();

		try
		{
		    xhtmlUtils.handleMacroDefinitions(body, conversionContext, new MacroDefinitionHandler()
		    {
			@Override
			public void handle(MacroDefinition macroDefinition)
			{
			    macros.add(macroDefinition);
			}
		    });
		}
		catch (XhtmlException e)
		{
		    throw new MacroExecutionException(e);
		}

              return "SmartsheetMacro Placeholder";
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

