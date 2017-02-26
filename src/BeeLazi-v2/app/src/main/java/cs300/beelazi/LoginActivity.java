package cs300.beelazi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import cs300.beelazi.JSONHelper.JsonHelper;
import cs300.beelazi.Model.StaticData;
import cs300.beelazi.Model.UserInfomation;
import cs300.beelazi.ServerWidget.RequestToServer;

public class LoginActivity extends AppCompatActivity implements RequestToServer.RequestResult{

    EditText usernameText, passwordText;
    Button signinButton, signinFBButton, signupButton;
    LoginButton loginButton;
    RequestToServer requestToServer;
    CallbackManager callbackManager;
    private String curTable = "User";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        initLayout();
        setEvents();
        checkLoggedIn();
    }

    void makeQuery(String query){
        requestToServer = new RequestToServer();
        requestToServer.delegate = this;
        requestToServer.execute(query, StaticData.serverLink);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initLayout() {
        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        signinButton = (Button) findViewById(R.id.signinButton);
        signinFBButton = (Button) findViewById(R.id.signinFBButton);
        signupButton = (Button) findViewById(R.id.signupButton);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email", "user_friends", "public_profile", "user_photos"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                Toast.makeText(LoginActivity.this, "Login Succeeded", Toast.LENGTH_SHORT).show();
                getUserProfile();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEvents(){
        signinFBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.callOnClick();
            }
        });
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameText.getText().toString(), password = passwordText.getText().toString();
                if (username.length() == 0 && password.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please fill in your username and password", Toast.LENGTH_SHORT).show();
                } else if (username.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please fill in your username", Toast.LENGTH_SHORT).show();
                } else if (password.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please fill in your password", Toast.LENGTH_SHORT).show();
                } else {
                    curTable = "User";
                    JsonHelper jsonHelper = new JsonHelper();
                    ArrayList<Pair<String, String>> condition = new ArrayList<Pair<String, String>>();
                    condition.add(new Pair("username", usernameText.getText().toString()));
                    condition.add(new Pair("password", passwordText.getText().toString()));
                    try {
                        String query = jsonHelper.writeQuery("SEL", "User", condition);
                        makeQuery(query);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        finish();
        startActivity(intent);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private void checkLoggedIn() {
        if (isLoggedIn()) {
            getUserProfile();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //Get user profile information: id, name, email, birthday
    private void getUserProfile(){
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(final JSONObject object, GraphResponse response) {
                        try {
                            String id = object.getString("id");
                            String name = object.getString("name");
                            String urlProfilePicture = "https://graph.facebook.com/" + id + "/picture?type=large";

                            curTable = "Facebook";

                            StaticData.setMainUser(new UserInfomation(id, name, "", urlProfilePicture));

                            JsonHelper jsonHelper = new JsonHelper();
                            String action = "SEL";
                            String table = "User";
                            ArrayList<Pair<String, String>> condition = new ArrayList<Pair<String, String>>();
                            condition.add(new Pair("username", id));
                            condition.add(new Pair("password", ""));
                            condition.add(new Pair("fullname", name));
                            try {
                                String query = jsonHelper.writeQuery(action, table, condition);
                                makeQuery(query);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        Bundle params = new Bundle();
        params.putString("fields", "id,name,email,birthday");
        request.setParameters(params);
        request.executeAsync();
    }

    private String decodeUnicode(String string){
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
            Log.d("fuckfuck", string);
            return string;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    @Override
    public void processFinish(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            if (jsonArray.length() == 0){
                if (curTable.equals("Facebook")){
                    Log.d("tudeptrai", "YES");
                    JsonHelper jsonHelper = new JsonHelper();
                    String action = "INS";
                    String table = "User";
                    ArrayList<Pair<String, String>> condition = new ArrayList<Pair<String, String>>();
                    condition.add(new Pair("username", StaticData.user.getUsername()));
                    condition.add(new Pair("password", StaticData.user.getPassword()));
                    condition.add(new Pair("fullname", StaticData.user.getRealname()));
                    condition.add(new Pair("imagelink", StaticData.user.getImageProfile()));
                    try {
                        String query = jsonHelper.writeQuery(action, table, condition);
                        makeQuery(query);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    table = "Favorite";
                    condition.clear();
                    condition.add(new Pair<String, String>("username", StaticData.user.getUsername()));
                    try {
                        String query = jsonHelper.writeQuery(action, table, condition);
                        makeQuery(query);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(this, "Your account does not exist", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Log.d("abcde", "YESSSSS");
                JSONObject object = jsonArray.getJSONObject(0);
                String name = object.getString("username");
                String realname = object.getString("fullname");
                String password = object.getString("password");
                String imageLink = object.getString("imagelink");
                UserInfomation userInfo = new UserInfomation(name, realname, password, imageLink);
                StaticData.setMainUser(userInfo);
                Toast.makeText(LoginActivity.this, "Login Succeeded", Toast.LENGTH_SHORT).show();
                gotoMainActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}