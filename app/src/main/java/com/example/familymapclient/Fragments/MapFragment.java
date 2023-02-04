package com.example.familymapclient.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionHelper;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.familymapclient.DataCache;
import com.example.familymapclient.PersonActivity;
import com.example.familymapclient.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;

public class MapFragment extends Fragment implements  OnMapReadyCallback {

    private GoogleMap map;
    public DataCache cache = DataCache.getInstance();
    private Map<String, Person> personMap = cache.getPersonMap();
    private Map<String, Event> eventMap = cache.getEventMap();
    private ArrayList<Person> people = cache.getFamily();
    private ArrayList<Event> allEvents = cache.getAllEvents();
    private List<Polyline> lines = new ArrayList<>();
    private LinearLayout linearLayout;
    private TextView name = null;
    private TextView eventDetails = null;
    private ImageView gender = null;
    private Event selectedEvent = null;
    private Marker selectedMarker = null;
    private boolean eventActivitySelected = false;
    private Map<String, Float> colorMap = new HashMap<>();
    private Float colorCounter = 0f;

    public MapFragment() {}

    public MapFragment(String eventID) {
        if (eventID != null) {
            eventActivitySelected = true;
        }
        else {
            eventActivitySelected = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        name = (TextView) view.findViewById(R.id.person_name);
        eventDetails = (TextView) view.findViewById(R.id.event_details);
        gender = (ImageView) view.findViewById(R.id.gender_icon);
        linearLayout = (LinearLayout) view.findViewById(R.id.event_bar);
        linearLayout.setTag("");

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }


    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        map = googleMap;
        map.clear();
        drawMarkers();
        setMarkerListener();

    }

    private void drawMarkers() {
        for (Event currEvent : allEvents) {
            setMarkerDetails(currEvent);
        }
    }

    private void setMarkerDetails(Event event) {
        LatLng pos = new LatLng(event.latitude, event.longitude);
        boolean fromEventActivity = false;
        selectedEvent = cache.getEventActivitySelectedEvent();
        Marker marker = null;

        if(event.getEventType().toLowerCase().equals("birth")) {
            marker = map.addMarker(new MarkerOptions().position(pos)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title("Birth"));
            marker.setTag(event);
        } else if(event.getEventType().toLowerCase().equals("baptism")) {
            marker = map.addMarker(new MarkerOptions().position(pos)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                    .title("Baptism"));
            marker.setTag(event);
        } else if(event.getEventType().toLowerCase().equals("marriage")) {
            marker = map.addMarker(new MarkerOptions().position(pos)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                    .title("Marriage"));
            marker.setTag(event);
        } else if(event.getEventType().toLowerCase().equals("death")) {
            marker = map.addMarker(new MarkerOptions().position(pos)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title("Death"));
            marker.setTag(event);
        } else {
            if(colorMap.get(event.getEventType().toLowerCase()) == null) {
                colorMap.put(event.getEventType().toLowerCase(), colorCounter+30);
                colorCounter+=30;
            }
            Float color = colorMap.get(event.getEventType().toLowerCase());
            marker = map.addMarker(new MarkerOptions().position(pos)
                    .icon(BitmapDescriptorFactory.defaultMarker(color))
                    .title(event.getEventType()));
            marker.setTag(event);
        }
        if(cache.getEventActivitySelectedEvent() == event) {
           fromEventActivity = true;
           selectedMarker = marker;
        }
        if(fromEventActivity && eventActivitySelected) {
            map.animateCamera(CameraUpdateFactory.newLatLng(pos));
            markerClicked(selectedMarker);
        }
    }

    private void drawLifeEvents(Event event, int width) {
        List<Event> eventArray = cache.getEventsFromPerson(event.getPersonID());
        eventArray = cache.sortEvents(eventArray);
        LatLng[] lats = new LatLng[eventArray.size()];
        for(int i = 0; i < eventArray.size(); i++) {
            lats[i] = new LatLng(eventArray.get(i).getLatitude(), eventArray.get(i).getLongitude());
        }
        LatLng firstLat = lats[0];
        for(int i = 0; i < lats.length; i++) {
            drawLine(firstLat, lats[i], width, Color.BLUE);
            firstLat = lats[i];
        }
    }

