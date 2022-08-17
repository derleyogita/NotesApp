package com.example.notes.listener;

/**
 * @author yogitad
 * @since 17-08-2022
 * This listener used to handle runtime permission action
 **/

public interface PermissionListener {
    void isPermissionDenied(boolean isDenied);
}
