package com.pixnfit.ws;

/**
 * Created by fabier on 16/02/16.
 */
public interface WsConstants {

    // Switch permettant de configurer pour la prod ou pour le dev local
    boolean DEV_MODE = false;

    // Production Configuration
    String HOSTNAME = DEV_MODE ? "192.168.1.128" : "pixnfit.com";
    String BASE_URL = DEV_MODE ? "http://" + HOSTNAME + ":8080/pixnfit/api/v1" : "http://" + HOSTNAME + "/api/v1";
}
