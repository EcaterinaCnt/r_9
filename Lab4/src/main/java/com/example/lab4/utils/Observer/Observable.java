package com.example.lab4.utils.Observer;

import com.example.lab4.utils.Events.Event;

public interface Observable <E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObserver(E t);
}
