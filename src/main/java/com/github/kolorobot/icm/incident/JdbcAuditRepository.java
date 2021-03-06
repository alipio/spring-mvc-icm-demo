package com.github.kolorobot.icm.incident;

import com.github.kolorobot.icm.support.date.DateConverter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
class JdbcAuditRepository implements AuditRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcAuditRepository.class);

    private JdbcTemplate jdbcTemplate;

    @Inject
    public JdbcAuditRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Audit save(Audit audit) {
        long incidentId = jdbcTemplate.queryForLong("select id from incident where incident.id = ?", new Object[]{audit.getIncidentId()});
        long auditId = jdbcTemplate.queryForLong("select max(id) from audit") + 1;
        String sql = "insert into audit (id, incident_id, creator_id, description, created, status, previous_status) values (?, ?, ?, ?, ?, ?, ?)";
        LOGGER.debug("Running SQL query: " + sql);
        jdbcTemplate.update(sql,
                new Object[]{auditId, audit.getIncidentId(), audit.getCreatorId(), audit.getDescription(), DateConverter.toDateString(audit.getCreated()), audit.getStatus().ordinal(), audit.getPreviousStatus().ordinal()}
        );
        audit.setId(auditId);
        return audit;
    }

    @Override
    public List<Audit> findAll(Long incidentId) {
        String sql = "select * from audit where incident_id = ?";
        LOGGER.debug("Running SQL query: " + sql);
        List<Audit> audits = jdbcTemplate.query(sql, new Object[]{incidentId}, new AuditMapper());
        return audits == null ? Lists.<Audit>newArrayList() : audits;
    }

    private class AuditMapper implements RowMapper<Audit> {
        @Override
        public Audit mapRow(ResultSet resultSet, int i) throws SQLException {
            Audit audit = new Audit();
            audit.setId(resultSet.getLong("id"));
            audit.setDescription(resultSet.getString("description"));
            audit.setIncidentId(resultSet.getLong("incident_id"));
            audit.setCreated(DateConverter.toDate(resultSet.getString("created")));
            audit.setCreatorId(resultSet.getLong("creator_id"));
            audit.setStatus(Incident.Status.valueOf(resultSet.getInt("status")));
            audit.setPreviousStatus(Incident.Status.valueOf(resultSet.getInt("previous_status")));
            return audit;
        }
    }
}
