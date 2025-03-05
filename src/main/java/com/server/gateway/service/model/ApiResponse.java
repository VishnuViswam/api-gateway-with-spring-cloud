package com.server.gateway.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse(Short code,String message,Object data) {
}
