/*
    The original version was taken from line-solver-java (https://github.com/imperial-qore/line-solver-java),
            under the BSD-3 license.

    All code here was written by Matthew Sheldon, also the primary developer for this project.
 */

package AgentTools.Util;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Pair<T, U> implements Comparable<Pair<T,U>>, Serializable, Collection<Object> {
    private T left;
    private U right;

    public Pair(T left, U right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return this.left;
    }

    public U getRight() {
        return this.right;
    }

    public void setLeft(T left) {
        this.left = left;
    }

    public void setRight(U right) {
        this.right = right;
    }

    @Override
    public int compareTo(Pair<T,U> other) {
        if (this.left instanceof Comparable) {
            return ((Comparable)this.left).compareTo(other.getLeft());
        } else if (this.right instanceof Comparable) {
            return ((Comparable)this.right).compareTo(other.getRight());
        }

        return 0;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object inObj) {
        return ((this.left == inObj) || (this.right == inObj));
    }

    @Override
    public Iterator<Object> iterator() {
        List<Object> listRep = new ArrayList<Object>(2);
        listRep.add(this.left);
        listRep.add(this.right);
        return listRep.iterator();
    }

    @Override
    public Object[] toArray() {
        Object[] outArray = new Object[2];
        outArray[0] = this.left;
        outArray[1] = this.right;
        return outArray;
    }

    @Override
    public <V> V[] toArray(V[] a) {
        a[0] = (V)this.left;
        a[1] = (V)this.right;

        return a;
    }

    @Override
    public boolean add(Object o) {
        if (this.left == null) {
            this.left = (T)o;
            return true;
        } else if (this.right == null) {
            this.right = (U)o;
            return true;
        }

        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (this.left == o) {
            this.left = null;
            return true;
        } else if (this.right == o){
            this.right = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object a : c) {
            if ((this.left != c) && (this.right != c)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<?> c) {
        for (Object a : c) {
            if (!this.add(a)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object a : c) {
            if (!this.remove(a)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.contains(this.left) && c.contains(this.right)) {
            return false;
        }

        if (!c.contains(this.left)) {
            this.left = null;
        }

        if (!c.contains(this.right)) {
            this.right = null;
        }

        return true;
    }

    @Override
    public void clear() {
        this.left = null;
        this.right = null;
    }
}
