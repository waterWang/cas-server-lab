package org.jasig.cas.support.rest;

import javax.validation.constraints.NotNull;
import org.jasig.cas.authentication.Credential;
import org.springframework.util.MultiValueMap;

public abstract interface CredentialFactory
{
  public abstract Credential fromRequestBody(@NotNull MultiValueMap<String, String> paramMultiValueMap);
}