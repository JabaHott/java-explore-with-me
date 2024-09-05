package ru.practicum.event.dto;

import lombok.Data;

@Data
public class UpdateEventUserRequest extends UpdateEventRequest {
    private StateAction stateAction;

    public enum StateAction {
        SEND_TO_REVIEW,
        CANCEL_REVIEW
    }

}
