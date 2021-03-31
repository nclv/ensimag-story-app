package models;

import java.io.Serializable;
import java.sql.Date;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Invited implements Serializable {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = -8486808310220589932L;
    private long story_id;
    private long user_id;
    private Date date;
}
