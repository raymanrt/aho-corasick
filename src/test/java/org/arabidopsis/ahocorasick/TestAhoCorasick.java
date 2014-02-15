package org.arabidopsis.ahocorasick;

import java.util.Iterator;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Junit test cases for AhoCorasick.
 */
public class TestAhoCorasick {
  private AhoCorasick tree;

  @BeforeMethod
  public void setUp() {
    this.tree = new AhoCorasick();
  }

  @Test
  public void testConstruction() {
    tree.add("hello", "hello".toCharArray());
    tree.add("hi", "hi".toCharArray());
    tree.prepare();

    State s0 = tree.getRoot();
    State s1 = s0.get((char) 'h');
    State s2 = s1.get((char) 'e');
    State s3 = s2.get((char) 'l');
    State s4 = s3.get((char) 'l');
    State s5 = s4.get((char) 'o');
    State s6 = s1.get((char) 'i');

    assertEquals(s0, s1.getFail());
    assertEquals(s0, s2.getFail());
    assertEquals(s0, s3.getFail());
    assertEquals(s0, s4.getFail());
    assertEquals(s0, s5.getFail());
    assertEquals(s0, s6.getFail());

    assertEquals(0, s0.getOutputs().size());
    assertEquals(0, s1.getOutputs().size());
    assertEquals(0, s2.getOutputs().size());
    assertEquals(0, s3.getOutputs().size());
    assertEquals(0, s4.getOutputs().size());
    assertEquals(1, s5.getOutputs().size());
    assertEquals(1, s6.getOutputs().size());

    assertTrue(s6 != null);
  }

  @Test
  public void testExample() {
    tree.add("he", "he".toCharArray());
    tree.add("she", "she".toCharArray());
    tree.add("his", "his".toCharArray());
    tree.add("hers", "hers".toCharArray());
    assertEquals(10, tree.getRoot().size());
    tree.prepare();

    State s0 = tree.getRoot();
    State s1 = s0.get((char) 'h');
    State s2 = s1.get((char) 'e');

    State s3 = s0.get((char) 's');
    State s4 = s3.get((char) 'h');
    State s5 = s4.get((char) 'e');

    State s6 = s1.get((char) 'i');
    State s7 = s6.get((char) 's');

    State s8 = s2.get((char) 'r');
    State s9 = s8.get((char) 's');

    assertEquals(s0, s1.getFail());
    assertEquals(s0, s2.getFail());
    assertEquals(s0, s3.getFail());
    assertEquals(s0, s6.getFail());
    assertEquals(s0, s8.getFail());

    assertEquals(s1, s4.getFail());
    assertEquals(s2, s5.getFail());
    assertEquals(s3, s7.getFail());
    assertEquals(s3, s9.getFail());

    assertEquals(0, s1.getOutputs().size());
    assertEquals(0, s3.getOutputs().size());
    assertEquals(0, s4.getOutputs().size());
    assertEquals(0, s6.getOutputs().size());
    assertEquals(0, s8.getOutputs().size());
    assertEquals(1, s2.getOutputs().size());
    assertEquals(1, s7.getOutputs().size());
    assertEquals(1, s9.getOutputs().size());
    assertEquals(2, s5.getOutputs().size());
  }

  @Test
  public void testStartSearchWithSingleResult() {
    tree.add("apple", "apple".toCharArray());
    tree.prepare();
    SearchResult result = tree.startSearch("washington cut the apple tree".toCharArray());

    assertEquals(1, result.getOutputs().size());
    assertEquals("apple", new String((char[]) result.getOutputs().iterator().next()));
    assertEquals(24, result.getLastIndex());
    assertEquals(null, tree.continueSearch(result));
  }

  @Test
  public void testStartSearchWithAdjacentResults() {
    tree.add("john", "john".toCharArray());
    tree.add("jane", "jane".toCharArray());
    tree.prepare();
    SearchResult firstResult = tree.startSearch("johnjane".toCharArray());
    SearchResult secondResult = tree.continueSearch(firstResult);
    assertEquals(null, tree.continueSearch(secondResult));
  }

  @Test
  public void testStartSearchOnEmpty() {
    tree.add("cipher", new Integer(0));
    tree.add("zip", new Integer(1));
    tree.add("nought", new Integer(2));
    tree.prepare();
    SearchResult result = tree.startSearch("".toCharArray());
    assertEquals(null, result);
  }

