package com.platform.pomodoropro.model;

import java.io.Serializable;

public interface EntityIdentifier extends Serializable {
    void setId(long id);
    long getId();
}