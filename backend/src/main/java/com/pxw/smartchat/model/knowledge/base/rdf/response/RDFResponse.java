package com.pxw.smartchat.model.knowledge.base.rdf.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RDFResponse {
    public String entityType;
    public String entityId;
    public String[] answer;
}
