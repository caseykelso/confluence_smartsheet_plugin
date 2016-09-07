package io.mutex.smartsheet;
import io.mutex.RESTClient;

public class RESTClientModdedCDN extends RESTClient
{
   @Override
   public String renderCDNs()
   {
       String cdns = ""
       + "<!-- Latest compiled and minified CSS -->"
       + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">"
       + "<!-- Optional theme -->"
       + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\" integrity=\"sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r\" crossorigin=\"anonymous\">"
       + "<link rel=\"stylesheet\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqx.base.css\" >"

       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxcore.js\" ></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxdata.js\" ></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxbuttons.js\" ></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxscrollbar.js\" ></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxmenu.js\" ></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxgrid.js\" ></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxgrid.selection.js\" ></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxgrid.filter.js\" ></script>"
       + "<script type=\"text/javacript\" href=\"https://s3.amazonaws.com/caseykelso-smartsheetplugin/jqwidgets/jqxgrid.sort.js\" ></script>";
       return cdns;
   }
};



