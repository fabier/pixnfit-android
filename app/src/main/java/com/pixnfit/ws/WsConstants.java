package com.pixnfit.ws;


import com.pixnfit.BuildConfig;

/**
 * Created by fabier on 16/02/16.
 */
public interface WsConstants {

    // Switch permettant de configurer pour la prod ou pour le dev local
    boolean AT_HOME = true;
    String LASERRE = "192.168.78.204";
    String HOME = "192.168.1.128";
    String PROD = "pixnfit.com";

    // Production Configuration
    String HOSTNAME = BuildConfig.DEBUG_MODE ? (AT_HOME ? HOME : LASERRE) : PROD;
    String BASE_URL = BuildConfig.DEBUG_MODE ? "http://" + HOSTNAME + ":8080/pixnfit/api/v1" : "http://" + HOSTNAME + "/api/v1";
}
