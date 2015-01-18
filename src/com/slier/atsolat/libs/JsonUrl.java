package com.slier.atsolat.libs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class JsonUrl{

    public JSONObject getJsonFromUrl(String url) {
        BufferedReader in = null;
        JSONObject data = null;

        try {
            HttpClient client = new DefaultHttpClient();
            URI website = new URI(url);
            HttpGet request = new HttpGet();
            request.setURI(website);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String l = "";
            String nl = System.getProperty("line.seperator");
            while ((l = in.readLine()) != null) {
                sb.append(l + nl);
            }
            in.close();
            return new JSONObject(sb.toString());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}