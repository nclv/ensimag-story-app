package models;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ParentSection implements Serializable {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = -2887008466027793578L;
    private long story_id;
    private long paragraphe_id;
    private long parent_story_id;
    private int parent_paragraphe_id;
    private long paragraphe_conditionnel_story_id;
    private int paragraphe_conditionnel_id;
    private String choice_text;
    private long choice_number;
}
