package com.example.todolist;


import org.json.JSONException;
import org.json.JSONObject;

public class Tasks {
    public static int num=0;
    private int id;
    private String taskText;
    private String taskTime;
    private String taskDate;
    private boolean done;

    public Tasks(String text,String date,String time)
    {
        setTaskDate(date);
        setTaskText(text);
        setTaskTime(time);
        this.done=false;
        num++;
        setId(num);
    }
    public Tasks(String id,String text,String time,String date,boolean done)
    {
        setTaskDate(date);
        setTaskText(text);
        setTaskTime(time);
        setId(Integer.parseInt(id));
        this.done=done;
    }

    public int getId() {
        return id;
    }

    public String getTaskText() {
        return taskText;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public boolean isDone() {
        return this.done;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
    public String toString()
    {
        return this.taskText+" "+this.taskTime+" "+this.taskDate+"   "+this.done;
    }

    public String toJson()
    {
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("id",getId());
            jsonObject.put("text",getTaskText());
            jsonObject.put("time",getTaskTime());
            jsonObject.put("date",getTaskDate());
            jsonObject.put("done",String.valueOf(this.done));
            return  jsonObject.toString();
        }
        catch (JSONException e)
        {
            return "";
        }

    }
}
