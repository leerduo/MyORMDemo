package me.chenfuduo.myormdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv;

    private MyAdapter adapter;

    private List<Student> studentList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView() {
        lv = (ListView) findViewById(R.id.lv);
        adapter = new MyAdapter(this,studentList);
        lv.setAdapter(adapter);

        //查询
        List<Student> studentLists = new Select().from(Student.class).execute();

        studentList.addAll(studentLists);


        //添加
        for (int i = 0; i < 8; i++) {
            Student student = new Student();
            student.name = "LiLei" + i;
            student.age = 10 + i;
            student.save();
            studentList.add(student);
        }

        adapter.notifyDataSetChanged();
    }


    //添加
    public void add(View view){
        Student student = new Student();
        student.name = "add item name";
        student.save();
        student.age = 10;
        studentList.add(student);
        adapter.notifyDataSetChanged();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
