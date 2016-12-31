package com.blogspot.practicegoodtheory.testurl2;


import android.util.Log;

import com.blogspot.practicegoodtheory.core.StringResultHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlChecker implements Serializable, Runnable
{
    private static final String WELL_KNOWN_URL_STRING = "http://www.google.com";
    private StringResultHandler resultHandler;
    private String urlString = null;
    private String searchString = null;


    public UrlChecker(StringResultHandler resultHandler, String urlString, String searchString)
    {
        this.resultHandler = resultHandler;
        this.urlString = (urlString != null) ? urlString : "";
        this.searchString = (searchString != null) ? searchString : "";
    }

    @Override
    public void run()
    {
        try
        {
            pageHasString();
        } catch (AppException e1)
        {
            try
            {
                Thread.sleep(10000l);
                pageHasString();
            } catch (AppException e2)
            {
                resultHandler.handleStringResult(e2.getMessage());
            } catch (InterruptedException e)
            {
                resultHandler.handleStringResult(e1.getMessage());
            }
        }
    }

    public boolean pageHasString() throws AppException
    {
        Log.i(this.getClass().getName(), "******************* About to check page *********************"+urlString);

        boolean stringWasFound = false;
        URL theUrl = null;
        BufferedReader in = null;

        try
        {
            try
            {
                theUrl = new URL(urlString);
            } catch (MalformedURLException e)
            {
                throw new AppException("The URL is not formed correctly. Expecting something like http://www.mysite.com/xyz.php");
            }

            try
            {

                in = new BufferedReader(
                        new InputStreamReader(
                                theUrl.openStream()));
            } catch (IOException e)
            {
                try
                {
                    in = new BufferedReader(new InputStreamReader((new URL(WELL_KNOWN_URL_STRING)).openStream()));
                }
                catch (IOException e2)
                {
                    throw new AppException("Network connectivity lost. Cannot find :"+WELL_KNOWN_URL_STRING);
                }
                throw new AppException("The URL is incorrect or the page was not found:"+theUrl.toString());
            }

            if (searchString==null || searchString.isEmpty())
            {
                stringWasFound = true;
            }
            else
            {
                try
                {
                    String inputLine = null;

                    while ((inputLine = in.readLine()) != null)
                    {
                        stringWasFound = inputLine.toUpperCase().contains(searchString.toUpperCase());
                        if (stringWasFound)
                        {
                            break;
                        }
                    }
                    if (!stringWasFound)
                    {
                        throw new AppException("The URL seems valid, but the string ["+searchString+"] was not found on the page.");
                    }
                } catch (IOException e)
                {
                    throw new AppException("The URL seems valid, but an error occurred while reading the page");
                }

            }



        } catch (AppException ae)
        {
            throw ae;
        } catch (Throwable e)
        {
            Log.i(this.getClass().getName(), "******************* Failed *********************"+e.getClass());
            Log.i(this.getClass().getName(), "******************* Failed *********************"+e.getMessage());
            e.printStackTrace();
            throw new AppException("Could not access URL or reading the page");
        }
        finally
        {
            try
            {
                if (in !=null)
                {
                    in.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }


        return stringWasFound;
    }

}
