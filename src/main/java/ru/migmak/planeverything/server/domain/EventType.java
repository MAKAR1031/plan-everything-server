package ru.migmak.planeverything.server.domain;

import lombok.Getter;
import lombok.Setter;
import ru.migmak.planeverything.server.domain.enums.EventTypeCode;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "event_types")
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    boolean isCreate() {
        return code.equals(EventTypeCode.CREATE.name());
    }

    boolean isUpdate() {
        return code.equals(EventTypeCode.UPDATE.name());
    }

    boolean isFinish() {
        return code.equals(EventTypeCode.FINISH.name());
    }
}
