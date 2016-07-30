package com.caseykelso.smartsheet;
import com.caseykelso.RESTClient;

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

       + "</script>";
       return cdns;
   }
};
