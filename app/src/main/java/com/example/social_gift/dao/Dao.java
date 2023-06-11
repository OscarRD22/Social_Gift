package com.example.social_gift.dao;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.social_gift.model.WishList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Dao {
    Context context;
    String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1";
    String wishList_url = "/wishlists";
    String users = "/users";
    String friends = "/friends";

    RequestQueue requestQueue;
    JSONObject jsonObject;

    public Dao(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        jsonObject = new JSONObject();
    }


    public void loginUser(String email, String password, Response.Listener<JSONObject> listenerLogin, Response.ErrorListener error) {
        String login = url + "/users/login";
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, login, jsonObject, listenerLogin, error);
            requestQueue.add(jr);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void enviarSolicitudAmistad(int idUsuario, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String urlSolicitudAmistad = url + friends + "/" + idUsuario;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlSolicitudAmistad, null, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }


    public void registerUser(String name, String lastName, String email, String password, String img, Response.Listener<JSONObject> listenerLogin, Response.ErrorListener error) {
        String login = url + "/users";
        try {
            jsonObject.put("name", name);
            jsonObject.put("last_name", lastName);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("image", img);

            JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, login, jsonObject, listenerLogin, error);
            requestQueue.add(jr);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public void getAllUsers(Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String getUsersUrl = url + "/users";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, getUsersUrl, null, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }


    public void getAllRequests(Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String getUsersRequestsUrl = url + friends + "/requests";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, getUsersRequestsUrl, null, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }



    public void getAllFriends(Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String getAllFriendsUrl = url + friends;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, getAllFriendsUrl, null, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }

    public void getUserInfo(int userId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String getUserUrl = url + "/users/" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUserUrl, null, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void getFriendWishlists(int userId, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        String urlGetFriendWishlists = url + users + "/" + userId + "/wishlists";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlGetFriendWishlists, null,
                successListener,
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                return headers;
            }
        };

        requestQueue.add(request);
    }


    public void getUserId(String email, Response.Listener<JSONArray> listener, Response.ErrorListener error) {
        String searchUser = url + "/users/search?s=" + email;
        JsonArrayRequest jsonArray = new JsonArrayRequest(Request.Method.GET, searchUser, null, listener, error) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                return headers;
            }
        };
        requestQueue.add(jsonArray);
    }

    public void getMyUser(int id, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String getMyUserUrl = url + "/users/" + id;
        Log.wtf("id", "ID DEL USUARIO" + id);
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, getMyUserUrl, null, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                return headers;
            }
        };
        requestQueue.add(jsonObject);
    }


    public void editUser(String name, String lastName, String email, String password, String img, Response.Listener<JSONObject> editUser, Response.ErrorListener error) {
        String login = url + "/users";
        try {
            jsonObject.put("name", name);
            jsonObject.put("last_name", lastName);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("image", img);

            JsonObjectRequest jr = new JsonObjectRequest(Request.Method.PUT, login, jsonObject, editUser, error) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                    return headers;
                }
            };
            requestQueue.add(jr);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void searchUsers(String searchTerm, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String searchUsersUrl = url + "/users/search?s=" + searchTerm;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, searchUsersUrl, null, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }


    //----------------------------------------------------LISTS---------------------------------------------------------//

    public void createWishList(WishList wishList, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String createListUrl = url + wishList_url;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", wishList.getName());
            jsonObject.put("description", wishList.getDescription());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createListUrl, jsonObject, listener, errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                    return headers;
                }
            };

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void editWishList(int listId, String name, String description, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String editListUrl = url + wishList_url + "/" + listId;
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);
            requestBody.put("description", description);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, editListUrl, requestBody, listener, errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteWishList(int listId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String deleteListUrl = url + wishList_url + "/" + listId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteListUrl, null, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }


    public void getMyLists(Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String getListsUrl = url + users + "/" + loadIdSharedPreferences(context) + wishList_url;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, getListsUrl, null, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + loadTokenSharedPreferences(context));
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }

    public void getFriendshipRequests(Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        String urlGetFriendshipRequests = url + friends + "/requests";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlGetFriendshipRequests, null,
                successListener,
                errorListener);

        // Add the request to the request queue
        requestQueue.add(request);
    }

    //----------------------------------------------------LISTS---------------------------------------------------------//

    //----------------------------------------------------SHARED PREFERENCES---------------------------------------------------------//


    public String loadTokenSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("tokens", MODE_PRIVATE);
        return sharedPref.getString("token", null);
    }

    public void saveTokenSharedPreferences(String token, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("tokens", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", token);
        editor.apply();
    }


    public void deleteTokenSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("tokens", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("token");
        editor.apply();
    }


    public int loadIdSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("idUser", MODE_PRIVATE);
        return sharedPref.getInt("id", 0);
    }

    public void saveIdSharedPreferences(int id, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("idUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("id", id);
        editor.apply();
    }


    //----------------------------------------------------SAVE PASSWORD---------------------------------------------------------//

    public String loadPasswordSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("password", MODE_PRIVATE);
        return sharedPref.getString("password", null);
    }

    public void savePasswordSharedPreferences(String password, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("password", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("password", password);
        editor.apply();
    }

    //----------------------------------------------------SAVE PASSWORD---------------------------------------------------------//

    //----------------------------------------------------SHARED PREFERENCES---------------------------------------------------------//


}
