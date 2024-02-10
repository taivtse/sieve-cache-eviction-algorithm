package com.github.taivtse.sieve;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SieveCache<K, V> {
  private final int capacity;
  private int size;
  private Node<K, V> head;
  private Node<K, V> tail;
  private Node<K, V> hand;
  private final Map<K, Node<K, V>> nodeMap = new HashMap<>();

  public SieveCache(int capacity) {
    this.capacity = capacity;
  }

  public V access(K key, Supplier<V> valueSupplier) {
    System.out.printf("Accessing \"%s\" from cache\n", key);
    V value = get(key);
    if (value == null) {
      value = valueSupplier.get();
      put(key, value);
    }
    return value;
  }

  private void put(K key, V value) {
    Node<K, V> existingNode = nodeMap.get(key);
    if (existingNode != null) {
      existingNode.value = value;
      return;
    }

    if (size == capacity) { // Cache full
      evict();  // Eviction
    }

    System.out.printf("Putting \"%s\" into cache\n", key);
    Node<K, V> newNode = addToHead(key, value);
    nodeMap.put(key, newNode);
    size++;
  }

  private V get(K key) {
    Node<K, V> node = nodeMap.get(key);
    if (node != null) { // Cache hit
      node.visited = true;
      return node.value;
    }
    return null;  // Cache miss
  }

  public void showCache() {
    System.out.println("============== Cache Data ==============");
    Node<K, V> cursor = head;
    while (cursor != null) {
      System.out.printf("\"%s\"=\"%s\" (visited=%s)\n", cursor.key, cursor.value, cursor.visited);
      cursor = cursor.next;
    }
    System.out.println("========================================");
  }

  private Node<K, V> addToHead(K key, V value) {
    Node<K, V> newNode = new Node<>(key, value);
    if (head == null) {
      tail = newNode;
      hand = newNode;
    } else {
      head.prev = newNode;
      newNode.next = head;
    }
    head = newNode;
    return newNode;
  }

  private void removeNode(Node<K, V> node) {
    if (node.prev != null) {
      node.prev.next = node.next;
    } else {
      head = node.next; // Remove head
    }

    if (node.next != null) {
      node.next.prev = node.prev;
    } else {
      tail = node.prev; // Remove tail
    }

    // Unlink removed node
    node.prev = null;
    node.next = null;
  }

  private void evict() {
    while (hand.visited) {
      hand.visited = false;
      hand = hand.prev;
      if (hand == null) {
        hand = tail;  // Reset the loop
      }
    }

    Node<K, V> evictedNode = hand;
    // Move the hand to the previous node
    hand = hand.prev;
    if (hand == null) {
      hand = tail;
    }

    // Delete node
    System.out.printf("Evicting \"%s\" from cache\n", evictedNode.key);
    removeNode(evictedNode);
    nodeMap.remove(evictedNode.key);
    size--;
  }

  private static class Node<K, V> {
    private final K key;
    private V value;
    private boolean visited;
    private Node<K, V> prev;
    private Node<K, V> next;

    public Node(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }
}