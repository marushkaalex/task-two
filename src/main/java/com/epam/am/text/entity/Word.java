package com.epam.am.text.entity;

import java.util.Iterator;

public class Word extends AbstractComposite<WordSymbol> implements SentencePart, Comparable<Word> {
    @Override
    public int compareTo(Word o) {
        Iterator<WordSymbol> myIterator = iterator();
        Iterator<WordSymbol> otherIterator = o.iterator();
        while (myIterator.hasNext()) {
            if (!otherIterator.hasNext()) {
                return 1;
            }

            WordSymbol mySymbol = myIterator.next();
            WordSymbol otherSymbol = otherIterator.next();
            int res = mySymbol.compareTo(otherSymbol);
            if (res == 0) continue;

            return res;
        }

        return 0;
    }
}
