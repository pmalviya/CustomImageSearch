package com.example.pmalviya.customimagesearch.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.sax.EndElementListener;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.example.pmalviya.customimagesearch.R;
import com.example.pmalviya.customimagesearch.activities.SearchFiltersDialog.SearchFiltersDialogListener;
import com.example.pmalviya.customimagesearch.adapters.ImageResultsAdapter;
import com.example.pmalviya.customimagesearch.listeners.EndlessScrollListener;
import com.example.pmalviya.customimagesearch.models.ImageResult;
import com.example.pmalviya.customimagesearch.models.SearchFilter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class SearchActivity extends FragmentActivity {
	private EditText etQuery;
	private GridView gvResults;
	private String baseUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
	private ArrayList<ImageResult> imageResults;
	private ImageResultsAdapter aImageResults;
	private SearchFilter filter;
	private HashMap<Integer,Integer> starts;
	private String searchedUrl ="";
	private SearchView searchView;
	
	AsyncHttpClient client = new AsyncHttpClient();
	
	public void customLoadMoreDataFromApi(int totalItemsCount){
		//if(starts.containsKey(page)){
			String moreResultsUrl = searchedUrl + "&start=" + totalItemsCount;
			client.get(moreResultsUrl, new JsonHttpResponseHandler(){

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					JSONArray imageResultsJSON = null;
					try {
						imageResultsJSON= response.getJSONObject("responseData").getJSONArray("results");
						
						// change the results directly in the adapter,
						// it also changes the underlying data structure
						aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			});
	//	}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        filter = new SearchFilter();
        gvResults.setAdapter(aImageResults);
        gvResults.setOnScrollListener(new EndlessScrollListener() {
		
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				customLoadMoreDataFromApi(totalItemsCount);
				
			}
		});
        starts = new HashMap<Integer, Integer>();
    }
    
    public void setupViews(){
    	etQuery  = (EditText) findViewById(R.id.etQuery);
    	gvResults  = (GridView) findViewById(R.id.gvResults);
    	gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
				ImageResult result = imageResults.get(position);
				i.putExtra("result", result);
				startActivity(i);
				
				
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_settings, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
                // perform query here
        	   onImageSearch(query);
                return true;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });
       return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onSettingsClick(MenuItem mi ){
		FragmentManager fm = getSupportFragmentManager();
		SearchFiltersDialog filterDialog = SearchFiltersDialog.newInstance(filter);
        filterDialog.show(fm, "fragment_search_filter");
        filterDialog.setDialogListener(new SearchFiltersDialogListener() {
			
			@Override
			public void onDialogDone(SearchFilter sFilter) {
				filter.setColor(sFilter.getColor());
				filter.setSize(sFilter.getSize());
				filter.setType(sFilter.getType());
				filter.setColorIndex(sFilter.getColorIndex());
				filter.setSizeIndex(sFilter.getSizeIndex());
				filter.setTypeIndex(sFilter.getTypeIndex());
				filter.setSite(sFilter.getSite());
				
			}
		}); 
	}
    /**
     * This method checks presence of internet connectivity
     * @return network status
     */
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    // Event Listener for Image search button
    public void onImageSearch(String query){
    	//String query =  etQuery.getText().toString();
    	String searchUrl = baseUrl + query + "&rsz=8";
    	aImageResults.clear();
    	if(filter.getColor() != "" && filter.getColor() != "any"){
    		searchUrl += "&imgcolor=" + filter.getColor();
    	}
    	if(filter.getType() != "" && filter.getType() != "any"){
    		searchUrl += "&imgtype=" + filter.getType();
    	}
    	if(filter.getSize() != "" && filter.getSize() != "any"){
    		searchUrl += "&imgsz=" + filter.getSize();
    	}
    	
    	if(filter.getSite() != ""){
    		searchUrl += "&as_sitesearch=" + filter.getSite();
    	}
    	searchedUrl = searchUrl;
    	if(!isNetworkAvailable()){
    		Toast.makeText(this, "No Internet Access avialable", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	client.get(searchUrl + "&start=0", new JsonHttpResponseHandler(){
    		
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Toast.makeText(getApplicationContext(), "Failed due to" + responseString, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				JSONArray imageResultsJSON = null;
				try {
					imageResultsJSON= response.getJSONObject("responseData").getJSONArray("results");
					//imageResults.clear(); // clear the results everytime search button is clicked
					
					starts = new HashMap<Integer, Integer>();
					JSONArray pagesJSON = response.getJSONObject("responseData").getJSONObject("cursor").getJSONArray("pages");
					for(int i= 0;i<pagesJSON.length() ;i++){
						int key = Integer.parseInt(pagesJSON.getJSONObject(i).getString("label"));
						int value =Integer.parseInt(pagesJSON.getJSONObject(i).getString("start"));
						starts.put(key, value);
					}
					// change the results directly in the adapter,
					// it also changes the underlying data structure
					aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
 
    	});
    }
}
