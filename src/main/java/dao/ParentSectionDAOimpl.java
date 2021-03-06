package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import models.DatabaseFields;
import models.ParentSection;

public class ParentSectionDAOimpl implements ParentSectionDAO {

    private static final Logger LOG = LogManager.getLogger();

    private final static String SQL_INSERT_PARENT_SECTION = "INSERT INTO \"Parent Section\" (\"story_id\", \"para_id\", \"parent_story_id\", \"parent_para_id\", \"parag_condition_story_id\", \"parag_condition_para_id\", \"choice_text\", \"choice_num\") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final static String SQL_FIND_CHILDREN_PARAGRAPHE = "SELECT * FROM \"Parent Section\""
            + "WHERE \"parent_story_id\" = ?" + "AND \"parent_para_id\" = ?" + "ORDER BY \"choice_num\"";

    private Connection connection = null;

    public ParentSectionDAOimpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveParentSection(ParentSection parentSection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_PARENT_SECTION)) {
            preparedStatement.setLong(1, parentSection.getStory_id());
            preparedStatement.setLong(2, parentSection.getParagraphe_id());
            preparedStatement.setLong(3, parentSection.getParent_story_id());
            preparedStatement.setInt(4, parentSection.getParent_paragraphe_id());
            preparedStatement.setLong(5, parentSection.getParagraphe_conditionnel_story_id());
            preparedStatement.setInt(6, parentSection.getParagraphe_conditionnel_id());
            preparedStatement.setString(7, parentSection.getChoice_text());
            preparedStatement.setLong(8, parentSection.getChoice_number());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<ParentSection> findChildrenParagraphe(long storyId, long parentParagrapheId) throws SQLException {
        List<ParentSection> children = new ArrayList<ParentSection>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_CHILDREN_PARAGRAPHE)) {
            preparedStatement.setLong(1, storyId);
            preparedStatement.setLong(2, parentParagrapheId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ParentSection parentSection = null;
                while (resultSet.next()) {
                    parentSection = getParentSection(resultSet);
                    children.add(parentSection);
                }
            }
        }
        return children;
    }

    private ParentSection getParentSection(ResultSet resultSet) throws SQLException {
        return ParentSection.builder().story_id(resultSet.getLong(DatabaseFields.STORY_ID))
                .paragraphe_id(resultSet.getLong(DatabaseFields.PARAGRAPHE_ID))
                .parent_story_id(resultSet.getLong(DatabaseFields.PARENT_SECTION_PARENT_STORY_ID))
                .parent_paragraphe_id(resultSet.getInt(DatabaseFields.PARENT_SECTION_PARENT_PARA_ID))
                .paragraphe_conditionnel_story_id(
                        resultSet.getLong(DatabaseFields.PARENT_SECTION_PARAG_CONDITION_STORY_ID))
                .paragraphe_conditionnel_id(resultSet.getInt(DatabaseFields.PARENT_SECTION_PARAG_CONDITION_PARA_ID))
                .choice_text(resultSet.getString(DatabaseFields.PARENT_SECTION_CHOICE_TEXT))
                .choice_number(resultSet.getLong(DatabaseFields.PARENT_SECTION_CHOICE_NUMBER)).build();
    }
}
