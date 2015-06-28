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
