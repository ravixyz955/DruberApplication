package com.example.user.druberapplication.constant;

public class Constant {
    public static final String START_JOB = "START JOB";
    public static final String STOP_JOB = "STOP JOB";
    public static final String PENDING = "Not started";
    public static final String INPROGRESS = "inprogress";
    public static final String COMPLETE = "complete";
    public static final String JOB_TYPE = "type";
    public static final String DATE = "Date";
    public static final String TIME = "Time";

    public interface ACTION {
        public static String MAIN_ACTION = "com.example.user.druberapplication.service.action.main";
        public static String STARTFOREGROUND_ACTION = "com.example.user.druberapplication.service.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.example.user.druberapplication.service.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
