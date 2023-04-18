package com.example.lab4.utils.Events;

import com.example.lab4.domain.FriendshipDB;

public class ChangeEvent implements Event{
    private ChangeEventType type;
    private FriendshipDB newData, oldData;

    public ChangeEvent(ChangeEventType type, FriendshipDB newData) {
        this.type = type;
        this.newData = newData;
    }

}
