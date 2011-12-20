package org.arabidopsis.ahocorasick;

import junit.framework.TestCase;

public class TestQueue extends TestCase {
    public void testSimple() {
	Queue<State> q = new Queue<State>();
	State s1 = new State(0);
	State s2 = new State(0);
	State s3 = new State(0);
	State s4 = new State(0);
	State s5 = new State(0);
	assertTrue(q.isEmpty());
	q.add(s1);
	assertFalse(q.isEmpty());
	assertEquals(s1, q.pop());

	q.add(s2);
	q.add(s3);
	assertEquals(s2, q.pop());
	q.add(s4);
	q.add(s5);
	assertEquals(s3, q.pop());
	assertEquals(s4, q.pop());
	assertEquals(s5, q.pop());
	assertTrue(q.isEmpty());
    }
}
