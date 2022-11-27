package ru.avalon.javapp.devj110.doublelinkedlist;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class DoubleLinkedList<T> implements Iterable<T>, Cloneable{
    private ListEx<T> head;
    private ListEx<T> tail;

    public DoubleLinkedList() {
    }

    public DoubleLinkedList(ListEx<T> head, ListEx<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    //    добавление значения в начало списка;
    public void addToHead(T value) {
        if( head != null) {
            ListEx<T> nh = new ListEx<>(value);
            nh.next = head;
            head.previous = nh;
            head = nh;
        } else {
            head = tail = new ListEx<>(value);
        }
    }

    // добавление всех значений заданного массива в начало списка с сохранением порядка
    public void addAllToHead(T[] values) {
        for (int i = values.length - 1; i >= 0 ; i--) {
            addToHead(values[i]);
        }
    }

    //извлечение значения из начала списка без его удаления из списка;
    public T peekFromHead() {
        return head != null ? head.value : null;
    }

    //    извлечение значения из начала списка с удалением из списка;
    public T peekAndRemoveFromHead() {
        if(head == null)
            return null;

        T res = head.value;

        if(head != tail) {
            head = head.next;
            head.previous = null;
        } else {
            head = tail = null;
        }
        return res;
    }

    //добавление значения в конец списка;
    public void addToTail(T value) {
        if(tail != null) {
            ListEx<T> nt = new ListEx<>(value);
            tail.next = nt;
            nt.previous = tail;
            tail = tail.next;
        } else {
            head = tail = new ListEx<>(value);
        }
    }

    //добавление всех значений заданного массива в конец списка с сохранением порядка
    public void addAllToTail(T[] values) {
        for (int i = 0; i < values.length; i++) {
            addToTail(values[i]);
        }
    }

    // извлечение значения из конца списка без его удаления;
    public T peekFromTail() {
        return tail != null ? tail.value : null;
    }

    //извлечение значения из конца списка с удалением;
    public T peekAndRemoveFromTail() {
        if(tail == null)
            return null;

        T res = tail.value;

        if( head != tail ) {
            tail = tail.previous;
            tail.next = null;
        } else {
            head = tail = null;
        }
        return res;
    }

    //определение, является ли список пустым, или нет
    public boolean isEmpty() {
        return head == null;
    }

    //определение, содержит ли список заданное значение, или нет
    public boolean contains(Object val) {
        ListEx<T> it = head;
        while (it != null) {
            if(it.keeps(val))
                return true;

            it = it.next;
        }
        return false;
    }

    //поглощение списка другим списком с добавлением значений второго в начало
    public boolean absorptionAndAddToHead(DoubleLinkedList<T> list) {
        if (list.head == null)
            return false;

        if(head != null) {
            list.tail.next = head;
            head.previous = list.tail;
            head = list.head;
            list.head = list.tail = null;
            return true;
        } else {
            releasingList(list);
            return true;
        }
    }

    //поглощение списка другим списком с добавлением значений второго в конец
    public boolean absorptionAndAddToTail(DoubleLinkedList<T> list) {
        if (list.head == null)
            return false;

        if(head != null) {
            list.head.previous = tail;
            tail.next = list.head;
            tail = list.tail;
            list.head = list.tail = null;
            return true;
        } else {
            releasingList(list);
            return true;
        }
    }
    private void releasingList (DoubleLinkedList<T> list) {
        head = list.head;
        tail = list.tail;
        list.head = list.tail = null;
    }

    //печать всех значений списка в прямом порядке
    public void printAllWithHead() {
        if ( head == null)
            System.out.println("null");
        forEach(System.out::println);
        System.out.println();
    }

    //печать всех значений списка в обратном порядке
    public void printAllWithTail() {
        forEachReversed(System.out::println);
        System.out.println();
    }

    //выполнять заданное действие для каждого значения списка в прямом порядке
    public void forEach(Consumer<? super T> action) {
        ListEx<T> it = head;
        while (it != null) {
            action.accept(it.value);
            it = it.next;
        }
    }

    //выполнять заданное действие для каждого значения списка в обратном порядке
    public void forEachReversed(Consumer<? super T> action) {
        ListEx<T> it = tail;
        while (it != null) {
            action.accept(it.value);
            it = it.previous;
        }
    }

    //агрегировать значения в прямом порядке
    public <A> A aggregateWithHead(A init, BiFunction<? super A,? super T, ? extends A> aggrFunk) {
        A res = init;
        ListEx<T> it = head;
        while (it != null) {
            res = aggrFunk.apply(res, it.value);
            it = it.next;
        }
        return res;
    }

    //агрегировать значения в прямом порядке
    public <A> A aggregateWithTail(A init, BiFunction<? super A,? super T,? extends A> aggrFunk) {
        A res = init;
        ListEx<T> it = tail;
        while (it != null) {
            res = aggrFunk.apply(res, it.value);
            it = it.previous;
        }
        return res;
    }

    @Override
    public Iterator<T> iterator() {
        return new ForwardIterator<>(head);
    }

    private static class ForwardIterator<V> implements Iterator<V>{
        ListEx<V> ex;

        ForwardIterator(ListEx<V> ex) {
            this.ex = ex;
        }

        @Override
        public boolean hasNext() {
            return ex != null ;
        }

        @Override
        public V next() {
            if( ex == null)
                throw new NoSuchElementException();
            V res = ex.value;
            ex = ex.next;
            return res;
        }
    }

    //для итерирования списка в обратном порядке
    public DoubleLinkedList<T> reverse() {
        if(head == null)
            return new DoubleLinkedList<>();

        ListEx<T> newTail = new ListEx<>(head.value),
                newHead = newTail,
                it = head;
        while (it.next != null) {
            it = it.next;
            ListEx<T> nh = new ListEx<>(it.value);
            nh.next = newHead;
            nh.previous = null;
            newHead.previous = nh;
            newHead = nh;
        }
        return new DoubleLinkedList<>(newHead, newTail);
    }

    @Override
    public DoubleLinkedList<T> clone() throws CloneNotSupportedException {
        DoubleLinkedList<T> copy = (DoubleLinkedList<T>) super.clone();
        DoubleLinkedList<T> result = new DoubleLinkedList<>();

        for(T el : copy) {
            result.addToTail(el);
        }
         return result;
    }

    // определение узла цепочки DoubleLinkedList
    private static class ListEx<T> implements Cloneable {
        T value;
        ListEx<T> next;
        ListEx<T> previous;

        ListEx(T value) {
            this.value = value;
        }

        boolean keeps(Object val) {
            return value == null && val == null ||
                    value != null && value.equals(val);
        }
    }
}
