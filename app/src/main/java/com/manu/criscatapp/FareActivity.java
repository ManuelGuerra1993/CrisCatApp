package com.manu.criscatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FareActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    ListView listService;
    SearchView searchService;
    List<String> listaServicios = new ArrayList<>();
    ArrayAdapter<String> adaptlr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare);
        searchService = findViewById(R.id.searchService);

        asignReferences();
        listAllServices();

        searchService.setOnQueryTextListener(this);

    }


    private void asignReferences(){

        searchService = findViewById(R.id.searchService);
        listService = findViewById(R.id.listService);


    }

    private void buscar( String txt){

        //String txt = "";
        String url = "http://criscatapp.atwebpages.com/index.php/services";

        int txtLen = txt.length();
        if (txtLen > 0){
            url += "/"+ txt;
        }



            StringRequest peticion = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONArray arreglo = new JSONArray(response);
                        listaServicios.clear();
                        for( int i=0; i<arreglo.length(); i++){
                            JSONObject objeto = arreglo.getJSONObject(i);
                            listaServicios.add(objeto.getString("nombre") +"                      Costo:" + objeto.getString("precio"));
                        }
                        adaptlr = new ArrayAdapter<>(FareActivity.this, android.R.layout.simple_list_item_1,listaServicios);
                        listService.setAdapter(adaptlr);

                    }catch (JSONException e){

                        Log.d("==>", e.toString());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("==>", error.toString());
                }
            });

            RequestQueue queueDat = Volley.newRequestQueue(this);
            queueDat.add(peticion);


    }

    private void listAllServices(){

        String url = "http://criscatapp.atwebpages.com/index.php/services";

        StringRequest peticion = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray arreglo = new JSONArray(response);
                    Log.d("==>", arreglo.toString());

                    listaServicios.clear();

                    for( int i=0; i<arreglo.length(); i++){
                        JSONObject objeto = arreglo.getJSONObject(i);
                        listaServicios.add(objeto.getString("nombre") + "                      Costo:" + objeto.getString("precio"));
                    }
                    adaptlr = new ArrayAdapter<>(FareActivity.this, android.R.layout.simple_list_item_1,listaServicios);
                    listService.setAdapter(adaptlr);

                }catch (JSONException e){

                    Log.d("==>", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("==>", error.toString());
            }
        });

        RequestQueue queueDat = Volley.newRequestQueue(this);
        queueDat.add(peticion);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        buscar(s);
        return false;
    }
}