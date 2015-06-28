`ORM(Object Relational Mapping)`框架采用元数据来描述对象与关系映射细节，把对象持久化到数据库中，就是以用Java的反射机制把对象和数据库记录映射关联起来。`ActiveAndroid`是ORM数据库框架。它具有下面的特点和优势：
* 基于ORM关系操作数据库
* 配置方便
* 几乎不需要编写任何SQL语句就能够保存和检索SQLite数据库记录
* 每个操作都封装为一个类，如`save()和delete()`
* 对象形式存取数据
[ActiveAndroid的github地址](https://github.com/pardom/ActiveAndroid)，里面介绍了基本的配置和使用的方法。
#基本配置
我在Android Studio使用gradle配置：
```xml
repositories {
    mavenCentral()
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}

compile 'com.michaelpardo:activeandroid:3.1.0-SNAPSHOT'
```
接下来创建一个Application类，继承自`com.activeandroid.app.Application`，如下：
```java
package me.chenfuduo.myormdemo;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

/**
 * Created by Administrator on 2015/6/28.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
```
接着在清单文件中配置：
```xml
 <application
        android:name=".MyApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme" >
        <meta-data android:name="AA_DB_NAME" android:value="Student.db"/>
        <meta-data android:name="AA_DB_VERSION" android:value="1"/>
        <activity
            android:name=".MainActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
```
在application结点下，加上刚刚新建的Application的name，再以元数据的形式加上数据库的名字和版本。
这样就配置好了。
#简单的demo演示
demo主要演示增删改插。
首先是主界面的布局：
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    android:orientation="vertical"
    tools:context=".MainActivity">


    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="add"
        android:onClick="add"
        />


    <ListView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/lv" />

</LinearLayout>
```
接着是实体类：
```java
package me.chenfuduo.myormdemo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/6/28.
 */
@Table(name = "Students")
public class Student extends Model{
    @Column(name = "Name")
    public String name;
    @Column(name = "Age")
    public Integer age;
}
```
指定了表名和列名。
下面是Adapter。
```java
package me.chenfuduo.myormdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2015/6/28.
 */
public class MyAdapter extends BaseAdapter {

    private Context context;

    private List<Student> list;

    public MyAdapter(Context context, List<Student> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item,null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.btn_del = (Button) convertView.findViewById(R.id.btn_del);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Student student = list.get(position);
        holder.tv.setText(student.name + "\n" + student.age);
        holder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student.delete();
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView tv;
        Button btn_del;
    }

}
```
每个条目均有删除的按钮，删除就是调用下`delete()`方法。注意要通知数据集的变化。
主类：
```java
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
        student.age = 10;
		student.save();
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
```
看到，添加通过`save()`方法，查询通过下面的方法：
```java
 List<Student> studentLists = new Select().from(Student.class).execute();
 ```
 改也是通过save()方法，改变field的值，最后调用save()即可。
 数据库的增删改插介绍完了，最后是数据库的升级。
 #数据库的升级
 假设在数据库版本2添加了学生性别一列。
 我们需要在`assets`下新建文件夹`migrations`，在`migrations`下新建文件`2.sql`，内容是：
 ```xml
 ALTER Students Items ADD COLUMN sex INTEGER;
 ```
 同时在实体类需要添加：
 ```java
  @Column(name = "Sex")
    public Integer sex;
```
最后在清单文件中将数据库的版本修改为2即可。
[demo的源码](https://github.com/leerduo/MyORMDemo)