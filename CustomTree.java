package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.*;


public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    Entry<String> root;
    //private static final long serialVersionUID = 1L;
    private transient int size;

    static class Entry<T> implements Serializable {
        String elementName;
        boolean availableToAddLeftChildren;
        boolean availableToAddRightChildren;
        Entry<T> parent;
        Entry<T> leftChild;
        Entry<T> rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            this.availableToAddLeftChildren = true;
            this.availableToAddRightChildren = true;
        }

        public boolean isAvailableToAddChildren() {
            return (availableToAddLeftChildren || availableToAddRightChildren);
        }
    }

    public CustomTree() {
        this.root = new Entry<>("root");
        this.root.parent = null;
        this.size = 0;
    }

    @Override
    public String get(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean add(String s) {
        //int maxDeep = getSubTreeDeep(root);
        Queue<Entry<String>> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Entry<String> node = queue.remove();
            // if current node can have a child, do it - left first, if left already exist then right
            if (node.availableToAddLeftChildren) {
                Entry<String> newEntry = new Entry<>(s);
                newEntry.parent = node;
                node.leftChild = newEntry;
                node.availableToAddLeftChildren = false;
                ++this.size;
                queue.clear();
                return true;
            }
            if (node.availableToAddRightChildren) {
                Entry<String> newEntry = new Entry(s);
                newEntry.parent = node;
                node.rightChild = newEntry;
                node.availableToAddRightChildren = false;
                ++this.size;
                queue.clear();
                return true;
            }
            // both children are present - need to move down to the next level
            if (node.leftChild != null) {
                queue.add(node.leftChild);
            }
            if (node.rightChild != null) {
                queue.add(node.rightChild);
            }

        }
        return false;
    }

    public String getParent(String s) {
        if ("root".equals(s)) {
            return null;
        }
        Entry<String> node = findEntry(s, root);
        if (node == null) {
            return null;
        }
        return node.parent.elementName;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof String)) {
            throw new UnsupportedOperationException();
        }
        Entry<String> entryToDelete = findEntry((String) o, root);
        Entry<String> parentOfDeleted = entryToDelete.parent;
        this.size -= (getSubTreeNodeCount(entryToDelete) + 1);
        if (parentOfDeleted.leftChild == entryToDelete) {
            parentOfDeleted.leftChild = null;
        } else if (parentOfDeleted.rightChild == entryToDelete) {
            parentOfDeleted.rightChild = null;
        }
        if ((parentOfDeleted.leftChild == null) && (parentOfDeleted.rightChild == null)) {
            parentOfDeleted.availableToAddLeftChildren = true;
            parentOfDeleted.availableToAddRightChildren = true;
        }
        return true;
    }

    private Entry<String> findEntry(String entryName, Entry<String> startNode) {
        Queue<Entry<String>> queue = new ArrayDeque<>();
        queue.add(startNode);
        while (!queue.isEmpty()) {
            Entry<String> node = queue.remove();
            if (node.elementName.equals(entryName)) {
                queue.clear();
                return node;
            }
            if (node.leftChild != null) {
                queue.add(node.leftChild);
            }
            if (node.rightChild != null) {
                queue.add(node.rightChild);
            }
        }
        return null;
    }

    private int getSubTreeNodeCount(Entry<String> startNode) {
        Queue<Entry<String>> queue = new ArrayDeque<>();
        queue.add(startNode);
        int count = 0;
        while (!queue.isEmpty()) {
            Entry<String> node = queue.remove();
            if (node != startNode) {
                count++;
            }
            if (node.leftChild != null) {
                queue.add(node.leftChild);
            }
            if (node.rightChild != null) {
                queue.add(node.rightChild);
            }
        }
        return count;
    }
}
