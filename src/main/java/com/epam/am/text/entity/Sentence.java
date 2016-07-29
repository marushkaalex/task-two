package com.epam.am.text.entity;

public class Sentence extends AbstractComposite<SentencePart> {
    public long getWordCount() {
        return componentList.stream()
                .filter(i -> i instanceof Word)
                .count();
    }
}
