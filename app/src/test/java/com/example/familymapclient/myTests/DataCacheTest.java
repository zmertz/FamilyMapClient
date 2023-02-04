package com.example.familymapclient.myTests;

import com.example.familymapclient.DataCache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import Model.Event;
import Model.Person;

public class DataCacheTest {

    DataCache cache = new DataCache();

    @Before
    public void setUp() {
        Event e1 = new Event("1", "superUser", "1", 1, 1, "USA", "Provo", "Birth", 2001);
        Event e2 = new Event("2", "superUser", "1", 1, 1, "USA", "Provo", "Baptism", 2011);
        Event e3 = new Event("3", "superUser", "1", 1, 1, "USA", "Provo", "Marriage", 2021);
        Event e4 = new Event("4", "superUser", "1", 1, 1, "USA", "Provo", "Death", 2031);
        Event e5 = new Event("5", "superUser", "1", 1, 1, "USA", "Provo", "Marriage", 1990);

        Person p1 = new Person("1", "superUser", "super", "user", "m", "father1", "mother1", "spouse1");
        Person p2 = new Person("2", "superUser", "super2", "user2", "m", "father2", "mother2", "spouse2");
        Person p3 = new Person("3", "superUser", "super3", "user3", "m", "father3", "mother3", "spouse3");
        Person p4 = new Person("4", "superUser", "super4", "user4", "m", "father4", "mother4", "spouse4");

        ArrayList<Event> eventArray = new ArrayList<Event>() {
            { add(e1); add(e2); add(e3); add(e4); add(e5);}
        };
        Person[] personArray = new Person[]{p1,p2,p3,p4};

        cache.addFamily(personArray);
        cache.addAllEvents(eventArray);

    }

    @Test
    public void setAndGetFamily() {
        ArrayList<Person> family = cache.getFamily();
        Assert.assertNotNull(family);
        Assert.assertEquals(4, family.size());

        Person testPerson = new Person("1", "superUser", "super", "user", "m", "father1", "mother1", "spouse1");
        Assert.assertEquals(testPerson.getAssociatedUserName(), family.get(0).getAssociatedUserName());
        Assert.assertEquals(testPerson, family.get(0));

        Person testPerson2 = new Person("2", "superUser", "super2", "user2", "m", "father2", "mother2", "spouse2");
        Assert.assertEquals(testPerson2, family.get(1));

        Person testPerson3 = new Person("3", "superUser", "super3", "user3", "m", "father3", "mother3", "spouse3");
        Assert.assertEquals(testPerson3, family.get(2));

        Person testPerson4 = new Person("4", "superUser", "super4", "user4", "m", "father4", "mother4", "spouse4");
        Assert.assertEquals(testPerson4, family.get(3));
    }

    @Test
    public void setAndGetEvents() {
        ArrayList<Event> allEvents = cache.getAllEvents();
        Assert.assertNotNull(allEvents);
        Assert.assertEquals(5, allEvents.size());

        Event testEvent = new Event("1", "superUser", "1", 1, 1, "USA", "Provo", "Birth", 2001);
        Event testEvent2 = new Event("2", "superUser", "1", 1, 1, "USA", "Provo", "Baptism", 2011);
        Event testEvent3 = new Event("3", "superUser", "1", 1, 1, "USA", "Provo", "Marriage", 2021);
        Event testEvent4 = new Event("4", "superUser", "1", 1, 1, "USA", "Provo", "Death", 2031);

        Assert.assertEquals(testEvent, allEvents.get(0));
        Assert.assertEquals(testEvent2, allEvents.get(1));
        Assert.assertEquals(testEvent3, allEvents.get(2));
        Assert.assertEquals(testEvent4, allEvents.get(3));
    }

    @Test
    public void sortEventTest() {
        List<Event> testList = cache.getAllEvents();
        List<Event> sortedList = cache.sortEvents(testList);
        Assert.assertNotNull(sortedList);

        Assert.assertTrue(testList.get(4).getYear() < testList.get(3).getYear());
        Assert.assertEquals(1990, testList.get(4).getYear());
        Assert.assertEquals(1990, sortedList.get(0).getYear());

    }
}
