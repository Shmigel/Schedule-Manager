package com.shmigel.scheduleManager.dialogflow.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shmigel.scheduleManager.dialogflow.model.response.message.Message;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class RichResponse {

    private List<Message> items = new ArrayList<>();

    private List<Suggestion> suggestions = new ArrayList<>();

    public RichResponse() {
    }

    @JsonIgnore
    public RichResponse addMessage(Message m) {
        items.add(m);
        return this;
    }

    @JsonIgnore
    public RichResponse addSuggestion(Suggestion s) {
        suggestions.add(s);
        return this;
    }

    @JsonIgnore
    public DialogflowResponse of(Message m) {
        items.addAll(Collections.singletonList(m));
        return build();
    }

    @JsonIgnore
    public DialogflowResponse build() {
        return withDefaultResponse("Something was going wrong. Please try later");
    }

    @JsonIgnore
    public DialogflowResponse withDefaultResponse(String defaultResponse) {
        return new DialogflowResponse("", new Payload(new Platform(this)));
    }
}
