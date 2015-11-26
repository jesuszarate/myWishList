package edu.utah.cs4962.mywishlist.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by jesuszarate on 11/24/15.
 */
public class NetworkClass
{

    private class wishList
    {
        public UsrList usrList;
    }

    private class UsrList
    {
        public int uid;
        public ArrayList<item> lst;
    }

    private class item
    {
        public int idex;
        public String name;
        public String price;
        public boolean sale;
        public String want;
        public double longitude;
        public double latitude;
    }

    wishList _wishList;
    Gson _gson = new Gson();

    //String BASE_URL = "http://155.99.90.115:5000/todo/api/v1.0/tasks/2";
    //String BASE_URL = "http://155.99.90.115:5000/myWishList/api/v1.0/list"; // School

    //String BASE_URL = "http://192.168.1.5:5000/myWishList/api/v1.0/list"; // Home
    String BASE_URL = "http://192.168.1.19:5000/myWishList/api/v1.0/list"; // Raspberry Pi


    public String GetRequest(String sUrl)
    {
        try
        {
            URL url = new URL(sUrl);
            HttpURLConnection getRequest = (HttpURLConnection)url.openConnection();
            Scanner hwScanner = new Scanner(getRequest.getInputStream());

            StringBuilder stringBuilder = new StringBuilder();
            while(hwScanner.hasNext())
                stringBuilder.append(hwScanner.nextLine());

            return stringBuilder.toString();
        }
        catch (Exception e)
        {
            Log.e("GetRequest", e.toString());
        }
        return "Didn't work";
    }

    public boolean getUserList(Context context, int USER)
    {
        String stringUrl = BASE_URL + "/" + USER;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new DownloadItemList().execute(stringUrl);
            return true;
        } else
        {
            return false;
        }
    }

    private class DownloadItemList extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            // params comes from the execute() call: params[0] is the url.
            return GetRequest(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                Gson gson = new Gson();

                //TODO: do something with the list
                _wishList = gson.fromJson(result, wishList.class);

            }catch (NullPointerException ne){
                // Do nothing if there was a NullPointerException.
                Log.e("NullPointerException", ne.toString());
            }
            catch (Exception e)
            {
                Log.e("gson", e.toString());
            }
        }
    }

    //region <Add new Item>
    public void addNewItem (Context context)
    {
        String playerNameTag = "playerName";

        String stringUrl = "http://155.99.90.115:5000/myWishList/api/v1.0/list";
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new addNewItemTask().execute(stringUrl, playerNameTag);
        } else
        {
            //textView.setText("No network connection available.");
        }

    }

    private class addNewItemTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings)
        {
            // params comes from the execute() call: params[0] is the url.
            try
            {
                return requestAddNewItem(strings[0]);
            } catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            // Do something with the returned information
            HashMap<String, String> missileAttackResult = parseMissileAttackResult(result);

            if (missileAttackResult != null)
            {

            }

        }

    }

    private String requestAddNewItem(String myurl) throws IOException
    {
        InputStream is = null;

        try
        {
            HashMap<String, String> newItem = new HashMap<String, String>();

            newItem.put("uid", "1");
            newItem.put("latitude", "000.000000");
            newItem.put("location", "Heck yeah");
            newItem.put("longitude", "-111.111111");
            newItem.put("name", "What");
            newItem.put("price", "700");
            newItem.put("sale", "False");
            newItem.put("want", "10");

            String jsonMissileAttack = _gson.toJson(newItem);

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppostreq = new HttpPost(myurl);

            StringEntity stringEntity = new StringEntity(jsonMissileAttack);

            stringEntity.setContentType("application/json");
            httppostreq.setEntity(stringEntity);

            HttpResponse httpResponse = httpclient.execute(httppostreq);

            String responseText;

            responseText = EntityUtils.toString(httpResponse.getEntity());

            return responseText;

        } finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    private HashMap<String, String> parseMissileAttackResult(String result)
    {
        try
        {
            Type gameType = new TypeToken<HashMap<String, String>>()
            {
            }.getType();
            //HashMap<String, String> missileAttackRes = _gson.fromJson(result, gameType);
            //return missileAttackRes;
            return null;
        } catch (Exception e)
        {
            return null;
        }
    }
    //endregion <Add new Item>


}
