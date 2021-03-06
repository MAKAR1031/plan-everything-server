package ru.migmak.planeverything.server.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

@Getter
@AllArgsConstructor
public class TaskUpdateInfo extends ResourceSupport {
    private Date createTime;
    private Date updateTime;
    private Date finishTime;
}