    private void drawMarriageLine(Event event) {
        LatLng pos = new LatLng(event.getLatitude(), event.getLongitude());
        Event spouseMarriage = cache.getRelationForLine(event, "Spouse");
        if(spouseMarriage != null) {
            LatLng marriage = new LatLng(spouseMarriage.getLatitude(), spouseMarriage.getLongitude());
            drawLine(pos, marriage, 8, Color.YELLOW);
        }
    }

    private void drawAncestorLine(Event event, int width) {
        drawParentLine(event, width);
        Person currPerson = cache.getPerson(event.getPersonID());

        if (currPerson.getFatherID() != null) {
            Event fatherEvent = cache.getRelationForLine(event,"Father");

            int newWidth = width - 1;
            if(newWidth < 1) {
                newWidth = 1;
            }
            if(fatherEvent != null) {
                drawAncestorLine(fatherEvent, newWidth);
            }
        }
        if(currPerson.getMotherID() != null) {
            Event motherEvent = cache.getRelationForLine(event, "Mother");
            int newWidth = width - 1;
            if(newWidth < 1) {
                newWidth = 1;
            }
            if(motherEvent != null) {
                drawAncestorLine(motherEvent, newWidth);
            }
        }
    }

    private void drawParentLine(Event event, int width) {
        LatLng pos = new LatLng(event.getLatitude(), event.getLongitude());
        Event fatherEvent = cache.getRelationForLine(event, "Father");
        Event motherEvent = cache.getRelationForLine(event, "Mother");

        if(fatherEvent != null) {
            LatLng fatherPos = new LatLng(fatherEvent.getLatitude(), fatherEvent.getLongitude());
            drawLine(pos, fatherPos, width, Color.BLACK);
        }
        if(motherEvent != null) {
            LatLng motherPos = new LatLng(motherEvent.getLatitude(), motherEvent.getLongitude());
            drawLine(pos, motherPos, width, Color.BLACK);
        }
    }

    private void drawLine(LatLng pos1, LatLng pos2, int width, int color) {
        PolylineOptions addLine = new PolylineOptions().add(pos1, pos2).color(color).width(width);
        lines.add(map.addPolyline(addLine));
    }


    private void setMarkerListener() {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                markerClicked(marker);
                return true;
            }
        });
    }
    View.OnClickListener onClickDetails = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent in = new Intent(getActivity(), PersonActivity.class);
            Person p = cache.getPerson(selectedEvent.getPersonID());
            String personID = p.getPersonID();
            in.putExtra("personID", personID);
            startActivity(in);
        }
    };

    private void markerClicked (Marker marker) {
        for(Polyline line : lines) {
            line.remove();
        }
        lines.clear();
        selectedEvent = (Event) marker.getTag();
        Person person = cache.getPerson(selectedEvent.getPersonID());

        linearLayout.setTag(person.getPersonID());
        LatLng pos = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLng(pos));
        name.setText(new StringBuilder().append(person.getFirstName()).append(" ")
                .append(person.getLastName()).toString());

        eventDetails.setText(new StringBuilder().append(selectedEvent.getEventType()).append(": ")
                .append(selectedEvent.getCity()).append(", ").append(selectedEvent.getCountry()).append(" (")
                .append(selectedEvent.getYear()).append(")").toString());

        if(person.getGender().equals("m")) {
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                    colorRes(R.color.male_icon).sizeDp(40);
            gender.setImageDrawable(genderIcon);
        } else {
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                    colorRes(R.color.female_icon).sizeDp(40);
            gender.setImageDrawable(genderIcon);
        }
        name.setOnClickListener(onClickDetails);
        eventDetails.setOnClickListener(onClickDetails);
        drawMarriageLine(selectedEvent);
        drawAncestorLine(selectedEvent,8);
        drawLifeEvents(selectedEvent,8);
    }
}