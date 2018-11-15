package io.pivotal.pal.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder key = new GeneratedKeyHolder();

        String insertQuery = "INSERT INTO time_entries(project_id, user_id, date, hours) values (?,?,?,?)";

        this.jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertQuery, new String[] {"id"});
            ps.setLong(1, timeEntry.getProjectId());
            ps.setLong(2, timeEntry.getUserId());
            ps.setDate(3, Date.valueOf(timeEntry.getDate()));
            ps.setInt(4, timeEntry.getHours());
            return ps;
        }, key);

        return findTimeEntryById(key.getKey().longValue());
    }

    private TimeEntry findTimeEntryById(long id) {
        String retrieveQuery = "SELECT * FROM time_entries WHERE id=" + id;

        try {
            return this.jdbcTemplate.queryForObject(retrieveQuery, (rs, rowNum) -> {
                return setTimeEntryByResultSet(rs);
            });
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }

    }

    private TimeEntry setTimeEntryByResultSet(ResultSet rs) throws SQLException {
        TimeEntry entry = new TimeEntry();
        entry.setId(rs.getInt(1));
        entry.setProjectId(rs.getInt(2));
        entry.setUserId(rs.getInt(3));
        entry.setDate(LocalDate.parse(rs.getString(4)));
        entry.setHours(rs.getInt(5));
        return entry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return findTimeEntryById(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        String retrieveQuery = "SELECT * FROM time_entries";
        return this.jdbcTemplate.query(retrieveQuery, new Object[]{}, (rs, rowNum) -> {
            return setTimeEntryByResultSet(rs);
        });
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        String query = "UPDATE time_entries SET project_id = ?, user_id = ?, date = ?, hours = ? WHERE id = ?";

        this.jdbcTemplate.update(
                query,
                timeEntry.getProjectId(), timeEntry.getUserId(), Date.valueOf(timeEntry.getDate()), timeEntry.getHours(), id);

        return findTimeEntryById(id);
    }

    @Override
    public void delete(long timeEntryId) {
        this.jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", timeEntryId);
    }
}
