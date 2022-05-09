package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        boolean itNewSave = false;

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            itNewSave = true;
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        setOrUpdRoles(user, itNewSave);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User result = DataAccessUtils.singleResult(users);
        if (result != null) {
            result.setRoles(getRoles(id));
        }
        return result;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User result = DataAccessUtils.singleResult(users);
        result.setRoles(getRoles(result.getId()));
        return result;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        Map<Integer, List<Role>> roles = jdbcTemplate.query("SELECT * FROM user_roles", rs -> {
            Map<Integer, List<Role>> result = new LinkedHashMap<>();
            while (rs.next()) {
                Integer user_id = Integer.parseInt(rs.getString("user_id"));
                result.putIfAbsent(user_id, new ArrayList<>());
                result.get(user_id).add(Role.valueOf(rs.getString("role")));
            }
            return result;
        });

        return users.stream().map(user -> {
            Integer id = user.getId();
            if (roles.get(id) != null) {
                Set<Role> setR = new LinkedHashSet<>();
                for (Role r : roles.get(id)) {
                    setR.add(r);
                }
                user.setRoles(setR);
            }else {
                user.setRoles(new ArrayList<>());
            }
            return user;
        }).toList();
    }

    private List<Role> getRoles(int id) {
        return jdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=?", Role.class, id);
    }

    private void setOrUpdRoles(User user, boolean save) {
        String sqlInsert = "INSERT INTO user_roles (role, user_id) VALUES (?,?)";
        String sqlUpdate = "UPDATE user_roles SET role = ? WHERE user_id = ?";

        jdbcTemplate.batchUpdate(save ? sqlInsert : sqlUpdate, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(2, user.getId());
                ps.setString(1, String.valueOf(user.getRoles().toArray()[i]));
            }

            @Override
            public int getBatchSize() {
                return user.getRoles().size();
            }
        });
    }
}
