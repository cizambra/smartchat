package com.pxw.smartchat.model.knowledge;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.ListIterator;

@RequiredArgsConstructor
@EqualsAndHashCode
@Data
public class Entity {
    final String name;
    final String description;
    ArrayList<Relation> relationships;
    ArrayList<Keyword> keywords;

    public ArrayList<String> getValuesFromKeywords() {
        final ArrayList<String> values = new ArrayList<>();

        for (ListIterator<Keyword> iter = keywords.listIterator(); iter.hasNext();) {
            values.add(iter.next().getValue());
        }
        return values;
    }
}
