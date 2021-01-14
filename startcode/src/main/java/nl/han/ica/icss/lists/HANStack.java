package nl.han.ica.icss.lists;

import nl.han.ica.datastructures.IHANStack;

import java.util.EmptyStackException;

public class HANStack<T> implements IHANStack {
    private Object _top;
    private HANStack<Object> _bottom;

    public HANStack(){}

    public HANStack(Object value, HANStack<Object> child) {
        this._top = value;
        this._bottom = child;
    }

    @Override
    public void push(Object value) {
        this._bottom = new HANStack<>(this._top, this._bottom);
        this._top = value;
    }

    @Override
    public Object pop() {
        // If the stack is empty throw error.
        if(empty()){
            throw new EmptyStackException();
        }

        // Set new top and bind current values.
        Object newTop = this._top;
        this._top = this._bottom._top;
        this._bottom = this._bottom._bottom;

        // Give the new Top back.
        return newTop;
    }

    @Override
    public Object peek() {
        return this._top;
    }
    public boolean empty(){
        return this._bottom == null;
    }
}
