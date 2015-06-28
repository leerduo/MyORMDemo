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
    @Column(name = "Sex")
    public Integer sex;
}
