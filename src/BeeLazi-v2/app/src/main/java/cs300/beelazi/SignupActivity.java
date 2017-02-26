package cs300.beelazi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import cs300.beelazi.JSONHelper.JsonHelper;
import cs300.beelazi.Model.StaticData;
import cs300.beelazi.ServerWidget.RequestToServer;
import cs300.beelazi.ServerWidget.RequestToServer.RequestResult;

public class SignupActivity extends AppCompatActivity implements RequestResult{

    EditText editEmailAddress, editPassword, editConfirm, editName;
    TextView tvNoti;
    Button signupButton;
    RequestToServer requestToServer;
    String result = "";
    private String curTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initLayouts();
    }

    private void initLayouts() {
        tvNoti = (TextView) findViewById(R.id.notifitcation);
        editEmailAddress = (EditText) findViewById(R.id.addusername);
        editPassword = (EditText) findViewById(R.id.addpassword);
        editConfirm = (EditText) findViewById(R.id.addpasswordconfirm);
        editName = (EditText) findViewById(R.id.realname);
        signupButton = (Button) findViewById(R.id.signupButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    //Send information to server.
                    curTable = "User";
                    ArrayList<Pair<String, String>> conditions = new ArrayList<Pair<String, String>>();
                    conditions.add(new Pair("username", editEmailAddress.getText().toString()));
                    conditions.add(new Pair("password", editPassword.getText().toString()));
                    conditions.add(new Pair("fullname", editName.getText().toString()));
                    JsonHelper jsonHelper = new JsonHelper();
                    try {
                        String query = jsonHelper.writeQuery("INS", curTable, conditions);
                        makeRequest(query);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void makeRequest(String query){
        requestToServer = new RequestToServer();
        requestToServer.delegate = this;
        requestToServer.execute(query, StaticData.serverLink);
    }

    public boolean validate(){
        String emailAddress= editEmailAddress.getText().toString(),
                password = editPassword.getText().toString(),
                repassword = editConfirm.getText().toString(),
                realName = editName.getText().toString();
        if (emailAddress.length() == 0 || password.length() == 0 || repassword.length() == 0 || realName.length() == 0){
            Toast.makeText(this, "Please fill in all the blank space in the form", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!password.equals(repassword)){
            Toast.makeText(this, "Password confirmation does not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void processFinish(String result) {
        if (result.equals("True")){
            if (curTable.equals("Favorite")){
                finish();
                onBackPressed();
            }
            Toast.makeText(this, "Your account has been created successfully", Toast.LENGTH_SHORT).show();

            //Send information to server.

            JsonHelper jsonHelper = new JsonHelper();
            ArrayList<Pair<String, String>> conditions = new ArrayList<Pair<String, String>>();
            conditions.add(new Pair("username", editEmailAddress.getText().toString()));
            try {
                String query = jsonHelper.writeQuery("INS", "Favorite", conditions);
                curTable = "Favorite";
                makeRequest(query);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else {
            if (!curTable.equals("Favorite")){
                Toast.makeText(this, "Username is already existed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}