package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import models.DatabaseFields;
import models.Paragraphe;

public class ParagrapheDAOimpl implements ParagrapheDAO {

    private static final Logger LOG = LogManager.getLogger();

    private final static String SQL_FIND_PARAGRAPHE = "SELECT * FROM \"Paragraphe\" WHERE \"story_id\"=? AND \"para_id\"=?";
    private final static String SQL_INSERT_PARAGRAPHE = "INSERT INTO \"Paragraphe\" (\"story_id\", \"user_id\", \"content\", \"is_final\") VALUES (?, ?, ?, ?)";
    private final static String SQL_UPDATE_PARAGRAPHE = "UPDATE \"Paragraphe\" SET \"story_id\"=?, \"user_id\"=?, \"content\"=?, \"is_final\"=? WHERE \"para_id\"=?";
    private final static String SQL_FIND_ALL_STORY_PARAGRAPHES = "SELECT * FROM \"Paragraphe\" WHERE \"story_id\"=?";
    private final static String SQL_FIND_ALL_PARENTS_FROM_FINAL_CHILD = "SELECT para.\"story_id\", para.\"para_id\", para.\"user_id\", para.\"content\", para.\"is_final\" FROM \"Parent Section\" pare LEFT JOIN \"Paragraphe\" para ON para.\"story_id\" = pare.\"story_id\" AND para.\"para_id\" = pare.\"para_id\" WHERE para.\"story_id\"=? START WITH para.\"is_final\" = 1 CONNECT BY NOCYCLE pare.\"para_id\" = prior pare.\"parent_para_id\" ORDER BY para.\"para_id\"";
    private final static String SQL_FIND_ALL_CHILDREN = "SELECT para.\"story_id\", para.\"para_id\", para.\"user_id\", para.\"content\", para.\"is_final\" FROM \"Paragraphe\" para LEFT JOIN \"Parent Section\" pare ON para.\"story_id\" = pare.\"story_id\" AND para.\"para_id\" = pare.\"para_id\" WHERE para.\"story_id\"=? AND pare.\"parent_para_id\"=?";
    private final static String SQL_GET_PARAGRAPHE_ID = "SELECT \"PARAGRAPHE_PARA_ID_SEQ\".currval FROM DUAL";
    private final static String SQL_REMOVE_PARAGRAPHE = "DELETE FROM \"Paragraphe\" WHERE \"story_id\"=? AND \"para_id\"=?";

    private Connection connection = null;

    public ParagrapheDAOimpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long saveParagraphe(Paragraphe paragraphe) throws SQLException {
        long id = -1;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_PARAGRAPHE)) {
            preparedStatement.setLong(1, paragraphe.getStory_id());
            preparedStatement.setLong(2, paragraphe.getUser_id());
            preparedStatement.setString(3, paragraphe.getContent());
            preparedStatement.setInt(4, paragraphe.isLast() ? 1 : 0);

            preparedStatement.executeUpdate();

            try (PreparedStatement ps = connection.prepareStatement(SQL_GET_PARAGRAPHE_ID);
                    ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getLong(1);
                }
            }
        }

        return id;
    }

    @Override
    public Optional<Paragraphe> findParagraphe(long story_id, long paragraphe_id) throws SQLException {
        Paragraphe paragraphe = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_PARAGRAPHE)) {
            preparedStatement.setLong(1, story_id);
            preparedStatement.setLong(2, paragraphe_id);

            paragraphe = getParagraphe(preparedStatement);
        }

        return Optional.ofNullable(paragraphe);
    }

    @Override
    public void updateParagraphe(Paragraphe paragraphe) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_PARAGRAPHE)) {
            preparedStatement.setLong(1, paragraphe.getStory_id());
            preparedStatement.setLong(2, paragraphe.getUser_id());
            preparedStatement.setString(3, paragraphe.getContent());
            preparedStatement.setInt(4, paragraphe.isLast() ? 1 : 0);
            preparedStatement.setLong(5, paragraphe.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void removeParagraphe(Paragraphe paragraphe) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_REMOVE_PARAGRAPHE)) {
            preparedStatement.setLong(1, paragraphe.getStory_id());
            preparedStatement.setLong(2, paragraphe.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Paragraphe> findAllStoryParagraphes(long storyId) throws SQLException {
        List<Paragraphe> paragraphes = new ArrayList<Paragraphe>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_STORY_PARAGRAPHES)) {
            preparedStatement.setLong(1, storyId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Paragraphe paragraphe = null;
                while (resultSet.next()) {
                    paragraphe = getParagraphe(resultSet);
                    paragraphes.add(paragraphe);
                }
            }
        }

        return paragraphes;
    }

    @Override
    public List<Paragraphe> findAllParentsFromFinalChild(long storyId) throws SQLException {
        List<Paragraphe> paragraphes = new ArrayList<Paragraphe>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_PARENTS_FROM_FINAL_CHILD)) {
            preparedStatement.setLong(1, storyId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Paragraphe paragraphe = null;
                while (resultSet.next()) {
                    paragraphe = getParagraphe(resultSet);
                    paragraphes.add(paragraphe);
                }
            }
        }

        return paragraphes;
    }

    @Override
    public List<Paragraphe> findAllChildren(long storyId, long paragrapheId) throws SQLException {
        List<Paragraphe> paragraphes = new ArrayList<Paragraphe>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_CHILDREN)) {
            preparedStatement.setLong(1, storyId);
            preparedStatement.setLong(2, paragrapheId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Paragraphe paragraphe = null;
                while (resultSet.next()) {
                    paragraphe = getParagraphe(resultSet);
                    paragraphes.add(paragraphe);
                }
            }
        }

        return paragraphes;
    }

    private Paragraphe getParagraphe(PreparedStatement preparedStatement) throws SQLException {
        Paragraphe paragraphe = null;

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                paragraphe = getParagraphe(resultSet);
            }
        }

        return paragraphe;
    }

    private Paragraphe getParagraphe(ResultSet resultSet) throws SQLException {
        return Paragraphe.builder().id(resultSet.getLong(DatabaseFields.PARAGRAPHE_ID))
                .story_id(resultSet.getLong(DatabaseFields.STORY_ID)).user_id(resultSet.getLong(DatabaseFields.USER_ID))
                .content(resultSet.getString(DatabaseFields.PARAGRAPHE_CONTENT))
                .last((resultSet.getInt(DatabaseFields.PARAGRAPHE_IS_FINAL) == 1)).validated(true).build();
    }
}
