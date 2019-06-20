package no.fint;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DoorRecyclerViewAdapter extends RecyclerView.Adapter<DoorRecyclerViewAdapter.DoorViewHolder> {
    ArrayList<Door> doors;
    Context context;

    DoorRecyclerViewAdapter(ArrayList<Door> doors, Context context) {
        this.doors = doors;
        this.context = context;
    }

    public static class DoorViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView doorName;
        TextView doorID;
        ImageView doorPhoto;
        Button openDoor;

        DoorViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.doors_card_view);
            doorName = itemView.findViewById(R.id.door_name);
            doorID = itemView.findViewById(R.id.door_id);
            doorPhoto = itemView.findViewById(R.id.door_photo);
            openDoor = itemView.findViewById(R.id.button_open_door);
        }
    }

    @NonNull
    @Override
    public DoorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.doors_to_open_cardview, viewGroup, false);
        DoorViewHolder doorHolderView = new DoorViewHolder(v);
        return doorHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull final DoorViewHolder doorViewHolder, final int i) {
        doorViewHolder.doorName.setText(doors.get(i).getName());
        doorViewHolder.doorID.setText(doors.get(i).getDoorID());
        doorViewHolder.doorPhoto.setImageResource(doors.get(i).getPictureOfDoorID());
        doorViewHolder.openDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openDoorIntent = new Intent(context.getApplicationContext(), MainActivity.class);
                openDoorIntent.putExtra("openedDoor", doors.get(i).getName());
                context.startActivity(openDoorIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doors.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
