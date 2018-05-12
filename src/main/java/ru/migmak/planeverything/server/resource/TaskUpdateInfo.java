package ru.migmak.planeverything.server.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class TaskUpdateInfo extends ResourceSupport {
    private Date createTime;
    private Date updateTime;
    private Date finishTime;

    public static TaskUpdateInfo of(Date createTime, Date updateTime, Date finishTime) {
        return new TaskUpdateInfo(createTime, updateTime, finishTime);
    }
}
