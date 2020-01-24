//==================================================
//
//  Copyright 2012 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Set;

import com.tdp2.services.loose.oaledgerlib.GetSONumbersDataService;
import com.tdp2.services.loose.oaledgerlib._2015_07.GetSONumbersData;
import com.teamcenter.clientx.AppXSession;
import com.teamcenter.soa.client.model.strong.User;



/**
 * This sample client application demonstrates some of the basic features of the
 * Teamcenter Services framework and a few of the services.
 *
 * An instance of the Connection object is created with implementations of the
 * ExceptionHandler, PartialErrorListener, ChangeListener, and DeleteListeners
 * intefaces. This client application performs the following functions:
 * 1. Establishes a session with the Teamcenter server
 * 2. Display the contents of the Home Folder
 * 3. Performs a simple query of the database
 * 4. Create, revise, and delete an Item
 *
 */
public class UpdateDBMain
{

    /**
     * @param args   -help or -h will print out a Usage statement
     */
    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            if (args[0].equals("-help") || args[0].equals("-h"))
            {
                System.out.println("usage: java [-Dhostinfodba=http://server:port/tc] com.teamcenter.hello.Hello");
                System.exit(0);
            }
        }

        // Get optional host information
        String serverHost = "http://wstn071:8080/tc";
       // String serverHost = "iiop:wstn071:1572/TcServer1";
        String host = System.getProperty("host");
        if (host != null && host.length() > 0)
        {
            serverHost = host;
        }
        long start = System.currentTimeMillis();

         
        AppXSession   session = new AppXSession(serverHost);
      //  Query       query = new Query();
        // Establish a session with the Teamcenter Server
        User user = session.login();
        System.out.println("after session login");
        
      //  query.queryItems();
        
        GetTCdata  getTCdataObj=new GetTCdata(session);
        
       LinkedHashMap<String, LinkedHashMap<String, String>>alldataList= getTCdataObj.callSOA("*","","","",false);
        System.out.println("alldataList****"+alldataList);
        new DBOperations().updateValuesInDatabase(alldataList);
       
        long end = System.currentTimeMillis();

        NumberFormat formatter = new DecimalFormat("#0.00000");
        System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");

        
        // Terminate the session with the Teamcenter server
        session.logout();

    }

}
