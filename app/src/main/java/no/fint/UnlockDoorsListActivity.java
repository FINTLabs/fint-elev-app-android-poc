package no.fint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class UnlockDoorsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_for_doors);
        getSupportActionBar().setTitle("Lås opp dører");
        ArrayList<Door> doors = createDoors();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_for_doors);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DoorRecyclerViewAdapter doorRecyclerViewAdapter = new DoorRecyclerViewAdapter(doors, this);
        recyclerView.setAdapter(doorRecyclerViewAdapter);
    }

    private ArrayList<Door> createDoors() {
        ArrayList<Door> doors = new ArrayList<Door>();
        doors.add(Door.builder().name("Hovedinngang").doorID("1001").pictureOfDoorID(R.drawable.school_main_door).build());
        doors.add(Door.builder().name("Bilbliotekdør").doorID("1002").pictureOfDoorID(R.drawable.library_door).build());
        doors.add(Door.builder().name("Kantinedør").doorID("1003").pictureOfDoorID(R.drawable.cafeteria_door).build());
        return doors;
    }
}
