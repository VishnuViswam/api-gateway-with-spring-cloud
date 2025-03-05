package com.server.gateway.service.util;

public class Constants {

    /*--------- JWT Keys : starts here ---------*/

    public static final String BEARER_KEY = "Bearer";
    public static final String AUTHENTICATION_HEADER_KEY = "Authorization";

    /*--------- JWT Keys : ends here ---------*/


    /*--------- Common Success codes : starts here ---------*/
    public static final String SUCCESS_MESSAGE = "Success";
    public static final short SUCCESS_CODE = 10;
    /*--------- Common Success codes : ends here ---------*/


    /*--------- Common failure codes : starts here ---------*/
    // General failure codes
    public static final short UNKNOWN_ERROR_CODE = -10;
    public static final String UNKNOWN_ERROR_MESSAGE = "GW-Internal Service Fault";

    public static final short API_HEADER_INVALID_VALUE_CODE = -11;
    public static final String API_HEADER_INVALID_VALUE_MESSAGE = "GW - Invalid value present in API header";

    /*--------- Common failure codes : ends here ---------*/


}
