package no.fint;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DoorRecyclerViewAdapter extends RecyclerView.Adapter<DoorRecyclerViewAdapter.DoorViewHolder> {
    ArrayList<Door> doors;
    DoorRecyclerViewAdapter(ArrayList<Door> doors){
        this.doors = doors;
    }
    public static class DoorViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView doorName;
        TextView doorID;
        ImageView doorPhoto;

        DoorViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.doors_card_view);
            doorName = (TextView)itemView.findViewById(R.id.door_name);
            doorID = (TextView)itemView.findViewById(R.id.door_id);
            doorPhoto = (ImageView)itemView.findViewById(R.id.door_photo);
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
    public void onBindViewHolder(@NonNull DoorViewHolder doorViewHolder, int i) {
        doorViewHolder.doorName.setText(doors.get(i).getName());
        doorViewHolder.doorID.setText(doors.get(i).getDoorID());
        doorViewHolder.doorPhoto.setImageResource(doors.get(i).getPictureOfDoor());
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
