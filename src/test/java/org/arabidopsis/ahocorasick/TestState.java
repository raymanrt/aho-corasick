package org.arabidopsis.ahocorasick;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestState {
  @Test
  public void testSimpleExtension() {
    State s = new State(0);
    State s2 = s.extend("a".toCharArray()[0]);
    assertTrue(s2 != s && s2 != null);
    assertEquals(2, s.size());
  }

  @Test
  public void testSimpleExtensionSparse() {
    State s = new State(50);
    State s2 = s.extend((char) 3);
    assertTrue(s2 != s && s2 != null);
    assertEquals(2, s.size());
  }

  @Test
  public void testSingleState() {
    State s = new State(0);
    assertEquals(1, s.size());
  }

  @Test
  public void testSingleStateSparse() {
    State s = new State(50);
    assertEquals(1, s.size());
  }

  @Test
  public void testExtendAll() {
    State s = new State(0);
    State s2 = s.extendAll("hello world".toCharArray());
    assertEquals(12, s.size());
  }

  @Test
  public void testExtendAllTwiceDoesntAddMoreStates() {
    State s = new State(0);
    State s2 = s.extendAll("hello world".toCharArray());
    State s3 = s.extendAll("hello world".toCharArray());
    assertEquals(12, s.size());
    assertTrue(s2 == s3);
  }

  @Test
  public void testExtendAllTwiceDoesntAddMoreStatesSparse() {
    State s = new State(50);
    State s2 = s.extendAll("hello world".toCharArray());
    State s3 = s.extendAll("hello world".toCharArray());
    assertEquals(12, s.size());
    assertTrue(s2 == s3);
  }

  @Test
  public void testAddingALotOfStatesIsOk() {
    State s = new State(0);
    for (int i = 0; i < 256; i++)
      s.extend((char) i);
    assertEquals(257, s.size());
  }

  @Test
  public void testAddingALotOfStatesIsOkOnSparseRep() {
    State s = new State(50);
    for (int i = 0; i < 256; i++)
      s.extend((char) i);
    assertEquals(257, s.size());
  }
}
