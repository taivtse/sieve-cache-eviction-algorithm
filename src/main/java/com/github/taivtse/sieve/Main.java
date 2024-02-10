package com.github.taivtse.sieve;

/**
 * @author taivt
 * @since 2024/02/10 21:10:48
 */
public class Main {
  public static void main(String[] args) {
    SieveCache<String, String> cache = new SieveCache<>(7);
    // Init cache
    cache.access("a", () -> "this is a");
    cache.access("b", () -> "this is b");
    cache.access("c", () -> "this is c");
    cache.access("d", () -> "this is d");
    cache.access("e", () -> "this is e");
    cache.access("f", () -> "this is f");
    cache.access("g", () -> "this is g");
    cache.showCache();

    // Access a, b, g from cache
    System.out.println("\"a\" value = " + cache.access("a", () -> "this is a"));
    System.out.println("\"b\" value = " + cache.access("b", () -> "this is b"));
    System.out.println("\"g\" value = " + cache.access("g", () -> "this is g"));
    cache.showCache();

    // Put more items to cache to trigger cache eviction
    cache.access("h", () -> "this is h");
    cache.showCache();
    cache.access("a", () -> "this is a");
    cache.showCache();
    cache.access("d", () -> "this is d");
    cache.showCache();
    cache.access("i", () -> "this is i");
    cache.showCache();
    cache.access("b", () -> "this is b");
    cache.showCache();
    cache.access("j", () -> "this is j");
    cache.showCache();
  }

}