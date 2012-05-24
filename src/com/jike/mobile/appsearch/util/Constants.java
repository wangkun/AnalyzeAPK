/*
 * Zirco Browser for Android
 * 
 * Copyright (C) 2010 J. Devauchelle and contributors.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.jike.mobile.appsearch.util;

import java.util.HashMap;

/**
 * Defines constants.
 */
public class Constants {
    
    public static final boolean debugAtLocal=false;

    public static final String properties = "Ads.properties";

    public static final HashMap<String, String> AdsMap = CommonUtils
            .getPropertiesValueMap(properties);

    /*
     * cassandra-keyspace:apk_keyspace 
     * cassandra-user:jike_apk
     * cassandra-passwd:123456 
     * cassandra-col-fam:app_package
     * cassandra-apk-col:apk_data
     * cassandra-port:10110
     * cassandra-server:10.1.3.212
     * 
cassandra-port:9160
cassandra-keyspace:apk_keyspace
cassandra-user:jike_apk
cassandra-passwd:123456
cassandra-col-fam:app_package
cassandra-apk-col:apk_data
     */
    public static final String cassandra_host = "10.1.3.212";
    
    public static final int cassandra_port = 9160;
    
    public static final String cassandra_keyspace = "apk_keyspace";

    public static final String cassandra_user = "apk_keyspace";

    public static final String cassandra_passwd = "123456";

    public static final String cassandra_col_fam = "app_package";

    public static final String cassandra_apk_col = "apk_data";

}
