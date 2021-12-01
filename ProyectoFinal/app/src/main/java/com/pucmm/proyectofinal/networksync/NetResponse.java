package com.pucmm.proyectofinal.networksync;

import java.io.FileNotFoundException;

public interface NetResponse<T> {
//Listener
    /**
     * Callback interface for delivering parsed responses.
     * @param response
     */
    void onResponse(T response) throws FileNotFoundException;

    /**
     * Callback interface for delivering error responses.
     */
    void onFailure(Throwable t);

}