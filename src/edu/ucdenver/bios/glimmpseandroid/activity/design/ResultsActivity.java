package edu.ucdenver.bios.glimmpseandroid.activity.design;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import edu.ucdenver.bios.glimmpseandroid.R;
import edu.ucdenver.bios.glimmpseandroid.adapter.GestureFilter;
import edu.ucdenver.bios.glimmpseandroid.adapter.PowerListAdapter;
import edu.ucdenver.bios.glimmpseandroid.adapter.GestureFilter.SimpleGestureListener;
import edu.ucdenver.bios.glimmpseandroid.adapter.ResultsListAdapter;
import edu.ucdenver.bios.webservice.common.domain.PowerResult;
import edu.ucdenver.bios.webservice.common.domain.PowerResultList;

public class ResultsActivity extends Activity implements SimpleGestureListener {
    private static GestureFilter detector;
    private static PowerResultList list;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        boolean useTitleFeature = false;
        detector = new GestureFilter(this,this);
        // If the window has a container, then we are not free
        // to request window features.
        if (window.getContainer() == null) {
            useTitleFeature = window
                    .requestFeature(Window.FEATURE_CUSTOM_TITLE);
        }
        setContentView(R.layout.results);

        if (useTitleFeature) {
            window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        }
        
        TextView title = (TextView) findViewById(R.id.window_title);
        title.setText(getResources().getString(R.string.title_results));
        
        Button homeButton = (Button) findViewById(R.id.home_button);
        homeButton.setText(getResources().getString(R.string.title_design));
        homeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // GlobalVariables.resetInstance();
                finish();
            }
        });
        
        Button back = (Button) findViewById(R.id.back_button);
        back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // GlobalVariables.resetInstance();
                finish();
            }
        });
        
        
        
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            
            System.out.println("Extras not null");
            String jsonString = extras.getString("results");
            System.out.println("JsonString : "+jsonString);
            ListView resultsListView = (ListView)findViewById(R.id.results_list_view);
            View header1 = getLayoutInflater().inflate(
                    R.layout.results_list_header, null, false);
            if (resultsListView.getHeaderViewsCount() == 0)
                resultsListView.addHeaderView(header1);
            
            ObjectMapper mapper = new ObjectMapper();
            try
            {
                list = mapper.readValue(jsonString, PowerResultList.class);                         
                System.out.println("-----------------------------------------------------");                       
               /* if(list == null || list.isEmpty())
                    System.out.println("Empty results");
                else
                    System.out.println(list);*/
                for(PowerResult power : list)
                {
                    String data = power.toXML()+"\n";
                    //text.setText(data);
                    System.out.println(data);
                }
                //resultsListView.setAdapter(new ResultsListAdapter(ResultsActivity.this,list));      
                resultsListView.setAdapter(new ResultsListAdapter(ResultsActivity.this, list));
            } catch (Exception e)
            {
                //text.setText("testPower Failed to retrieve: " + e.getMessage());    
                System.out.println("Exception: " + e.getMessage());
            }
        }
        
        
        Button saveEmail = (Button) findViewById(R.id.save_email_button);
        saveEmail.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                
                String columnString =   "\"Power\",\"SampleSize\"";
                String dataString = null;
                for(PowerResult power : list)
                {
                    if(dataString == null || dataString.isEmpty()) {
                      dataString = new String();
                    }
                    dataString = dataString + "\"" + power.getActualPower() +"\",\"" + power.getTotalSampleSize() + "\"" + "\n";
                }     
                String combinedString = columnString;
                if(dataString != null && !dataString.isEmpty())
                    combinedString = combinedString + "\n" + dataString;
                
                File file   = null;
                File root   = Environment.getExternalStorageDirectory();
                if (root.canWrite()){
                    File dir    =   new File (root.getAbsolutePath() + "/PowerResultData");
                    System.out.println(dir);
                     dir.mkdirs();
                     file   =   new File(dir, "PowerResults.csv");
                     FileOutputStream out   =   null;
                    try {
                        out = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        try {
                            out.write(combinedString.getBytes());
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                        try {
                            out.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }                
                Uri url  =   null;
                url  =  Uri.fromFile(file);

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "PowerResult Details");
                sendIntent.putExtra(Intent.EXTRA_STREAM, url);
                //sendIntent.setType("text/html");
                sendIntent.setType("message/rfc822");
                startActivity(sendIntent);

            }
        });
        
    }
    
    public void onDoubleTap() {
        // TODO Auto-generated method stub
        
    }

    
    public boolean dispatchTouchEvent(MotionEvent me){
        //System.out.println("dispatchTouchEvent");
        detector.onTouchEvent(me);
       return super.dispatchTouchEvent(me);
      }

    public void onSwipe(int direction) {
       // TODO Auto-generated method stub
       /*if(direction == 3)
           finish();*/
       String str = "";
            
           switch (direction) {
            
           case GestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                                                   finish();
                                                    break;
           /*case GestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                                                          break;
           case GestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                                                          break;
           case GestureFilter.SWIPE_UP :    str = "Swipe Up";
                                                          break;*/
                                                     
           }
            //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
