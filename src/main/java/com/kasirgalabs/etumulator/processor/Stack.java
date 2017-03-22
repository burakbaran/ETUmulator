/*
 * Copyright (C) 2017 Kasirgalabs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kasirgalabs.etumulator.processor;

import com.google.inject.Singleton;
import com.kasirgalabs.etumulator.pattern.Observable;
import com.kasirgalabs.etumulator.pattern.Observer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class Stack implements Observable {
    private final LinkedList<Integer> list;
    private final List<Observer> observers;

    public Stack() {
        list = new LinkedList<>();
        observers = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach((observer) -> {
            observer.update(Stack.class);
        });
    }

    public void push(Integer item) {
        list.push(item);
        notifyObservers();
    }

    public Integer pop() {
        Integer result = list.pop();
        notifyObservers();
        return result;
    }

    public List<Integer> getAll() {
        return list.subList(0, list.size());
    }
}
