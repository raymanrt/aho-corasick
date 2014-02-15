package org.arabidopsis.ahocorasick;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import org.testng.annotations.Test;

/**
 *  Quick and dirty code: measures the amount of time it takes to construct an AhoCorasick tree out
 *  of all the words in <tt>/usr/share/dict/words</tt>.
 */
public class TestTimeTrial {
  @Test(timeOut = 10000)
  public void timeTrial() throws IOException {
    long startTime = System.currentTimeMillis();
    AhoCorasick tree = new AhoCorasick();
    BufferedReader reader = new BufferedReader
      (new InputStreamReader (new FileInputStream("/usr/share/dict/words")));
    String line;
    while ((line = reader.readLine()) != null) {
      tree.add(line, null);
    }
    tree.prepare();
    long endTime = System.currentTimeMillis();
    System.out.println("endTime - startTime = " + (endTime - startTime) + " milliseconds");
  }
}
