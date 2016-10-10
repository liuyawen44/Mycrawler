package com.yawen.Util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yawen.Url.CrawlUrl;



/**
 * Created by Avi Hayun on 9/22/2014.
 * Net related Utils
 */
public class Net {
    private static final Pattern pattern = initializePattern();

    public static Set<CrawlUrl> extractUrls(String input) {
        Set<CrawlUrl> extractedUrls = new HashSet<CrawlUrl>();

        if (input != null) {
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                CrawlUrl CrawlUrl = new CrawlUrl();
                String urlStr = matcher.group();
                if (!urlStr.startsWith("http")) {
                    urlStr = "http://" + urlStr;
                }

                CrawlUrl.setUrl(urlStr);
                extractedUrls.add(CrawlUrl);
            }
        }

        return extractedUrls;
    }

    /** Singleton like one time call to initialize the Pattern */
    private static Pattern initializePattern() {
        return Pattern.compile("\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" +
                               "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov" +
                               "|mil|biz|info|mobi|name|aero|jobs|museum" +
                               "|travel|[a-z]{2}))(:[\\d]{1,5})?" +
                               "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" +
                               "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                               "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" +
                               "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                               "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" +
                               "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");
    }
}