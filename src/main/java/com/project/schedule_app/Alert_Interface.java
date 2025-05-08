package com.project.schedule_app;

/**
 *  - Interface to more easily create alerts when doing validation elsewhere in the program
 */
@FunctionalInterface
public interface Alert_Interface {
    public void alert(String title, String header, String content);
}
