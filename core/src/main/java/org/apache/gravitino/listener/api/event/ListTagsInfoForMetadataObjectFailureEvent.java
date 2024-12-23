package org.apache.gravitino.listener.api.event;

import org.apache.gravitino.MetadataObject;

public class ListTagsInfoForMetadataObjectFailureEvent extends TagFailureEvent {
    private final String metalake;
    private final MetadataObject metadataObject;
    public ListTagsInfoForMetadataObjectFailureEvent(String user, String metalake, MetadataObject metadataObject, Exception exception) {
        super(user, exception);
        this.metalake = metalake;
        this.metadataObject = metadataObject;
    }

    public String metalake() {
        return metalake;
    }

    public MetadataObject metadataObject() {
        return metadataObject;
    }

    @Override
    public OperationType operationType() {
        return OperationType.LIST_TAGS_INFO_FOR_METADATA_OBJECT;
    }
}
