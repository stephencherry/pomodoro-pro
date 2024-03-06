package com.platform.pomodoropro.entity.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

public interface WithDates extends Serializable {
    void setCreatedDate(OffsetDateTime createdDate);
    OffsetDateTime getCreatedDate();
    void setUpdatedDate(OffsetDateTime updatedDate);
    OffsetDateTime getUpdatedDate();
}
