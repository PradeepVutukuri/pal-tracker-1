package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    Map<Long, TimeEntry> timeEntryMap = new HashMap<>();
    private Long timeEntryId = 1L;

    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(timeEntryId);
        timeEntryMap.put(timeEntryId, timeEntry);
        timeEntryId++;
        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return timeEntryMap.get(timeEntryId);
    }

    public List<TimeEntry> list() {
        return timeEntryMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        timeEntry.setId(id);
        timeEntryMap.put(id, timeEntry);
        return timeEntry;
    }

    public void delete(long id) {
        timeEntryMap.remove(id);
    }
}
