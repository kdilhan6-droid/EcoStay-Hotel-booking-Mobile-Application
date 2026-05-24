package com.ecostay.retreat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RoomAdapter extends BaseAdapter {
    private Context context;
    private List<RoomItem> roomList;
    private LayoutInflater inflater;

    public RoomAdapter(Context context, List<RoomItem> roomList) {
        this.context = context;
        this.roomList = roomList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return roomList.size();
    }

    @Override
    public Object getItem(int position) {
        return roomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_room, parent, false);
            holder = new ViewHolder();
            holder.roomImage = convertView.findViewById(R.id.room_image);
            holder.roomName = convertView.findViewById(R.id.room_name);
            holder.roomPrice = convertView.findViewById(R.id.room_price);
            holder.roomFeatures = convertView.findViewById(R.id.room_description);
            holder.bookButton = convertView.findViewById(R.id.book_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RoomItem room = roomList.get(position);
        holder.roomName.setText(room.getName());
        holder.roomPrice.setText(room.getPrice());
        holder.roomFeatures.setText(room.getFeatures());
        
        // Set room image background based on room type
        if (room.getName().toLowerCase().contains("treehouse")) {
            holder.roomImage.setBackgroundColor(context.getResources().getColor(R.color.eco_primary));
        } else if (room.getName().toLowerCase().contains("cabin")) {
            holder.roomImage.setBackgroundColor(context.getResources().getColor(R.color.eco_secondary));
        } else {
            holder.roomImage.setBackgroundColor(context.getResources().getColor(R.color.accent_orange));
        }
        
        // Set up book button click listener
        holder.bookButton.setOnClickListener(v -> {
            Toast.makeText(context, "🏨 Booking " + room.getName() + "...", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView roomImage;
        TextView roomName;
        TextView roomPrice;
        TextView roomFeatures;
        Button bookButton;
    }

    public static class RoomItem {
        private String name;
        private String price;
        private String features;

        public RoomItem(String name, String price, String features) {
            this.name = name;
            this.price = price;
            this.features = features;
        }

        public String getName() { return name; }
        public String getPrice() { return price; }
        public String getFeatures() { return features; }
    }
}
