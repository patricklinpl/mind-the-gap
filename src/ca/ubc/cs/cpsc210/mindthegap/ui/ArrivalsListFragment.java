package ca.ubc.cs.cpsc210.mindthegap.ui;

/*
 * Copyright 2015-2016 Department of Computer Science UBC
 */

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.ubc.cs.cpsc210.mindthegap.R;
import ca.ubc.cs.cpsc210.mindthegap.model.Arrival;
import ca.ubc.cs.cpsc210.mindthegap.model.ArrivalBoard;
import ca.ubc.cs.cpsc210.mindthegap.model.Station;
import ca.ubc.cs.cpsc210.mindthegap.model.StationManager;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Fragment to display list of arrivals at selected station for particular line
 * and particular platform (as specified by travel direction).
 */
public class ArrivalsListFragment extends ListFragment {

    private StationManager stnManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        String lineId = bundle.getString(getString(R.string.line_id_key));
        String travelDirn = bundle.getString(getString(R.string.travel_dirn_key));

        stnManager = StationManager.getInstance();

        ArrayList<Arrival> arrivals = getArrivalsForSelectedStationOnLineInDirection(lineId, travelDirn);
        ArrivalsAdapter adapter = new ArrivalsAdapter(arrivals);

        setListAdapter(adapter);
    }

    /**
     * Get arrivals for selected station on line with given ID at platform with given travel direction
     *
     * @param lineId   the line id
     * @param travelDirn   the travel direction
     * @return   list of arrivals at selected station on given line in given travel direction
     */
    public ArrayList<Arrival> getArrivalsForSelectedStationOnLineInDirection(String lineId, String travelDirn) {
        ArrayList<Arrival> listofArival = new ArrayList<>();
        Station currentStation = stnManager.getSelected();
        Iterator abs = currentStation.iterator();

        while (abs.hasNext()) {
            ArrivalBoard currentArrivalBoard = (ArrivalBoard) abs.next();
            if (currentArrivalBoard.getLine().getId().equals(lineId)) {
                for (Arrival next2 : currentArrivalBoard) {
                    if (next2.getTravelDirn().equals(travelDirn)) {
                        listofArival.add(next2);
                    }
                }
            }
        }
        return listofArival;
    }


    /**
     * Array adapter for list of arrivals displayed to user
     */
    private class ArrivalsAdapter extends ArrayAdapter<Arrival> {
        public ArrivalsAdapter(ArrayList<Arrival> arrivals) {
            super(getActivity(), 0, arrivals);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.arrival_item, null);
            }

            Arrival arrival = getItem(position);
            TextView destination = (TextView) convertView.findViewById(R.id.destination);
            destination.setText(arrival.getDestination());
            TextView platform = (TextView) convertView.findViewById(R.id.platform);
            platform.setText(arrival.getPlatform());
            TextView waitTime = (TextView) convertView.findViewById(R.id.wait_time);
            waitTime.setText(Integer.toString(arrival.getTimeToStationInMins()) + " mins");

            return convertView;
        }
    }
}
