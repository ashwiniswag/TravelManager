package com.example.travelmanager.itineary.addtrip;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelmanager.R;
import com.example.travelmanager.database.dao.daoimpl.ProfileDAOImpl;
import com.example.travelmanager.database.dao.daoimpl.TripDAOImpl;
import com.example.travelmanager.database.dto.ProfileDTO;
import com.example.travelmanager.database.dto.TripDTO;

import java.util.ArrayList;
import java.util.List;

public class History_Main extends AppCompatActivity {


    public static HistoryAdapter history_adapter;
    public static List<TripDTO> history_trips = new ArrayList<TripDTO>();
    RecyclerView history_rv;
    String email;
    ProfileDTO profileDTO;
    ProfileDAOImpl profileDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history__main);
        history_rv = findViewById(R.id.history_rv);
        history_rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        history_rv.setLayoutManager(lm);
        profileDTO = new ProfileDTO();
        profileDAO = new ProfileDAOImpl(History_Main.this);
        SharedPreferencesManager preferencesManager = new SharedPreferencesManager(History_Main.this);
        email = preferencesManager.getEmail();
        profileDTO = profileDAO.getProfileByEmail(email);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getHistoryTrips();

    }

    private void getHistoryTrips() {
        new AsyncTask<Void, Void, List<TripDTO>>() {
            @Override
            protected List<TripDTO> doInBackground(Void... params) {
                TripDAOImpl tripDAO = new TripDAOImpl(History_Main.this);
                history_trips = tripDAO.getAllDoneAndCancelledTrips(profileDTO.getProfile_id());

                return history_trips;
            }

            @Override
            protected void onPostExecute(List<TripDTO> list) {
                super.onPostExecute(list);

                history_adapter = new HistoryAdapter(History_Main.this, history_trips);
                history_rv.setAdapter(history_adapter);
                history_adapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
