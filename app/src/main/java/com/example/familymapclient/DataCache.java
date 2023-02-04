package com.example.familymapclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Model.Event;
import Model.Person;

public class DataCache {

    private static DataCache cache;
    private Person user;
    private Map<String, Person> people;
    private Map<String, Event> events;
    private String authToken;
    private Event eventActivitySelectedEvent;

    public DataCache() {
        people = new HashMap<>();
        events = new HashMap<>();
    }

    public static DataCache getInstance() {
        if (cache == null) {
            cache = new DataCache();
        }
        return cache;
    }

    public static void setCache(DataCache cache) {   //pass in null to clear
        DataCache.cache = cache;
    }

    public void addPerson(Person person) {
        people.put(person.personID, person);
    }

    public void addFamily(Person[] family) {
        for (int i = 0; i < family.length; i++) {
            people.put(family[i].personID, family[i]);
        }
    }

    public Person getPerson(String personID) {
        Person person = null;
        person = people.get(personID);
        return person;
    }

    public void addEvent(Event event) {
        events.put(event.eventID, event);
    }

    public void addAllEvents(ArrayList<Event> allEvents) {
        for (int i = 0; i < allEvents.size(); i++) {
            events.put(allEvents.get(i).eventID, allEvents.get(i));
        }
    }

    public Event getEvent(String eventID) {
        Event event = null;
        event = events.get(eventID);
        return event;
    }

    public ArrayList<Event> getEventsFromPerson(String personID) {
        ArrayList<Event> returnEvents = new ArrayList<>();
        ArrayList<Event> eventArray = getAllEvents();
        for(int i = 0; i < eventArray.size(); i++) {
            if(eventArray.get(i).getPersonID().equals(personID)) {
                returnEvents.add(eventArray.get(i));
            }
        }
        return returnEvents;
    }

    public ArrayList<Person> getFamily() {
        ArrayList<Person> family = new ArrayList<>();
        Set<String> keys = people.keySet();
        for(String key : keys) {
            family.add(people.get(key));
        }
        return family;
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> allEvents = new ArrayList<>();
        Set<String> keys = events.keySet();
        for(String key: keys) {
            allEvents.add(events.get(key));
        }
        return allEvents;
    }

    public Map<String, Event> getEventMap() {
        return events;
    }

    public Map<String, Person> getPersonMap() {
        return people;
    }

    public Event getRelationForLine(Event event, String relation) {
        Person person = getPerson(event.personID);
        ArrayList<Event> eventArray = getAllEvents();
        String relationID = null;
        if (relation.equals("Spouse")) {
            relationID = person.spouseID;
        } else if (relation.equals("Father")) {
            relationID = person.fatherID;
        } else if (relation.equals("Mother")) {
            relationID = person.motherID;
        } else {
            relationID = person.getPersonID();
        }
        Event eventRelation = null;
        if (relationID != null) {
            for (int i = 0; i < eventArray.size(); i++) {
                if (eventArray.get(i).getPersonID().equals(relationID)) {
                    if(eventRelation == null) {
                        eventRelation = eventArray.get(i);
                    }
                    if (eventArray.get(i).getYear() < eventRelation.getYear()) {
                        eventRelation = eventArray.get(i);
                    }
                }
            }
        }
        return eventRelation;
    }

    public List<Person> findRelatives(String personID) {
        Person person = getPerson(personID);
        List<Person> personArray = getFamily();
        List<Person> returnArray = new ArrayList<>();

        for(int i = 0; i < personArray.size(); i++) {
            if(personArray.get(i).getPersonID().equals(person.getSpouseID())){
                returnArray.add(personArray.get(i));
            } else if(personArray.get(i).getPersonID().equals(person.getFatherID())) {
                returnArray.add(personArray.get(i));
            } else if(personArray.get(i).getPersonID().equals(person.getMotherID())) {
                returnArray.add(personArray.get(i));
            }
            if(personArray.get(i).getMotherID() != null) {
                    if(personArray.get(i).getMotherID().equals(personID)) {
                        returnArray.add(personArray.get(i));
                    }
            }
            if (personArray.get(i).getFatherID() != null) {
                if(personArray.get(i).getFatherID().equals(personID)) {
                    returnArray.add(personArray.get(i));
                }
            }
        }
        return returnArray;
    }

    public List<Event> sortEvents(List<Event> eventList) {
        List<Event> sortedList = new ArrayList<>();
        List<Event> currentList = new ArrayList<>(eventList);

        while(currentList.size() > 0) {
            Event event = currentList.get(0);
            int index = 0;
            for(int i = 0; i < currentList.size(); i++) {
                if(currentList.get(i).getYear() < event.getYear()) {
                    event = currentList.get(i);
                    index = i;
                }
            }
            sortedList.add(event);
            currentList.remove(index);
        }
        return sortedList;
    }

    public Event getEventActivitySelectedEvent() {
        return eventActivitySelectedEvent;
    }

    public void setEventActivitySelectedEvent(Event eventActivitySelectedEvent) {
        this.eventActivitySelectedEvent = eventActivitySelectedEvent;
    }
}
