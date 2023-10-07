package com.telerikacademy.api.tests;

public class JSONRequests {
    public static final String BOARD = "{\n" +
            "    \"name\": \"%s\",\n" +
            "    \"desc\": \"%s\",\n" +
            "    \"idOrganization\": \"%s\", \n" +
            "    \"key\": \"%s\",\n" +
            "    \"token\": \"%s\"\n" +
            "}";

    public static final String LIST = "{\n" +
            "    \"name\": \"%s\",\n" +
            "    \"idBoard\": \"%s\",\n" +
            "    \"key\": \"%s\",\n" +
            "    \"token\": \"%s\"\n" +
            "}";


    public static final String CARD = "{\n" +
            "    \"name\": \"%s\",\n" +
            "    \"desc\": \"%s\",\n" +
            "    \"idList\": \"%s\",\n" +
            "    \"key\": \"%s\",\n" +
            "    \"token\": \"%s\"\n" +
            "}";

    public static final String UPDATE_CARD = "{\n" +
            "    \"name\": \"%s\",\n" +
            "    \"desc\": \"%s\",\n" +
            "    \"key\": \"%s\",\n" +
            "    \"token\": \"%s\"\n" +
            "}";

    public static final String UPDATE_CARD_COVER_COLOR = "{\n" +
            "    \"cover\": {\n" +
            "        \"color\": \"%s\"\n" +
            "    }\n" +
            "}";
}
