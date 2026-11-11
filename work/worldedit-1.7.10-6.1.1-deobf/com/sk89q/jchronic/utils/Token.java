/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.utils;

import com.sk89q.jchronic.tags.Tag;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Token {
    private String _word;
    private List<Tag<?>> _tags;

    public Token(String word) {
        this._word = word;
        this._tags = new LinkedList();
    }

    public String getWord() {
        return this._word;
    }

    public void tag(Tag<?> newTag) {
        this._tags.add(newTag);
    }

    public void untag(Class<?> tagClass) {
        Iterator<Tag<?>> tagIter = this._tags.iterator();
        while (tagIter.hasNext()) {
            Tag<?> tag = tagIter.next();
            if (!tagClass.isInstance(tag)) continue;
            tagIter.remove();
        }
    }

    public boolean isTagged() {
        return !this._tags.isEmpty();
    }

    public <T extends Tag> T getTag(Class<T> tagClass) {
        List<T> matches = this.getTags(tagClass);
        Tag matchingTag = null;
        if (matches.size() > 0) {
            matchingTag = (Tag)matches.get(0);
        }
        return (T)matchingTag;
    }

    public List<Tag<?>> getTags() {
        return this._tags;
    }

    public <T extends Tag<?>> List<T> getTags(Class<T> tagClass) {
        LinkedList matches = new LinkedList();
        for (Tag<?> tag : this._tags) {
            if (!tagClass.isInstance(tag)) continue;
            matches.add(tag);
        }
        return matches;
    }

    public String toString() {
        return String.valueOf(this._word) + " " + this._tags;
    }
}

