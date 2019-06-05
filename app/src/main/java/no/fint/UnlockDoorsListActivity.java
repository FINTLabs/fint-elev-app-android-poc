package no.fint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class UnlockDoorsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_for_doors);
        ArrayList<Door> doors = createDoors();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view_for_doors);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DoorRecyclerViewAdapter doorRecyclerViewAdapter = new DoorRecyclerViewAdapter(doors, this);
        recyclerView.setAdapter(doorRecyclerViewAdapter);
    }

    private ArrayList<Door> createDoors() {
        ArrayList<Door> doors = new ArrayList<Door>();
        doors.add(new Door("Hovedinngang", "1001", R.drawable.school_main_door));
        doors.add(new Door("Bilbliotekdør","1002", R.drawable.library_door));
        doors.add(new Door("Kantinedør","1003", R.drawable.cafeteria_door));
        return doors;
    }
}
