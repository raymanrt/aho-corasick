package org.arabidopsis.ahocorasick;

import java.util.Iterator;
import java.util.Set;

public class Prove {
  public static void main(String[] args) {
    AhoCorasick tree = new AhoCorasick();

    tree.add(".", ".".toCharArray());
    tree.add("...", "...".toCharArray());
    tree.prepare();

    Iterator<SearchResult> it = tree.search("Questa prova è molto cattiva..".toCharArray());
    while(it.hasNext()) {
      SearchResult sr = it.next();
      System.out.println(sr.getLastIndex());

      Set<Object> outputs = sr.getOutputs();

      for(Object s : outputs)
        System.out.println(new String((char[]) s));
    }
  }
}
