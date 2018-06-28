package com.example.shilpipandey.ruzzle;

import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Shilpi Pandey on 30-03-2018.
 */

public class trieDictionary  {
    private int MAX_SIZE=26;


    public trieDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim().toLowerCase();
            //Log.i("word is ",word);
            insert(word);
        }
    }
    public trieDictionary() {

    }
    private class trieNode {
        private trieNode[] children=new trieNode[MAX_SIZE];
        private boolean isEndWord;
        trieNode() {
            isEndWord=false;
            for(int i=0;i<MAX_SIZE;i++)
                children[i]=null;
        }
    }

    trieNode root=new trieNode();
    public void insert(String s) {
        int index=0,n=s.length();
        trieNode iterator=root;
        for(int i=0;i<n;i++) {
            index=s.charAt(i)-97;
            if(index>26||index<0)
                return;
            if(iterator.children[index]==null)
                iterator.children[index]=new trieNode();
            iterator=iterator.children[index];
        }
        iterator.isEndWord=true;
    }
    public int search(String s) {
        int index=0,n=s.length();
        trieNode iterator=root;
        for(int i=0;i<n;i++) {
            index=s.charAt(i)-97;
            if(index>26||index<0)
                return 0;
            if(iterator.children[index]==null)
                return 0;
            iterator=iterator.children[index];
        }
        if(iterator!=null && iterator.isEndWord==true )
            return 1;
        else if(iterator.isEndWord==false)
            return -1;
        else
            return 0;
    }


}
