package edu.muniz.universeguide.rest;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

public abstract class Rest {

	@Context
    private HttpServletResponse servletResponse;

   protected void allowCrossDomainAccess() {
       if (servletResponse != null){
           servletResponse.setHeader("Access-Control-Allow-Origin", "*");
           servletResponse.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE");
           servletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type");
       }
   }
}
