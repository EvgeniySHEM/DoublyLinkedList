package ru.avalon.javapp.devj110.doublelinkedlist;

public class DoubleLinkedList {
    private ListEx head;
    private ListEx tail;

    //    добавление значения в начало списка;
    public void addToHead(Object value) {
        if( head != null) {
            ListEx nh = new ListEx(value);
            nh.next = head;
            head.previous = nh;
            nh.previous = null;
            head = nh;
        } else {
            head = tail = new ListEx(value);
        }
    }

    // добавление всех значений заданного массива в начало списка с сохранением порядка
    public void addAllToHead(Object[] values) {
        for (int i = values.length - 1; i >= 0 ; i--) {
            addToHead(values[i]);
        }
    }

    //извлечение значения из начала списка без его удаления из списка;
    public Object peekFromHead() {
        return head != null ? head.value : null;
    }

    //    извлечение значения из начала списка с удалением из списка;
    public Object peekAndRemoveFromHead() {
        if(head == null)
            return null;

        Object res = head.value;

        if(head != tail) {
            head = head.next;
            head.previous = null;
        } else {
            head = tail = null;
        }
        return res;
    }

    //добавление значения в конец списка;
    public void addToTail(Object value) {
        if(tail != null) {
            ListEx nt = new ListEx(value);
            tail.next = nt;
            nt.previous = tail;
            tail = tail.next;
        } else {
            head = tail = new ListEx(value);
        }
    }

    //добавление всех значений заданного массива в конец списка с сохранением порядка
    public void addAllToTail(Object[] values) {
        for (int i = 0; i < values.length; i++) {
            addToTail(values[i]);
        }
    }

    // извлечение значения из конца списка без его удаления;
    public Object peekFromTail() {
        return tail != null ? tail.value : null;
    }

    //извлечение значения из конца списка с удалением;
    public Object peekAndRemoveFromTail() {
        if(tail == null)
            return null;

        Object res = tail.value;

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
        ListEx it = head;
        while (it != null) {
            if(it.keeps(val))
                return true;

            it = it.next;
        }
        return false;
    }

    //поглощение списка другим списком с добавлением значений второго в начало
    public boolean absorptionAndAddToHead(DoubleLinkedList list) {
        if (list.head == null)
            return false;

        ListEx it = list.head;
        while (it != null) {
            addToHead(it.value);
            it = it.next;
        }

        list.head = list.tail = null;
        return true;
    }

    //поглощение списка другим списком с добавлением значений второго в конец
    public boolean absorptionAndAddToTail(DoubleLinkedList list) {
        if (list.head == null)
            return false;

        ListEx it = list.head;
        while (it != null) {
            addToTail(it.value);
            it = it.next;
        }

        list.head = list.tail = null;
        return true;
    }

    //печать всех значений списка в прямом порядке
    public void printAllWithHead() {
        ListEx it = head;
        if ( it == null)
            System.out.println("null");

        while (it != null) {
            System.out.println(it.value);
            it = it.next;
        }
        System.out.println();
    }

    //печать всех значений списка в обратном порядке
    public void printAllWithTail() {
        ListEx it = tail;
        while (it != null) {
            System.out.println(it.value);
            it = it.previous;
        }
        System.out.println();
    }



    // определение узла цепочки DoubleLinkedList
    private static class ListEx {
        Object value;
        ListEx next;
        ListEx previous;

        ListEx(Object value) {
            this.value = value;
        }

        boolean keeps(Object val) {
            return value == null && val == null ||
                    value != null && value.equals(val);
        }
    }
}
