package com.valyrian.scanandpay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    List<ListItem> listItems;
    Context context;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex,int flag);
    }

    public MyAdapter(List<ListItem> listItems, Context context,ListItemClickListener listener) {
        this.listItems = listItems;
        this.context = context;
        mOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.textView_Id.setText(String.valueOf(listItem.getName()));
        holder.textViewCode.setText(listItem.getPname());
        String x=Integer.toString(listItem.getQty());
        holder.textViewType.setText(x);
        Linkify.addLinks(holder.textViewCode, Linkify.ALL);

        String item_total_price = Double.toString(listItem.getPrice()*listItem.getQty());
        holder.textViewPrice.setText("$" + item_total_price);
    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView_Id;
        public TextView textViewCode;
        public TextView textViewType;
        public TextView textViewPrice;
        public Button minusButton;
        public Button plusButton;

        public TextView getTextView_Id() {
            return textView_Id;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            textView_Id = (TextView) itemView.findViewById(R.id.textView_Id);
            textViewCode = (TextView) itemView.findViewById(R.id.textViewCode);
            textViewType = (TextView) itemView.findViewById(R.id.textViewType);
            textViewPrice=(TextView) itemView.findViewById(R.id.textViewPrice);
            minusButton = (Button) itemView.findViewById(R.id.minus);
            plusButton = (Button) itemView.findViewById(R.id.plus);
            minusButton.setOnClickListener(this);
            plusButton.setOnClickListener(this);
           // itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            int clickedPos = getAdapterPosition();
            if(v.getId() == R.id.minus)
                mOnClickListener.onListItemClick(clickedPos,1);
            else if(v.getId()==R.id.plus)
                mOnClickListener.onListItemClick(clickedPos,0);

        }
    }
}

