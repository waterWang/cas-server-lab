package com.github.water.restlet.v1;

import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.support.WebRequestDataBinder;

import com.github.water.service.util.RestletWebRequest;

public class TicketResource extends ServerResource
{
  private static final Logger LOGGER = LoggerFactory.getLogger(TicketResource.class);

  @Autowired
  private CentralAuthenticationService centralAuthenticationService;

  public TicketResource() { setNegotiated(false); }

  @Post
  public final void acceptRepresentation(Representation entity)
  {
    LOGGER.debug("Obtaining credentials...");
    Credential c = obtainCredentials();

    Formatter fmt = null;
    try {
      String ticketGrantingTicketId = this.centralAuthenticationService.createTicketGrantingTicket(new Credential[] { c });
      getResponse().setStatus(determineStatus());
      Reference ticketReference = getRequest().getResourceRef().addSegment(ticketGrantingTicketId);
      getResponse().setLocationRef(ticketReference);

      fmt = new Formatter();
      fmt.format("<!DOCTYPE HTML PUBLIC \\\"-//IETF//DTD HTML 2.0//EN\\\"><html><head><title>", new Object[0]);

      fmt.format("%s %s", new Object[] { Integer.valueOf(getResponse().getStatus().getCode()), getResponse().getStatus().getDescription() }).format("</title></head><body><h1>TGT Created</h1><form action=\"%s", new Object[] { ticketReference }).format("\" method=\"POST\">Service:<input type=\"text\" name=\"service\" value=\"\">", new Object[0]).format("<br><input type=\"submit\" value=\"Submit\"></form></body></html>", new Object[0]);

      getResponse().setEntity(fmt.toString(), MediaType.TEXT_HTML);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
    } finally {
      IOUtils.closeQuietly(fmt);
    }
  }

  protected Status determineStatus()
  {
    return Status.SUCCESS_CREATED;
  }

  protected Credential obtainCredentials() {
    UsernamePasswordCredential c = new UsernamePasswordCredential();
    WebRequestDataBinder binder = new WebRequestDataBinder(c);
    RestletWebRequest webRequest = new RestletWebRequest(getRequest());

    logFormRequest(new Form(getRequest().getEntity()));
    binder.bind(webRequest);

    return c;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
private void logFormRequest(Form form) {
    if (LOGGER.isDebugEnabled()) {
      Set pairs = new HashSet();
      for (String name : form.getNames()) {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append(": ");
        if (!"password".equalsIgnoreCase(name))
          builder.append(form.getValues(name));
        else {
          builder.append("*****");
        }
        pairs.add(builder.toString());
      }
      LOGGER.debug(StringUtils.join(pairs, ", "));
    }
  }
}