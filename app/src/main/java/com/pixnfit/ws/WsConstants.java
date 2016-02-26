package com.pixnfit.ws;

/**
 * Created by fabier on 16/02/16.
 */
public interface WsConstants {

    // Switch permettant de configurer pour la prod ou pour le dev local
    boolean devMode = true;

    // Production Configuration
    String HOSTNAME = devMode ? "192.168.1.128" : "pixnfit.com";
    String BASE_URL = devMode ? "http://" + HOSTNAME + ":8080/pixnfit/api/v1" : "http://" + HOSTNAME + "/api/v1";
}
