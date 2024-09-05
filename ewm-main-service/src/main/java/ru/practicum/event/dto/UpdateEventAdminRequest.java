package ru.practicum.event.dto;

import lombok.Data;

@Data
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private StateAction stateAction;

    public enum StateAction {
        PUBLISH_EVENT,
        REJECT_EVENT
    }

}
