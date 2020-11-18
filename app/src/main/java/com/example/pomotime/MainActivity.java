package com.example.pomotime;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TodoDialog.TodoDialogListener, SharedPreferences.OnSharedPreferenceChangeListener, RequestOperator.RequestOperatorListener{
    private long timeWork = 1500000;
    private long timeBreak = 300000;
    private String c_text = "";
    private long timeLeftInMillisecondsWork = timeWork;
    private long timeLeftInMillisecondsBreak = timeBreak;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setTimer();
    }

    private ActionBar myToolBar;
    private TextView workingOnWhat;
    private TextView countdownText;
    private TextView countdownBreakText;
    private Button countdownButton;
    private Button countdownButtonStop;
    private Button secondActivityButton;
    private Button addTodo;
    private CountDownTimer countDownTimer;
    private CountDownTimer countDownTimerBreak;
    private boolean WorkTimerRunning;
    private boolean BreakTimerRunning;
    private boolean WorkOrBreak = true;
    private Context context = this;
    private int breakCount = 0;
    private SharedPreferences preferences;
    private ProgressBar progressBar;
    private Button sendRequestButton;
    private TextView done;
    private TextView gaveup;
    private ModelPost publication;
    private DrawerLayout drawer;
    private int jsoncount;
    private TextView itemsCount;
    private Button sendCountRequest;
    private ArrayList<ModelPost> models;
    private IndicatingView indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        workingOnWhat = findViewById(R.id.workingOnWhat);

        createNotificationChannel();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        DBHelper dataBaseHelper = new DBHelper(this);
        if(extras != null){
            String data = intent.getExtras().getString("workOn");
            CurrentlyWorkingTodo todo = new CurrentlyWorkingTodo(1, data);
            dataBaseHelper.deleteCurrentlyWorking();
            dataBaseHelper.insertCurrentlyWorking(todo);
            workingOnWhat.setText(todo.toString());
        }
        else if(dataBaseHelper.isCurrentlyWorking() == false) {
            String workingOnTodo = dataBaseHelper.getCurrentlyWorking();
            workingOnWhat.setText("Working on: " + workingOnTodo);
        }
        else{
            workingOnWhat.setText("");
        }
        Toolbar myToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolBar);
        countdownText = findViewById(R.id.countdown_text);
        countdownBreakText = findViewById(R.id.countdown_break_text);
        countdownButton = findViewById(R.id.countdown_button);
        countdownButtonStop = findViewById(R.id.countdown_button_stop);
        secondActivityButton = findViewById(R.id.secondActivityButton);
        addTodo = findViewById(R.id.addListItem);
        countdownButtonStop.setEnabled(false);
        progressBar = findViewById(R.id.progressbar);
        int i = 0;
        progressBar.setProgress(i);
        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPause();
            }
        });

        countdownButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopDone();
            }
        });

        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        secondActivityButton.setOnClickListener(starTodoList);
        secondActivityButton.setOnLongClickListener(startTodoListLong);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        sendRequestButton = (Button) headerView.findViewById(R.id.send_request);
        sendRequestButton.setOnClickListener(requestButtonClicked);
        done = (TextView) headerView.findViewById(R.id.tasks_done);
        gaveup = (TextView) headerView.findViewById(R.id.task_gaveup);
        itemsCount = (TextView) headerView.findViewById(R.id.items_count);
        indicator = (IndicatingView) headerView.findViewById(R.id.generated_graphic);
        setTimer();
    }

    ;

    @Override
    protected void onResume() {
        super.onResume();
        setTimer();
    }

    public void openDialog() {
        TodoDialog tododialog = new TodoDialog();
        tododialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyText(String title) {
        if (title.compareTo("Hello") == 0) {
            View root = addTodo.getRootView();
            root.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
    }

    //Functions for start and resume button
    public void startPause() {
        if (WorkOrBreak) {
            if (WorkTimerRunning) {
                pauseWorkTimer();
            } else {
                startWorkTimer();
            }
        } else {
            if (BreakTimerRunning) {
                pauseBreakTimer();
            } else {
                startBreakTimer();
            }
        }
    }

    //Funtions for done and skip button
    public void stopDone() {
        if (WorkOrBreak) {
            if (WorkTimerRunning) {
                stopWorkTimer();
            } else {
                finishWorkTimer();
            }
        } else {
            skipBreakTimer();
        }
    }

    //funtion that starts the work timer

    /**
     *
     */
    public void startWorkTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillisecondsWork, 1000) {
            int i = 0;

            @Override
            public void onTick(long l) {
                timeLeftInMillisecondsWork = l;
                updateWorkTimer();
                i++;
                progressBar.setProgress((int)i*100/(60000/1000));
            }

            @Override
            public void onFinish() {
                i++;
                progressBar.setProgress(100);
                Toast toast = Toast.makeText(getApplicationContext(), "Finished", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(MainActivity.this, TimerFinishedReminder.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
            }
        }.start();
        countdownButton.setText(("PAUSE"));
        countdownButtonStop.setText("STOP");
        countdownButtonStop.setEnabled(true);
        WorkTimerRunning = true;
        WorkOrBreak = true;
    }

    //function that starts the break timer
    public void startBreakTimer() {
        if (breakCount == 4) {
            timeLeftInMillisecondsBreak = 300000;
            breakCount = 0;
        }
        countDownTimerBreak = new CountDownTimer(timeLeftInMillisecondsBreak, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillisecondsBreak = millisUntilFinished;
                updateBreakTimer();
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(MainActivity.this, BreakTimerFinishedReminder.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
            }
        }.start();
        countdownButton.setText("PAUSE");
        countdownButtonStop.setText("SKIP");
        BreakTimerRunning = true;
        WorkOrBreak = false;
    }

    //pauses work timer
    public void pauseWorkTimer() {
        countDownTimer.cancel();
        countdownButton.setText("RESUME");
        countdownButtonStop.setText("DONE");
        WorkTimerRunning = false;
    }

    //stops the work timer, makes it go back to 25:00 minutes
    public void stopWorkTimer() {
        countDownTimer.cancel();
        countdownText.setText(formatTime(timeWork));
        timeLeftInMillisecondsWork = timeWork;
        countdownButton.setText("START");
        countdownButtonStop.setText("DONE");
        countdownButtonStop.setEnabled(false);
        WorkTimerRunning = false;

    }

    //finishes work timer and starts the break timer
    public void finishWorkTimer() {
        countDownTimer.cancel();
        countdownText.setText(formatTime(timeWork));
        timeLeftInMillisecondsWork = timeWork;
        countdownButton.setText("PAUSE");
        countdownButtonStop.setText("SKIP");
        startBreakTimer();
        breakCount++;
    }

    //pauses the break timer
    public void pauseBreakTimer() {
        countDownTimerBreak.cancel();
        countdownButton.setText("RESUME");
        BreakTimerRunning = false;
    }

    //skips the break
    public void skipBreakTimer() {
        countDownTimerBreak.cancel();
        countdownBreakText.setText(formatTime(timeBreak));
        timeLeftInMillisecondsBreak = timeBreak;
        countdownButton.setText("START");
        countdownButtonStop.setText("DONE");
        countdownButtonStop.setEnabled(false);
        WorkOrBreak = true;
    }

    //updates the work timer textview every second
    public void updateWorkTimer() {
        int minutes = (int) timeLeftInMillisecondsWork / 60000;
        int seconds = (int) timeLeftInMillisecondsWork % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        countdownText.setText((timeLeftText));
    }

    //updates the break timer textview every second
    public void updateBreakTimer() {
        int minutes = (int) timeLeftInMillisecondsBreak / 60000;
        int seconds = (int) timeLeftInMillisecondsBreak % 60000 / 1000;

        String timeLeft;
        timeLeft = "" + minutes;
        timeLeft += ":";
        if (seconds < 10) timeLeft += "0";
        timeLeft += seconds;

        countdownBreakText.setText((timeLeft));
    }

    public void runTodoList(boolean flag) {
        Intent intent = new Intent(context, TodoList.class);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
    }

    View.OnClickListener starTodoList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            runTodoList(true);
        }
    };

    View.OnLongClickListener startTodoListLong = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            runTodoList(false);
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(context, Settings.class);
                intent.putExtra("flag", true);
                context.startActivity(intent);
                return true;
            case R.id.add_category:
                addCategoryDialog();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setTimer() {
        timeWork = Long.valueOf(Integer.parseInt(preferences.getString("work_duration", "1")) * 60000);
        timeLeftInMillisecondsWork = Long.valueOf(Integer.parseInt(preferences.getString("work_duration", "1")) * 60000);
        timeBreak = Long.valueOf(Integer.parseInt(preferences.getString("break_duration", "1")) * 60000);
        timeLeftInMillisecondsBreak = Long.valueOf(Integer.parseInt(preferences.getString("break_duration", "1")) * 60000);
        countdownText.setText(formatTime(timeWork));
        countdownBreakText.setText(formatTime(timeBreak));
        progressBar.setMax((Integer.parseInt(preferences.getString("work_duration", "1"))) % 60000 / 1000);
    }

    private String formatTime(long time) {
        String timeText = String.format("%01d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds((TimeUnit.MILLISECONDS.toMinutes(time))));
        return timeText;
    }

    private void addCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Category");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Category category;
                c_text = input.getText().toString();
                DBHelper dataBaseHelper = new DBHelper(MainActivity.this);
                category = new Category(-1, c_text);
                dataBaseHelper.insertCategory(category);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    View.OnClickListener requestButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setIndicatorStatus(IndicatingView.EXECUTING);
            sendRequest();
        }
    };

    private void sendRequest(){
        RequestOperator ro = new RequestOperator();
        ro.setListener(this);
        ro.start();
    }


    public void updatePublication(){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                if(jsoncount != -1){
                    itemsCount.setText("Item count: " + jsoncount);
                }else{
                    done.setText("");
                    gaveup.setText("");
                    itemsCount.setText("");
                }
            }
        });
    }

    @Override
    public void success(int count) {
        this.jsoncount = count;
        updatePublication();
        setIndicatorStatus(IndicatingView.SUCCESS);
    }

    @Override
    public void failed(int responseCode) {
        this.jsoncount = -1;
        updatePublication();
        setIndicatorStatus(IndicatingView.FAILED);
    }

//    public void updateCount(){
//        runOnUiThread(new Runnable(){
//            @Override
//            public void run(){
//                if(jsoncount != 0){
//                    itemsCount.setText("Item Count: " + jsoncount);
//                }else{
//                    itemsCount.setText("I failed :(");
//                }
//            }
//        });
//    }
//
//    @Override
//    public void successCount(int count) {
//        this.jsoncount = count;
//        updateCount();
//    }
//
//    @Override
//    public void failedCount(int responseCode) {
//        this.jsoncount = 0;
//        updateCount();
//    }
    public void setIndicatorStatus(final int status){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                indicator.setState(status);
                indicator.invalidate();
            }
        });
    }

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "LemubitReminderChannel";
            String description = "Channel for Lemubit Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyLemubit", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}