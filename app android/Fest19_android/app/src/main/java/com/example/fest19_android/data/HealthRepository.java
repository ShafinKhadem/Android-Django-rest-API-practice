package com.example.fest19_android.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.fest19_android.data.dao.HealthDao;
import com.example.fest19_android.data.database.UserHealthDatabase;
import com.example.fest19_android.data.model.HealthEntry;
import com.example.fest19_android.rest.GsonRequest;
import com.example.fest19_android.rest.MyStringRequest;
import com.example.fest19_android.rest.VolleySingleton;
import com.example.fest19_android.ui.login.LoginActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.example.fest19_android.ui.login.LoginActivity.API_BASE;
import static com.example.fest19_android.ui.login.LoginActivity.TAG;

public class HealthRepository {
    private static volatile HealthRepository instance;
    private HealthDao healthDao;
    private LiveData<List<HealthEntry>> allEntries;
//    public static SparseArray<HealthEntry> ITEM_MAP = new SparseArray<>();
    private VolleySingleton volleySingleton;

    private HealthRepository(Context context) {
        UserHealthDatabase userHealthDatabase = UserHealthDatabase.getInstance(context);
        healthDao = userHealthDatabase.healthDao();

//        allEntries = new MutableLiveData<>();
        allEntries = healthDao.getAllEntries();
        
        volleySingleton = VolleySingleton.getInstance(context);
    }


    public static HealthRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (HealthRepository.class) {
                instance = new HealthRepository(context);
            }
        }
        return instance;
    }
    


    public void insert(HealthEntry... healthEntries) {
        new InsertAsyncTask(healthDao).execute(healthEntries);
    }

    public void update(HealthEntry... healthEntries) {
        new UpdateAsyncTask(healthDao).execute(healthEntries);
    }

    public void delete(HealthEntry... healthEntries) {
        new DeleteAsyncTask(healthDao).execute(healthEntries);
    }

    public void deleteAll() {
        new DeleteAllAsynctask(healthDao).execute();
    }

    public LiveData<List<HealthEntry>> getAllEntries() {
        return allEntries;
    }

    public LiveData<HealthEntry> getItemById(String id) {
        GetItemByIdAsynctask getItemByIdAsynctask = new GetItemByIdAsynctask(healthDao);
        getItemByIdAsynctask.execute(id);
        return getItemByIdAsynctask.getItem();
    }



    public void fetchAll() {
        GsonRequest<HealthEntry[]> gsonRequest = new GsonRequest<>(Request.Method.GET, LoginActivity.API_BASE+"health_entries_me/", null,
                new Response.Listener<HealthEntry[]>(){
                    @Override
                    public void onResponse(HealthEntry[] response) {
//                        for (HealthEntry item : response) {
//                            ITEM_MAP.put(item.id, item);
//                        }

//                        allEntries.setValue(Arrays.asList(response));
                        insert(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error parsing health data :)", error);
                    }
                }, HealthEntry[].class, LoginRepository.getInstance(null).getHeaders());
        volleySingleton.addToRequestQueue(gsonRequest);
    }

    public void upload(HealthEntry healthEntry) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(healthEntry));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<HealthEntry> gsonRequest = new GsonRequest<>(Request.Method.POST, LoginActivity.API_BASE + "health_entries_me/", jsonObject,
                new Response.Listener<HealthEntry>() {
                    @Override
                    public void onResponse(HealthEntry response) {
                        Log.d(TAG, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading health data entry, LOL!", error);
                    }
                }, HealthEntry.class, LoginRepository.getInstance(null).getHeaders());
        volleySingleton.addToRequestQueue(gsonRequest);
    }

    public void uploadAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // often, at beginning it's null.
                while(getAllEntries().getValue()==null);
                List<HealthEntry> healthEntries = getAllEntries().getValue();
                for (HealthEntry healthEntry : healthEntries) {
                    if (healthEntry!=null) upload(healthEntry);
                }
            }
        }).start();
    }

    public LiveData<Boolean> deleteFromServer(final HealthEntry healthEntry) {
        final MutableLiveData<Boolean> ok = new MutableLiveData<>(true);
        final String url = API_BASE+"health_entries_me/"+healthEntry.id+"/";

        MyStringRequest myStringRequest = new MyStringRequest
                (Request.Method.DELETE, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response!=null && response.isEmpty()) delete(healthEntry);
                        else ok.setValue(false);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ok.setValue(false);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return LoginRepository.getInstance(null).getHeaders();
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myStringRequest);
        return ok;
    }

    public void deleteAllFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // often, at beginning it's null.
                while(getAllEntries().getValue()==null);
                List<HealthEntry> healthEntries = getAllEntries().getValue();
                for (HealthEntry healthEntry : healthEntries) {
                    if (healthEntry!=null) deleteFromServer(healthEntry);
                }
            }
        }).start();
    }



    private static class InsertAsyncTask extends AsyncTask<HealthEntry, Float, Void> {
        private HealthDao healthDao;

        InsertAsyncTask(HealthDao healthDao) {
            this.healthDao = healthDao;
        }

        @Override
        protected Void doInBackground(HealthEntry... healthEntries) {
            healthDao.insert(healthEntries);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<HealthEntry, Float, Void> {
        private HealthDao healthDao;

        UpdateAsyncTask(HealthDao healthDao) {
            this.healthDao = healthDao;
        }

        @Override
        protected Void doInBackground(HealthEntry... healthEntries) {
            healthDao.update(healthEntries);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<HealthEntry, Float, Void> {
        private HealthDao healthDao;

        DeleteAsyncTask(HealthDao healthDao) {
            this.healthDao = healthDao;
        }

        @Override
        protected Void doInBackground(HealthEntry... healthEntries) {
            healthDao.delete(healthEntries);
            return null;
        }
    }

    private static class DeleteAllAsynctask extends AsyncTask<HealthEntry, Float, Void> {
        private HealthDao healthDao;

        DeleteAllAsynctask(HealthDao healthDao) {
            this.healthDao = healthDao;
        }

        @Override
        protected Void doInBackground(HealthEntry... healthEntries) {
            healthDao.deleteAll();
            return null;
        }
    }

    private static class GetItemByIdAsynctask extends AsyncTask<String, Float, HealthEntry> {
        private HealthDao healthDao;
        MutableLiveData<HealthEntry> item = new MutableLiveData<>(null);

        GetItemByIdAsynctask(HealthDao healthDao) {
            this.healthDao = healthDao;
        }

        @Override
        protected HealthEntry doInBackground(String... strings) {
            return healthDao.getItemByid(strings[0]);
        }

        @Override
        protected void onPostExecute(HealthEntry healthEntry) {
            super.onPostExecute(healthEntry);
            item.setValue(healthEntry);
        }

        MutableLiveData<HealthEntry> getItem() {
            return item;
        }
    }
}
