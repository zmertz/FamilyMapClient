package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.Event;
import Model.Person;

public class PersonActivity extends AppCompatActivity {

    private Activity activity = this;
    private DataCache cache = DataCache.getInstance();
    private Person person = null;
    private ArrayList<Event> eventArray = new ArrayList<>();
    private TextView firstName;
    private TextView lastName;
    private TextView gender;

    private ExpandableListAdapter listAdapter;
    private HashMap<String, List<String>> eventListDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Intent intent = getIntent();
        String personID = intent.getStringExtra("personID");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Family Map: Person Details");

        person = cache.getPerson(personID);
        eventArray = cache.getEventsFromPerson(personID);

        firstName = (TextView) findViewById(R.id.first_name);
        lastName = (TextView) findViewById(R.id.last_name);
        gender = (TextView) findViewById(R.id.gender);
        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
        if (person.getGender().equals("m")) {
            gender.setText("Male");
        } else {
            gender.setText("Female");
        }

        ExpandableListView expandableListView = findViewById(R.id.expandable_list_view);
        List<Event> eventList = cache.getEventsFromPerson(personID);
        eventList = cache.sortEvents(eventList);
        List<Person> familyList = cache.findRelatives(personID);

        listAdapter = new ExpandableListAdapter(eventList, familyList, person);
        expandableListView.setAdapter(listAdapter);


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if(i == 1) {
                    Intent intent1 = new Intent(PersonActivity.this, PersonActivity.class);
                    Person p = (Person) listAdapter.getChild(i, i1);
                    String personID = p.getPersonID();
                    intent1.putExtra("personID", personID);
                    startActivity(intent1);
                } else if(i == 0) {
                    Intent intent1 = new Intent(PersonActivity.this, EventActivity.class);
                    Event e = (Event) listAdapter.getChild(i, i1);
                    String eventID = e.getEventID();
                    cache.setEventActivitySelectedEvent(e);
                    intent1.putExtra("eventID", eventID);
                    startActivity(intent1);
                }
                return false;
            }
        });
    }

    //Up button here
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PersonActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //this.finish();
                startActivity(intent);
                return true;
        }
        return PersonActivity.super.onOptionsItemSelected(item);
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private final List<Event> eventList;
        private final List<Person> familyList;
        private final Person currentPerson;

        ExpandableListAdapter(List<Event> eventList, List<Person> familyList, Person person) {
            this.eventList = eventList;
            this.familyList = familyList;
            this.currentPerson = person;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int i) {
            switch (i) {
                case 0:
                    return eventList.size();
                case 1:
                    return familyList.size();
            }
            return 0;
        }

        @Override
        public Object getGroup(int i) {
            switch (i) {
                case 0:
                    return "Life Events";
                case 1:
                    return "Family";
            }
            return null;
        }

        @Override
        public Object getChild(int i, int i1) {
            switch (i) {
                case 0:
                    return eventList.get(i1);
                case 1:
                    return familyList.get(i1);
            }
            return null;
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            if(view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_header, viewGroup, false);
            }
            TextView titleView = view.findViewById(R.id.list_title);
            titleView.setText((String) getGroup(i));

            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            View itemView = view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_children, viewGroup, false);
            ImageView icon = view.findViewById(R.id.list_icon);

            if(i == 0) {
                Event currEvent = (Event) getChild(i,i1);
                Drawable eventIcon = new IconDrawable(activity, FontAwesomeIcons.fa_map_marker).
                        colorRes(R.color.teal_200).sizeDp(40);
                icon.setImageDrawable(eventIcon);
                initializeView(itemView, currEvent);
            } else {
                Person currPerson = (Person) getChild(i,i1);
                if(currPerson.getGender().equals("m")) {
                    Drawable genderIcon = new IconDrawable(activity, FontAwesomeIcons.fa_male).
                            colorRes(R.color.male_icon).sizeDp(40);
                    icon.setImageDrawable(genderIcon);
                    initializeView(itemView, currPerson);
                } else {
                    Drawable genderIcon = new IconDrawable(activity, FontAwesomeIcons.fa_female).
                            colorRes(R.color.female_icon).sizeDp(40);
                    icon.setImageDrawable(genderIcon);
                    initializeView(itemView, currPerson);
                }
            }
            return itemView;
        }


        private void initializeView(View view, Person person) {
            TextView listInfo = view.findViewById(R.id.list_info);
            TextView listInfo2 = view.findViewById(R.id.list_info2);

            String info = person.getFirstName() + " " + person.getLastName();
            listInfo.setText(info);

            String relation = null;
            if(currentPerson.getSpouseID() != null) {
                if (currentPerson.getSpouseID().equals(person.getPersonID())) {
                    relation = "Spouse";
                }
            }
            if(person.getFatherID()!= null && person.getMotherID() != null) {
                if(person.getMotherID().equals(currentPerson.getPersonID()) ||
                person.getFatherID().equals(currentPerson.getPersonID())) {
                    relation = "Child";
                }
            }
            if (currentPerson.getFatherID() != null && currentPerson.getMotherID() != null) {
                if(currentPerson.getFatherID().equals(person.getPersonID())) {
                    relation = "Father";
                } else if(currentPerson.getMotherID().equals(person.getPersonID())) {
                    relation = "Mother";
                }
            }
            listInfo2.setText(relation);
        }

        private void initializeView(View view, Event event) {
            TextView listInfo = view.findViewById(R.id.list_info);
            TextView listInfo2 = view.findViewById(R.id.list_info2);

            String info = event.getEventType()+ ", " + event.getCity()+ ", "+ event.getCountry()+
                    " "+ event.getYear();
            listInfo.setText(info);

            Person p = cache.getPerson(event.getPersonID());
            String personInfo = p.getFirstName()+ " " +p.getLastName();
            listInfo2.setText(personInfo);
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    };
}
