package com.example.telphone.model;

  
import com.example.telphone.R;

import android.content.Context;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.ImageView;  
import android.widget.TextView;  
  
public class MenuGridviewAdapter extends BaseAdapter {  
  
    private int data[]=null;  
    private int imgId[]=null;  
    private Context context=null;  
    private LayoutInflater inflater=null;  
    public MenuGridviewAdapter(Context context,int[] data, int[] imgId) {  
        super();  
        this.data = data;  
        this.imgId = imgId;  
        this.context = context;  
          
        inflater=LayoutInflater.from(context);  
    }  
  
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return data.length;  
    }  
  
    @Override  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return position;  
    }  
  
    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        return position;  
    }  
    private class Holder{  
          
        TextView tv=null;  
        ImageView img=null;  
        public TextView getTv() {  
            return tv;  
        }  
        public void setTv(TextView tv) {  
            this.tv = tv;  
        }  
        public ImageView getImg() {  
            return img;  
        }  
        public void setImg(ImageView img) {  
            this.img = img;  
        }  
          
    }  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub  
//        获得holder以及holder对象中tv和img对象的实例  
        Holder holder;  
        if(convertView==null){  
              
              
            convertView=inflater.inflate(R.layout.item_gridview, null);  
            holder=new Holder();  
            holder.tv=(TextView) convertView.findViewById(R.id.gridview_text);  
            holder.img=(ImageView) convertView.findViewById(R.id.gridview_img);  
              
            convertView.setTag(holder);  
              
        }else{  
            holder=(Holder) convertView.getTag();  
              
        }  
//        为holder中的tv和img设置内容  
        holder.tv.setText(data[position]);  
        holder.img.setImageResource(imgId[position]);  
//        注意  默认为返回null,必须得返回convertView视图  
        return convertView;  
    }  
  
}  