  @Test
  public void testMultipleOutputs() {
    tree.add("x", "x");
    tree.add("xx", "xx");
    tree.add("xxx", "xxx");
    tree.prepare();

    SearchResult result = tree.startSearch("xxx".toCharArray());
    assertEquals(1, result.getLastIndex());
    assertEquals(new HashSet<Object>(Arrays.asList(new String[] {"x"})), result.getOutputs());

    result = tree.continueSearch(result);
    assertEquals(2, result.getLastIndex());
    assertEquals(new HashSet<Object>(Arrays.asList(new String[] {"xx", "x"})), result.getOutputs());

    result = tree.continueSearch(result);
    assertEquals(3, result.getLastIndex());
    assertEquals(new HashSet<Object>(Arrays.asList(new String[] {"xxx", "xx", "x"})),
        result.getOutputs());

    assertEquals(null, tree.continueSearch(result));
  }

  @Test
  public void testIteratorInterface() {
    tree.add("moo", "moo");
    tree.add("one", "one");
    tree.add("on", "on");
    tree.add("ne", "ne");
    tree.prepare();
    Iterator<SearchResult> iter = tree.search("one moon ago".toCharArray());

    assertTrue(iter.hasNext());
    SearchResult r = (SearchResult) iter.next();
    assertEquals(new HashSet<Object>(Arrays.asList(new String[] {"on"})), r.getOutputs());
    assertEquals(2, r.getLastIndex());

    assertTrue(iter.hasNext());
    r = (SearchResult) iter.next();
    assertEquals(new HashSet<Object>(Arrays.asList(new String[] {"one", "ne"})), r.getOutputs());
    assertEquals(3, r.getLastIndex());

    assertTrue(iter.hasNext());
    r = (SearchResult) iter.next();
    assertEquals(new HashSet<Object>(Arrays.asList(new String[] {"moo"})), r.getOutputs());
    assertEquals(7, r.getLastIndex());

    assertTrue(iter.hasNext());
    r = (SearchResult) iter.next();
    assertEquals(new HashSet<Object>(Arrays.asList(new String[] {"on"})), r.getOutputs());
    assertEquals(8, r.getLastIndex());

    assertFalse(iter.hasNext());

    try {
      iter.next();
      fail();
    } catch (NoSuchElementException e) {
    }
  }

  @Test
  public void largerTextExample() {
    String text = "The ga3 mutant of Arabidopsis is a gibberellin-responsive dwarf. We present data" +
      "showing that the ga3-1 mutant is deficient in ent-kaurene oxidase activity, the first" +
      "cytochrome P450-mediated step in the gibberellin biosynthetic pathway. By using a combination" +
      "of conventional map-based cloning and random sequencing we identified a putative cytochrome" +
      "P450 gene mapping to the same location as GA3. Relative to the progenitor line, two ga3 mutant" +
      "alleles contained single base changes generating in-frame stop codons in the predicted amino" +
      "acid sequence of the P450. A genomic clone spanning the P450 locus complemented the ga3-2" +
      "mutant. The deduced GA3 protein defines an additional class of cytochrome P450 enzymes. The" +
      "GA3 gene was expressed in all tissues examined, RNA abundance being highest in inflorescence" +
      "tissue.";

    String[] terms = {
      "microsome", "cytochrome", "cytochrome P450 activity", "gibberellic acid biosynthesis", "GA3",
      "cytochrome P450", "oxygen binding", "AT5G25900.1", "protein", "RNA", "gibberellin",
      "Arabidopsis", "ent-kaurene oxidase activity", "inflorescence", "tissue",
    };

    for (int i = 0; i < terms.length; i++) {
      tree.add(terms[i], terms[i]);
    }
    tree.prepare();

    Set<Object> termsThatHit = new HashSet<Object>();
    for (Iterator<SearchResult> iter = tree.search(text.toCharArray()); iter.hasNext(); ) {
      SearchResult result = (SearchResult) iter.next();
      termsThatHit.addAll(result.getOutputs());
    }

    assertEquals
      (new HashSet<Object>(Arrays.asList(new String[] {
        "cytochrome",
        "GA3",
        "cytochrome P450",
        "protein",
        "RNA",
        "gibberellin",
        "Arabidopsis",
        "ent-kaurene oxidase activity",
        "inflorescence",
        "tissue",
      })), termsThatHit);

  }
}
