package com.example.lab4.utils.Observer;

import com.example.lab4.utils.Events.Event;

public interface Observer <E extends Event>{

    void update(E e);
}
