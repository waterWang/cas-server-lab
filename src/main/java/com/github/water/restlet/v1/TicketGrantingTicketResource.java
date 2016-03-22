package com.github.water.restlet.v1;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.SimpleWebApplicationServiceImpl;
import org.jasig.cas.ticket.InvalidTicketException;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Delete;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public final class TicketGrantingTicketResource extends ServerResource {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TicketGrantingTicketResource.class);

	@Autowired
	private CentralAuthenticationService centralAuthenticationService;
	private String ticketGrantingTicketId;

	public void init(Context context, Request request, Response response) {
		super.init(context, request, response);
		this.ticketGrantingTicketId = ((String) request.getAttributes().get(
				"ticketGrantingTicketId"));
		setNegotiated(false);
		getVariants().add(new Variant(MediaType.APPLICATION_WWW_FORM));
	}

	@Delete
	public void removeRepresentations() {
		this.centralAuthenticationService
				.destroyTicketGrantingTicket(this.ticketGrantingTicketId);
		getResponse().setStatus(Status.SUCCESS_OK);
	}

	@Post
	public void acceptRepresentation(Representation entity) {
		Form form = new Form(entity);
		String serviceUrl = form.getFirstValue("service");
		try {
			String serviceTicketId = this.centralAuthenticationService
					.grantServiceTicket(this.ticketGrantingTicketId,
							new SimpleWebApplicationServiceImpl(serviceUrl));

			getResponse().setEntity(serviceTicketId, MediaType.TEXT_PLAIN);
		} catch (InvalidTicketException e) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND,
					"TicketGrantingTicket could not be found.");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST,
					e.getMessage());
		}
	}
}