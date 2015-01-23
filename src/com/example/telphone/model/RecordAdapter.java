package com.example.telphone.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.telphone.R;
import com.example.telphone.activity.Calling;
import com.example.telphone.property.SingleRecord;



import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.CallLog.Calls;
import android.support.v7.appcompat.R.color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;




public class RecordAdapter extends BaseAdapter {  
	private final String TAG = "RecordAdapter";
    private LayoutInflater mInflater;    
    private List<Map<String, Object>> mData;    
    private Context context;
    private ArrayList<SingleRecord> list;
    

    public RecordAdapter(Context context,List<SingleRecord> list) {    
        mInflater = LayoutInflater.from(context);  
        this.list = (ArrayList<SingleRecord>) list;
        this.context = context;
//        init();   
    }    
    

    private void init() {    
        mData=new ArrayList<Map<String, Object>>(); 
        for (int i =0;i<list.size();i++)
        {
        	Map<String,Object> map = new HashMap<String ,Object>();
        	map.put("title", list.get(i));
        	mData.add(map);
        }        
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
    public View getView(int position, View convertView, ViewGroup parent) {    
        
    	ViewHolder holder ;
        
        if (convertView == null) {    
            holder = new ViewHolder();    
            convertView = mInflater.inflate(R.layout.item_record, parent,false);    
            holder.iv_call_type = (ImageView) convertView.findViewById(R.id.iv_ir_call_type);
            holder.tv_call_name = (TextView) convertView.findViewById(R.id.tv_ir_name);
            holder.tv_call_number = (TextView) convertView.findViewById(R.id.tv_ir_phonenum);
            holder.tv_call_date = (TextView) convertView.findViewById(R.id.tv_ir_calldate);
            holder.iv_call_right = (ImageView)convertView.findViewById(R.id.iv_ir_callphone);
            convertView.setTag(holder);
            
        } else {    
            holder = (ViewHolder) convertView.getTag();    
        }
        
        switch(list.get(position).type)
        {
        case Calls.INCOMING_TYPE:
        	holder.iv_call_type.setImageResource(R.drawable.call_coming);
        	break;
        case Calls.OUTGOING_TYPE:
        	holder.iv_call_type.setImageResource(R.drawable.call_ougoing);
        	break;        	
        case Calls.MISSED_TYPE:
        	holder.iv_call_type.setImageResource(R.drawable.call_missing);
        	break;
        	default:
        		holder.iv_call_type.setImageResource(R.drawable.call_coming);
        		break;
        }
        SingleRecord t = list.get(position);
        if(null == t.name)
        {
        	holder.tv_call_name.setText("δ֪");
        }
        else
        {
        	holder.tv_call_name.setText(list.get(position).name);
        }
        if(t.number.length()<3)
        {
        	holder.tv_call_number.setText("δ֪");
        }
        else
        {
        	holder.tv_call_number.setText(list.get(position).number);
        }
        holder.tv_call_date.setText(list.get(position).time);
        final int pos = position;
        
        holder.iv_call_right.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(RecordAdapter.this.context,Calling.class);
				it.putExtra("number", list.get(pos).number);
				RecordAdapter.this.context.startActivity(it);
			}
        	
        });
        
        return convertView;    
    }    
    
    public final class ViewHolder {
    	public ImageView iv_call_type;  //
    	public TextView tv_call_name;    //
    	public TextView tv_call_number;    // 
    	public TextView tv_call_date;    // 
    	
    	public ImageView iv_call_right;

    }
}
