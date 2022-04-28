package AgentTools.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MultiSpace extends ValueSpace<List> {
    private class MultiSpaceIterator implements Iterator<List> {
        protected List<ValueSpace> spaces;
        protected List<Iterator> iterators;
        protected List<Object> currentValue;
        protected List<Boolean> hasNextList;
        protected boolean hasCurrent;

        public MultiSpaceIterator(List<ValueSpace> spaces) {
            this.spaces = spaces;

            this.iterators = new ArrayList<Iterator>();
            for (ValueSpace v : spaces) {
                this.iterators.add(v.iterator());
            }

            this.currentValue = new ArrayList<Object>();
            this.hasNextList  = new ArrayList<Boolean>();
            this.hasCurrent = true;

            for (Iterator i : this.iterators) {
                if (!i.hasNext()) {
                    this.hasCurrent = false;
                    break;
                } else {
                    this.currentValue.add(i.next());
                    this.hasNextList.add(i.hasNext());
                }
            }
        }

        protected List<Object> findNext() {
            boolean isDone = true;
            int nFinished = 0;

            for (Iterator it : this.iterators) {
                if (it.hasNext()) {
                    isDone = false;
                    break;
                } else {
                    nFinished++;
                }
            }

            if (isDone) {
                this.hasCurrent = false;
                return null;
            }

            int i = 0;
            int breakpoint = -1;
            List<Integer> toReset =  new ArrayList<Integer>();
            List<Object> toReturn = new ArrayList<Object>();

            for (Iterator it : this.iterators) {
                if (!it.hasNext() && (nFinished > 0)) {
                    nFinished--;
                    toReset.add(i);
                } else {
                    breakpoint = i;
                }
                i += 1;
            }

            for (int j = 0; j < this.iterators.size(); j++) {
                toReturn.set(j, this.currentValue.get(j));
            }
            for (int resetIdx : toReset) {
                this.iterators.set(resetIdx, this.spaces.get(resetIdx).iterator());
                toReturn.set(resetIdx, this.iterators.get(resetIdx).next());
            }

            if (breakpoint != -1) {
                toReturn.set(breakpoint, this.iterators.get(breakpoint).next());
            }

            return toReturn;
        }

        @Override
        public boolean hasNext() {
            return this.hasCurrent;
        }

        @Override
        public List next() {
            List<Object> toReturn = this.currentValue;
            this.currentValue = this.findNext();
            return toReturn;
        }
    }

    protected List<ValueSpace> spaces;

    public MultiSpace(List<ValueSpace> spaces) {
        this.spaces = spaces;
    }

    @Override
    public List getRealization(Random random) {
        List<Object> outList = new ArrayList<Object>();
        for (ValueSpace vs : this.spaces) {
            outList.add(vs.getRealization(random));
        }
        return outList;
    }

    @Override
    public Iterator<List> iterator() {
        return new MultiSpaceIterator(this.spaces);
    }

    @Override
    public int getSize() {
        int totalSize = 1;
        for (ValueSpace vs : this.spaces) {
            totalSize *= vs.getSize();
        }

        return totalSize;
    }
}
