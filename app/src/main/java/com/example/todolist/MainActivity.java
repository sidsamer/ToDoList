package com.example.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "MainActivity";
    private TextView DisplayTime;
    private TimePickerDialog.OnTimeSetListener TimeSetListener;
    private TextView DisplayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    private ListView listView;
    private ArrayListAdapter arrayListAdapter;
    private EditText Tasktext;
    private Button CreateBtn;
    private String filename="file.txt";
    private Spinner spinner;
    public static long timeInMillis;
    public static NotificationManagerCompat notificationManager;
    private ArrayList<TextView> aList=new ArrayList<TextView>();
    private ArrayList<Tasks> TaskList=new ArrayList<Tasks>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreateBtn = (Button) findViewById(R.id.createBtn);
        Tasktext = (EditText) findViewById(R.id.TextObj);
        DisplayDate = (TextView) findViewById(R.id.dateObj);
        DisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_DarkActionBar,
                        DateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        notificationManager=NotificationManagerCompat.from(this);
        DisplayTime= (TextView) findViewById(R.id.timeObj);
        DisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar Time= Calendar.getInstance();
                int hour= Time.get(Calendar.HOUR);
                int minute=Time.get(Calendar.MINUTE);
                TimePickerDialog dialog2= new TimePickerDialog(
                        MainActivity.this,
                        TimeSetListener,
                        hour,minute,true);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();
            }
        });
        DateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String date=day+"/"+(((month+1)==13)?1:(month+1))+"/"+year;
                DisplayDate.setText(date);
            }

        };
        TimeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time=hourOfDay+":"+minute;
                DisplayTime.setText(time);
            }
        };
        CreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Tasktext.length()!=0 && DisplayDate.length()!=0 && DisplayTime.length()!=0)
                {
                    Tasks task = new Tasks(Tasktext.getText().toString(),DisplayDate.getText().toString(),DisplayTime.getText().toString());
                    Tasktext.setText("");
                    DisplayTime.setText("");
                    DisplayDate.setText("");
                    saveFile(filename,task);
                    sortTasks(comBuilder(spinner.getSelectedItem().toString()));
                    arrayListAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplicationContext(),"somthing is missing",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        readFile(filename);
        listView = (ListView) findViewById(R.id.Task_list);
        arrayListAdapter=new ArrayListAdapter(getApplicationContext(),TaskList);
        listView.setAdapter(arrayListAdapter);
        arrayListAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder mbuilder=new AlertDialog.Builder(MainActivity.this);
                final View mView=getLayoutInflater().inflate(R.layout.dialog_layout,null);
                final EditText content=(EditText)mView.findViewById(R.id.TextObjdia);
                final TextView timeText=(TextView)mView.findViewById(R.id.timeObjdia);
                final TextView dateText=(TextView)mView.findViewById(R.id.dateObjdia);
                Button updateBtn=(Button)mView.findViewById(R.id.updateBtndia);
                Button removeBtn=(Button)mView.findViewById(R.id.removeBtndia);
                Button doneBtn=(Button)mView.findViewById(R.id.doneBtndia);
                Button alarmBtn=(Button)mView.findViewById(R.id.alarmBtndia);
                content.setText(TaskList.get(position).getTaskText());
                timeText.setText(TaskList.get(position).getTaskTime());
                dateText.setText(TaskList.get(position).getTaskDate());
                mbuilder.setView(mView);
                final AlertDialog dialog=mbuilder.create();
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(content.length()!=0 && dateText.length()!=0 && timeText.length()!=0)
                        {
                            if(TaskList.get(position).isDone())
                                return;
                            Tasks task = new Tasks(content.getText().toString(),dateText.getText().toString(),timeText.getText().toString());
                            updateTask(position,task);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"cannot update",
                                    Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();
                    }
                });
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tasks task = TaskList.get(position);
                        task.setDone(!task.isDone());
                        updateTask(position,task);
                        dialog.cancel();
                    }
                });
                removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder rbuilder=new AlertDialog.Builder(mView.getContext());
                        rbuilder.setTitle("Confirm");
                        rbuilder.setMessage("Are you sure?");
                        rbuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog2, int which) {
                                MyAlarm.RemoveAlarm(TaskList.get(position));
                                removeTask(position);
                                dialog2.dismiss();
                                dialog.cancel();
                            }
                        });

                        rbuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog2, int which) {
                                dialog2.dismiss();
                            }
                        });
                        AlertDialog alert = rbuilder.create();
                        alert.show();
                    }
                });
                alarmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setAlarm(TaskList.get(position));
                        Toast.makeText(getApplicationContext(),"Alarm Set",
                                Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }
        public void saveFile(String file,Tasks t){
        try{
            FileOutputStream fos=openFileOutput(file,Context.MODE_APPEND);
            fos.write(t.toJson().getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
             fos.close();
            Toast.makeText(getApplicationContext(),"New Task Created",
                    Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),"Error saving",
                    Toast.LENGTH_SHORT).show();
        }
        readFile(file);
    }
    public void saveFile(String file,ArrayList<Tasks> t){
        try{
            FileOutputStream fos=openFileOutput(file,Context.MODE_PRIVATE);
            for(int i=0;i<t.size();i++) {
                fos.write(t.get(i).toJson().getBytes());
                fos.write(System.getProperty("line.separator").getBytes());
            }
            fos.close();
            Toast.makeText(getApplicationContext(),"Done",
                    Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),"Error",
                    Toast.LENGTH_SHORT).show();
        }
        readFile(file);
    }
    public void readFile(String file)
    {
        String text="";
        try{
            FileInputStream fis = openFileInput(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            TaskList.clear();
            while ((line = bufferedReader.readLine()) != null) {
                JSONObject obj = new JSONObject(line);
                TaskList.add(new Tasks(obj.getString("id"), obj.getString("text"), obj.getString("time"), obj.getString("date"),Boolean.valueOf(obj.getString("done"))));
            }
            fis.close();
            //showTasks();
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),"Error reading",
                    Toast.LENGTH_SHORT).show();

    }
    }
    public void sortTasks(Comparator com){
        if(com==null)
            return;
        Collections.sort(TaskList,com);
        arrayListAdapter.notifyDataSetChanged();
        try {
            saveFile(filename,TaskList);
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),"Unable to sort"+e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void removeTask(int index){
        TaskList.remove(index);
        arrayListAdapter.notifyDataSetChanged();
        try {
            saveFile(filename,TaskList);
            Toast.makeText(getApplicationContext(),"task removed",
                    Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),"Unable to remove task"+e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void updateTask(int index,Tasks t){
        TaskList.set(index,t);
        arrayListAdapter.notifyDataSetChanged();
        try {
            saveFile(filename,TaskList);
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),"Unable to update task"+e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    public Comparator comBuilder(String s) {
        Comparator com = null;
        if (s.equals("Sooner")) {
            com = new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    try {
                        Tasks a = (Tasks) o1;
                        Tasks b = (Tasks) o2;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");;
                        Date d1 = sdf.parse(a.getTaskDate()+" "+a.getTaskTime());
                        Date d2 = sdf.parse(b.getTaskDate()+" "+b.getTaskTime());
                        return d1.compareTo(d2);
                    } catch (Exception e) {
                        return 0;
                    }
                }
            };
        } else if (s.equals("Later")) {
            com = new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    try {
                        Tasks a = (Tasks) o1;
                        Tasks b = (Tasks) o2;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");;
                        Date d1 = sdf.parse(a.getTaskDate()+" "+a.getTaskTime());
                        Date d2 = sdf.parse(b.getTaskDate()+" "+b.getTaskTime());
                        return (d1.compareTo(d2)) * -1;
                    } catch (Exception e) {
                        return 0;
                    }
                }
            };
        }
        return com;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        sortTasks(comBuilder(item));
            Toast.makeText(parent.getContext(), "Order By: " + item, Toast.LENGTH_LONG).show();
        }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public Date CurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date currentTime = Calendar.getInstance().getTime();
        String s= sdf.format(new Date(System.currentTimeMillis()));
        try {
            Date date = sdf.parse(s);
            return date;

        } catch (Exception e) {
            return null;
        }
    }
    public Date ChoosedTime(Tasks task){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date date = sdf.parse(task.getTaskDate()+" "+task.getTaskTime());
            return date;

        } catch (Exception e) {
            return null;
        }
    }
public void setAlarm(Tasks task){
 AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
 Intent intent=new Intent(this,MyAlarm.class);
    intent.putExtra("msg",task.getTaskText());
    intent.putExtra("id",task.getId());
 PendingIntent pendingIntent=PendingIntent.getBroadcast(this,task.getId(),intent,0);
    if(CurrentTime()==null || ChoosedTime(task)==null) {
        return;
    }
   timeInMillis=ChoosedTime(task).getTime()-(60*60*1000);
    alarmManager.set(AlarmManager.RTC_WAKEUP,timeInMillis,pendingIntent);
}
}