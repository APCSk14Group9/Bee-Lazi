package cs300.beelazi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;

import cs300.beelazi.CustomFragment.PersonalFragment;

public class PersonalEditActivity extends AppCompatActivity {

    String name;
    Button adjustFoodButton, adjustMovieButton, saveButton;
    EditText editName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_edit);
        name = getIntent().getStringExtra("realName");
        initLayout();
        setEvents();
    }

    private void setEvents() {
        adjustFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalEditActivity.this, AdjustFoodFlavorActivity.class);
                startActivity(intent);
            }
        });
        adjustMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalEditActivity.this, AdjustMovieFlavorActivity.class);
                startActivity(intent);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editName.length() == 0 ){
                    Toast.makeText(PersonalEditActivity.this, "Name cannot be empty string", Toast.LENGTH_SHORT).show();
                }
                else{
                    PersonalFragment.tvName.setText(editName.getText().toString());
                    onBackPressed();
                }
            }
        });
    }

    private void initLayout() {
        adjustFoodButton = (Button) findViewById(R.id.adjustFood);
        adjustMovieButton = (Button) findViewById(R.id.adjustMovie);
        saveButton = (Button) findViewById(R.id.saveButton);
        editName = (EditText) findViewById(R.id.realName);
        editName.setText(name);
        if (AccessToken.getCurrentAccessToken() != null){
            editName.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
