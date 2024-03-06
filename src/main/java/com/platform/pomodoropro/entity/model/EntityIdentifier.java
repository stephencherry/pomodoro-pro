package com.platform.pomodoropro.entity.model;

import java.io.Serializable;

public interface EntityIdentifier extends Serializable {
    void setId(long id);
    long getId();
}