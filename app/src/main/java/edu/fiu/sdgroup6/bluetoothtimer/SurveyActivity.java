package edu.fiu.sdgroup6.bluetoothtimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        String surveyName;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                surveyName = null;
            }else{
                surveyName = extras.getString("id");
            }
        }
        else {
            surveyName = (String) savedInstanceState.getSerializable("id");
        }
        setTitle(surveyName);
    }
}
