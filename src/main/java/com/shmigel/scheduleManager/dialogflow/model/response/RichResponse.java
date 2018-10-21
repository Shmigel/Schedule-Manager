package com.shmigel.scheduleManager.dialogflow.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RichResponse {

    private List<ResponseElement> items;

    private List<Suggestion> suggestions;

    public RichResponse(List<ResponseElement> items, List<Suggestion> suggestions) {
        this.items = items;
        this.suggestions = suggestions;
    }

    public RichResponse() {
        items = new ArrayList<>();
        suggestions = new ArrayList<>();
    }

    @JsonIgnore
    public RichResponse addElement(SimpleResponse simpleResponse) {
        items.add(simpleResponse);
        return this;
    }

    @JsonIgnore
    public RichResponse addSuggestions(List<Suggestion> suggestion) {
        suggestions.addAll(suggestion);
        return this;
    }
}
